const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");
const path = require("node:path");
const rutDir = path.dirname(require.resolve("@firebase/rules-unit-testing"));
const firebaseCompat = require(require.resolve("firebase/compat/app", { paths: [rutDir] })).default;
require(require.resolve("firebase/compat/firestore", { paths: [rutDir] }));

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_ardy_001";
const ADMIN_EMAIL = "ardy.syafii@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

const serverTs = () => firebaseCompat.firestore.FieldValue.serverTimestamp();

function aliceDb() {
  return testEnv.authenticatedContext(ALICE_UID, { email: "alice@drg.id", email_verified: true }).firestore();
}

function bobDb() {
  return testEnv.authenticatedContext(BOB_UID, { email: "bob@drg.id", email_verified: true }).firestore();
}

function adminDb() {
  return testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL, email_verified: true }).firestore();
}

function spoofedUnverifiedAdminDb() {
  return testEnv.authenticatedContext("spoof_999", { email: ADMIN_EMAIL, email_verified: false }).firestore();
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: { rules, host: emulatorHost, port: emulatorPort },
  });
});

after(async () => {
  if (testEnv) await testEnv.cleanup();
});

beforeEach(async () => {
  if (testEnv) await testEnv.clearFirestore();
});

test("Unauthenticated user: denied across all protected collections", async () => {
  const unauth = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauth.collection("users").doc(ALICE_UID).get());
  await assertFails(unauth.collection("members").doc(ALICE_UID).get());
  await assertFails(unauth.collection("kas_transactions").where("userId", "==", ALICE_UID).get());
  await assertFails(unauth.collection("emergency_alerts").where("status", "==", "ACTIVE").get());
  await assertFails(unauth.collection("role_audit_logs").where("targetUserId", "==", ALICE_UID).get());
  await assertFails(unauth.collection("admins").doc(ADMIN_UID).get());
});

test("Users collection: owner create, query alignment, self-escalation & ghost fields denied", async () => {
  const db = aliceDb();
  await assertSucceeds(
    db.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Driver",
      email: "alice@drg.id",
      driverId: "DRG-A01",
      role: "ANGGOTA",
      createdAt: serverTs(db),
      updatedAt: serverTs(db),
    })
  );

  // Matching query succeeds; unfiltered list fails
  await assertSucceeds(db.collection("users").where("userId", "==", ALICE_UID).get());
  await assertFails(db.collection("users").get());

  // Cross-user Bob denied
  await assertFails(bobDb().collection("users").doc(ALICE_UID).get());

  // Self-escalation to KETUA on update denied
  await assertFails(
    db.collection("users").doc(ALICE_UID).update({
      role: "KETUA",
      updatedAt: serverTs(db),
    })
  );

  // Ghost field injection denied
  await assertFails(
    db.collection("users").doc(ALICE_UID).update({
      isSuperAdmin: true,
      updatedAt: serverTs(db),
    })
  );

  // Forged future timestamp denied
  const futureDate = new Date(Date.now() + 3600_000);
  await assertFails(
    db.collection("users").doc(ALICE_UID).update({
      displayName: "Alice Forged",
      updatedAt: futureDate,
    })
  );

  // Valid owner update succeeds
  await assertSucceeds(
    db.collection("users").doc(ALICE_UID).update({
      displayName: "Alice Rider Updated",
      updatedAt: serverTs(db),
    })
  );
});

test("Users collection: non-admin cannot self-register with KETUA role", async () => {
  const bob = bobDb();
  await assertFails(
    bob.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob Escalator",
      role: "KETUA",
      createdAt: serverTs(bob),
      updatedAt: serverTs(bob),
    })
  );
});

test("Private PII & Encrypted Backups & Sync Queue: strict owner-only isolation", async () => {
  const db = aliceDb();
  await assertSucceeds(
    db.collection("users").doc(ALICE_UID).collection("private").doc("pii").set({
      userId: ALICE_UID,
      phone: "081234567890",
      bloodType: "O",
      createdAt: serverTs(db),
      updatedAt: serverTs(db),
    })
  );

  const validSha256 = "a".repeat(64);
  await assertSucceeds(
    db.collection("users").doc(ALICE_UID).collection("backups").doc("backup_1").set({
      userId: ALICE_UID,
      checksumSha256: validSha256,
      encryptedPayload: "base64ciphertext==",
      schemaVersion: 14,
      createdAt: serverTs(db),
      updatedAt: serverTs(db),
    })
  );

  await assertSucceeds(
    db.collection("users").doc(ALICE_UID).collection("sync_queue").doc("sync_1").set({
      userId: ALICE_UID,
      entityType: "KAS_TX",
      entityId: "tx_1",
      payload: "{\"amount\":10000}",
      syncState: "SYNCED",
      createdAt: serverTs(db),
      updatedAt: serverTs(db),
    })
  );

  // Bob cannot access Alice's private PII, backups, or sync_queue
  const bob = bobDb();
  await assertFails(bob.collection("users").doc(ALICE_UID).collection("private").doc("pii").get());
  await assertFails(bob.collection("users").doc(ALICE_UID).collection("backups").doc("backup_1").get());
  await assertFails(bob.collection("users").doc(ALICE_UID).collection("sync_queue").doc("sync_1").get());
  await assertFails(
    bob.collection("users").doc(ALICE_UID).collection("backups").doc("evil").set({
      userId: BOB_UID,
      checksumSha256: validSha256,
      encryptedPayload: "evil",
      schemaVersion: 14,
      createdAt: serverTs(bob),
      updatedAt: serverTs(bob),
    })
  );
});

test("Members & RBAC: non-admin restricted, bootstrapped admin verified, spoofed admin blocked", async () => {
  const db = aliceDb();
  const adm = adminDb();
  const spoof = spoofedUnverifiedAdminDb();

  await assertSucceeds(
    db.collection("members").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice Driver",
      driverId: "DRG-A01",
      baseArea: "Malang Kota",
      role: "ANGGOTA",
      verificationStatus: "PENDING_SCREENING",
      loyaltyPoints: 10,
      isOnline: true,
      createdAt: serverTs(db),
      updatedAt: serverTs(db),
    })
  );

  // Alice cannot self-promote or self-verify
  await assertFails(
    db.collection("members").doc(ALICE_UID).update({
      role: "SATGAS",
      updatedAt: serverTs(db),
    })
  );
  await assertFails(
    db.collection("members").doc(ALICE_UID).update({
      verificationStatus: "VERIFIED",
      updatedAt: serverTs(db),
    })
  );

  // Alice can update allowed profile fields
  await assertSucceeds(
    db.collection("members").doc(ALICE_UID).update({
      baseArea: "Sawojajar",
      isOnline: false,
      updatedAt: serverTs(db),
    })
  );

  // Spoofed admin with email_verified: false is denied!
  await assertFails(
    spoof.collection("members").doc(ALICE_UID).update({
      role: "SATGAS",
      updatedAt: serverTs(spoof),
    })
  );

  // Bootstrapped admin (verified email) can promote and record immutable audit log
  await assertSucceeds(
    adm.collection("members").doc(ALICE_UID).update({
      role: "SATGAS",
      verificationStatus: "VERIFIED",
      updatedAt: serverTs(adm),
    })
  );

  await assertSucceeds(
    adm.collection("role_audit_logs").doc("log_1").set({
      actorUid: ADMIN_UID,
      targetUserId: ALICE_UID,
      previousRole: "ANGGOTA",
      newRole: "SATGAS",
      reason: "Promosi anggota terverifikasi",
      createdAt: serverTs(adm),
    })
  );

  // Audit log cannot be updated or deleted even by admin
  await assertFails(adm.collection("role_audit_logs").doc("log_1").update({ reason: "Tampered" }));
  await assertFails(adm.collection("role_audit_logs").doc("log_1").delete());
});

test("Kas transactions & Emergency alerts: amount bounds, responder counter & terminal state lock", async () => {
  const alice = aliceDb();
  const bob = bobDb();

  // Negative amount denied
  await assertFails(
    alice.collection("kas_transactions").doc("tx_bad").set({
      userId: ALICE_UID,
      title: "Invalid Kas",
      amount: -5000,
      type: "INCOME",
      category: "Iuran",
      createdAt: serverTs(alice),
      updatedAt: serverTs(alice),
    })
  );

  await assertSucceeds(
    alice.collection("kas_transactions").doc("tx_ok").set({
      userId: ALICE_UID,
      title: "Iuran Mingguan",
      amount: 15000,
      type: "INCOME",
      category: "Iuran",
      recordedBy: "Alice",
      createdAt: serverTs(alice),
      updatedAt: serverTs(alice),
    })
  );
  await assertSucceeds(alice.collection("kas_transactions").where("userId", "==", ALICE_UID).get());
  await assertFails(bob.collection("kas_transactions").doc("tx_ok").get());

  // Emergency alert creation & community visibility
  await assertSucceeds(
    alice.collection("emergency_alerts").doc("sos_1").set({
      userId: ALICE_UID,
      driverName: "Alice",
      alertType: "ACCIDENT",
      message: "Ban pecah di Soekarno Hatta",
      status: "ACTIVE",
      respondersCount: 0,
      createdAt: serverTs(alice),
      updatedAt: serverTs(alice),
    })
  );

  await assertSucceeds(bob.collection("emergency_alerts").where("status", "==", "ACTIVE").get());
  // Bob can increment respondersCount while ACTIVE
  await assertSucceeds(
    bob.collection("emergency_alerts").doc("sos_1").update({
      respondersCount: 1,
      updatedAt: serverTs(bob),
    })
  );
  // Bob cannot resolve Alice's alert
  await assertFails(
    bob.collection("emergency_alerts").doc("sos_1").update({
      status: "RESOLVED",
      updatedAt: serverTs(bob),
    })
  );
  // Alice resolves her alert
  await assertSucceeds(
    alice.collection("emergency_alerts").doc("sos_1").update({
      status: "RESOLVED",
      updatedAt: serverTs(alice),
    })
  );
  // Terminal state lock: once RESOLVED, updates denied
  await assertFails(
    alice.collection("emergency_alerts").doc("sos_1").update({
      message: "Late edit",
      updatedAt: serverTs(alice),
    })
  );
});

test("P2P peers, WebRTC signals & PTT transmissions: bounds, participant isolation & anti-spoofing", async () => {
  const unauth = testEnv.unauthenticatedContext().firestore();
  const alice = aliceDb();
  const bob = bobDb();

  // Unauthenticated denied
  await assertFails(unauth.collection("p2p_peers").where("status", "==", "ONLINE").get());
  await assertFails(unauth.collection("p2p_signals").where("targetUserId", "==", ALICE_UID).get());
  await assertFails(unauth.collection("ptt_transmissions").where("status", "==", "LIVE").get());

  // Peer registration: Alice cannot impersonate Bob's peer doc
  await assertFails(
    alice.collection("p2p_peers").doc(BOB_UID).set({
      userId: BOB_UID, driverName: "Spoof", channelId: "suhat", localIp: "192.168.1.2",
      port: 8080, status: "ONLINE", createdAt: serverTs(), updatedAt: serverTs(),
    })
  );
  await assertSucceeds(
    alice.collection("p2p_peers").doc(ALICE_UID).set({
      userId: ALICE_UID, driverName: "Alice", channelId: "suhat", localIp: "192.168.1.10",
      port: 8080, sdpOffer: "v=0...", status: "ONLINE", createdAt: serverTs(), updatedAt: serverTs(),
    })
  );
  await assertSucceeds(bob.collection("p2p_peers").where("channelId", "==", "suhat").where("status", "==", "ONLINE").get());

  // Signal packet: Alice sends OFFER to Bob; Bob queries targetUserId == BOB_UID & marks consumed
  await assertSucceeds(
    alice.collection("p2p_signals").doc("sig_1").set({
      senderUserId: ALICE_UID, targetUserId: BOB_UID, channelId: "suhat",
      signalType: "OFFER", payload: "sdp-offer-payload", consumed: false,
      createdAt: serverTs(), updatedAt: serverTs(),
    })
  );
  await assertSucceeds(bob.collection("p2p_signals").where("targetUserId", "==", BOB_UID).get());
  await assertFails(bob.collection("p2p_signals").get());
  await assertSucceeds(bob.collection("p2p_signals").doc("sig_1").update({ consumed: true, updatedAt: serverTs() }));
  // Alice cannot mark consumed=true on Bob's target signal
  await assertFails(alice.collection("p2p_signals").doc("sig_1").update({ consumed: false, updatedAt: serverTs() }));

  // PTT audio transmission: invalid duration rejected, valid burst succeeds, terminal ENDED locks updates
  await assertFails(
    alice.collection("ptt_transmissions").doc("ptt_bad").set({
      userId: ALICE_UID, senderName: "Alice", channelId: "suhat", audioBase64: "AAAA",
      codec: "ULAW_8K", sampleRateHz: 8000, durationMs: 999999, status: "LIVE",
      createdAt: serverTs(), updatedAt: serverTs(),
    })
  );
  await assertSucceeds(
    alice.collection("ptt_transmissions").doc("ptt_1").set({
      userId: ALICE_UID, senderName: "Alice", channelId: "suhat", audioBase64: "f39/f38=",
      codec: "ULAW_8K", sampleRateHz: 8000, durationMs: 1200, status: "LIVE",
      createdAt: serverTs(), updatedAt: serverTs(),
    })
  );
  await assertSucceeds(bob.collection("ptt_transmissions").where("channelId", "==", "suhat").where("status", "==", "LIVE").get());
  await assertFails(bob.collection("ptt_transmissions").doc("ptt_1").update({ status: "ENDED", updatedAt: serverTs() }));
  await assertSucceeds(alice.collection("ptt_transmissions").doc("ptt_1").update({ status: "ENDED", updatedAt: serverTs() }));
  await assertFails(alice.collection("ptt_transmissions").doc("ptt_1").update({ status: "LIVE", updatedAt: serverTs() }));
});
