# DOCUMENTATION: DRG (DRIVER RIANG GEMBIRA) MOBILE PLATFORM

## 1. Executive Overview
DRG (Driver Riang Gembira) is a production-grade, local-first Android application designed specifically for online driver communities (ojek online). It integrates real-time GPS satellite radar tracking, emergency SOS alerts, transparent community treasury management, digital member identification cards (KTA Digital), role-based governance, weighted gamification peer-rating, community tasks, push notification preferences, and an interactive road hazard layer.

---

## 2. Architecture & Directory Structure
The application strictly follows Clean Architecture / MVVM with modern Unidirectional Data Flow (UDF) and local-first Room database persistence.

```
project_root/
├── core/
│   ├── database/                  # Room Entities, DAOs (Gamification, Hazard, NotifPref, Member, etc.), Converters
│   ├── repository/                # DRG Repository & Seed Data Generators
│   └── viewmodel/                 # DRGViewModel & MainNavTab
├── shared/
│   ├── atoms/                     # Reusable Material 3 UI Components & Role Badges
│   └── models/                    # Immutable Domain Models (BadgeItem, CommunityTask, RewardItem, HazardArea, PointTransaction, NotificationPreference)
├── modules/
│   ├── admin/                     # Admin Governance & Dewan Etika
│   ├── dashboard/                 # Home Dashboard & Quick Shift Actions
│   ├── emergency/                 # SOS Radar & Escort Monitoring
│   ├── forum_workshop/            # Technical Forum & Workshop Partners
│   ├── gamification/              # Leaderboard, Badges, Tasks, Rewards, Award Point Dialog
│   ├── members/                   # Driver List, Screening, & Posko
│   ├── notifications/             # Notification Center & Notification Preferences Dialog
│   ├── profile/                   # Digital KTA & Profile Settings
│   ├── radar/                     # Canvas Radar, Satellite GPS Grid, Hazard Layer, Consent Switch
│   ├── treasury/                  # Realtime Kas & Financial Ledger
│   └── main/                      # Bottom Navigation & App Container
└── ui/theme/                      # M3 Vibrant Palette Color Tokens & Typography
```

---

## 3. Role-Based Access Control (RBAC) & Point Weights
DRG features 7 distinct organizational roles:
1. **KETUA / WAKIL KETUA (Chairman)**: Complete administrative control, broadcast announcements, screening approvals. **Point Award Weight: +4 XP**.
2. **BENDAHARA (Treasurer)**: Full access to community treasury management and receipt logs. **Point Award Weight: +3 XP**.
3. **SEKRETARIS (Secretary)**: Member registration management and screening verification. **Point Award Weight: +3 XP**.
4. **SATGAS (Road Patrol Task Force)**: Roadside emergency response management and escort monitoring. **Point Award Weight: +3 XP**.
5. **DEWAN ETIKA (Ethics Committee)**: Ethical oversight and conduct screening. **Point Award Weight: +5 XP**.
6. **PENASIHAT (Advisory Board)**: Strategic guidance and read-only audit access. **Point Award Weight: +3 XP**.
7. **BENEFICIARY DIRECT (Korban / Receiver of Help)**: Driver receiving direct roadside help from another member. **Point Award Weight: +2 XP**.
8. **ANGGOTA DRIVER (Driver Member)**: Access to GPS radar, SOS button, treasury transparency, forum, tasks, and reward store. **Point Award Weight: +1 XP**.

---

## 4. Key Functional Features (Version 1 + Version 2)

### A. Advanced Interactive Radar & Hazard Layer
- **Clean Satellite Canvas**: Modern high-contrast dark satellite grid with distance rings and radar sweep animation.
- **Hazard Zone Layer (Area Rawan)**: Mark and view dangerous road spots (*Jalur Rawan Begal*, *Genangan Banjir*, *Jalan Berlubang Parah*, *Macet Total/Penyekatan*).
- **Privacy Consent Toggle**: Opt-in switch for live location broadcasting to protect member privacy.

### B. Gamification & Loyalty Engine
- **Badges Collection**: Earnable badges (*Pejuang Kopdar*, *Pahlawan Aspal*, *Donatur Setia Kas*, *Suhu Mekanik*, *Eksekutor Tugas*).
- **Role-Weighted Peer Rating**: Point gifting with role-based weights.
- **Community Tasks (Misi Komunitas)**: Tasks issued by leadership with specific task XP rewards.
- **Rewards Store (Tukar Poin)**: Redeem points for fuel vouchers, kas exemption, metallic stickers, and workshop discounts.
- **Leaderboard**: Real-time driver rankings.

### C. Comprehensive Push Notifications & Preferences
- **Per-Role Preferences Matrix**: Custom toggles for SOS Alerts, Kopdar Reminders, Kas Ledger Updates, Forum Activity, and Point Appreciation.
- **Notification Center**: Filtered notification feed with actionable cards.

---

## 5. Database Schema & Entities
1. `members`: DriverMember entity with location consent flag.
2. `emergency_alerts`: EmergencyAlert entity for SOS calls.
3. `posko_locations`: PoskoLocation entity for basecamps.
4. `kas_transactions`: KasTransaction entity for treasury ledger.
5. `forum_posts`: ForumPost entity for technical discussions.
6. `workshops`: WorkshopPartner entity for repair discounts.
7. `attendance_events`: AttendanceEvent entity for Kopdar events.
8. `driver_reviews`: DriverReview entity for peer feedback.
9. `notifications`: CommunityNotification entity.
10. `badges`: BadgeItem entity for earnable badges.
11. `community_tasks`: CommunityTask entity for community work assignments.
12. `rewards`: RewardItem entity for perk redemptions.
13. `hazard_areas`: HazardArea entity for road danger spots.
14. `point_transactions`: PointTransaction entity for point audit trails.
15. `notification_preferences`: NotificationPreference entity per driver.
