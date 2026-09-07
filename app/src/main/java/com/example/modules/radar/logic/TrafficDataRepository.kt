package com.example.modules.radar.logic

import com.example.modules.radar.models.TrafficIncident
import com.example.modules.radar.models.TrafficRoadSegment
import com.example.modules.radar.models.TrafficStatus

object TrafficDataRepository {
    fun getRoadSegments(): List<TrafficRoadSegment> = listOf(
        TrafficRoadSegment(
            id = "TRF-SUHAT",
            roadName = "Jl. Soekarno Hatta (Suhat)",
            points = listOf(Pair(-7.9400, 112.6140), Pair(-7.9450, 112.6160), Pair(-7.9510, 112.6190)),
            status = TrafficStatus.SMOOTH,
            avgSpeedKmh = 42,
            description = "Arus lancar menuju Jembatan UB"
        ),
        TrafficRoadSegment(
            id = "TRF-DINOYO",
            roadName = "Jl. MT Haryono (Dinoyo)",
            points = listOf(Pair(-7.9510, 112.6190), Pair(-7.9550, 112.6100), Pair(-7.9580, 112.6020)),
            status = TrafficStatus.CONGESTED,
            avgSpeedKmh = 22,
            description = "Padat merayap depan Mall Dinoyo City"
        ),
        TrafficRoadSegment(
            id = "TRF-KAYUTANGAN",
            roadName = "Jl. Basuki Rahmat (Kayutangan)",
            points = listOf(Pair(-7.9750, 112.6280), Pair(-7.9790, 112.6300), Pair(-7.9839, 112.6315)),
            status = TrafficStatus.JAMMED,
            avgSpeedKmh = 11,
            description = "Macet antrian wisata & simpang Rajabally"
        ),
        TrafficRoadSegment(
            id = "TRF-IJEN",
            roadName = "Jl. Besar Ijen",
            points = listOf(Pair(-7.9680, 112.6230), Pair(-7.9740, 112.6235), Pair(-7.9800, 112.6240)),
            status = TrafficStatus.SMOOTH,
            avgSpeedKmh = 48,
            description = "Jalur dua arah sangat lancar"
        ),
        TrafficRoadSegment(
            id = "TRF-ARJOSARI",
            roadName = "Jl. Ahmad Yani (Flyover Arjosari)",
            points = listOf(Pair(-7.9300, 112.6560), Pair(-7.9400, 112.6530), Pair(-7.9500, 112.6500)),
            status = TrafficStatus.CONGESTED,
            avgSpeedKmh = 26,
            description = "Padat volume bus & angkutan antar kota"
        ),
        TrafficRoadSegment(
            id = "TRF-GADANG",
            roadName = "Jl. Kolonel Sugiono (Pasar Gadang)",
            points = listOf(Pair(-8.0050, 112.6280), Pair(-8.0120, 112.6290), Pair(-8.0200, 112.6300)),
            status = TrafficStatus.JAMMED,
            avgSpeedKmh = 9,
            description = "Tersendat aktivitas bongkar muat pasar"
        )
    )

    fun getIncidents(): List<TrafficIncident> = listOf(
        TrafficIncident(
            id = "INC-01",
            locationName = "Simpang Empat Rajabally",
            lat = -7.9790,
            lng = 112.6300,
            speedKmh = 10,
            status = TrafficStatus.JAMMED,
            notes = "Traffic Light padat antrian kendaraan pribadi"
        ),
        TrafficIncident(
            id = "INC-02",
            locationName = "Jembatan Suhat",
            lat = -7.9430,
            lng = 112.6150,
            speedKmh = 44,
            status = TrafficStatus.SMOOTH,
            notes = "Jalur leluasa untuk motor & ojek online"
        ),
        TrafficIncident(
            id = "INC-03",
            locationName = "Pasar Gadang",
            lat = -8.0120,
            lng = 112.6290,
            speedKmh = 8,
            status = TrafficStatus.JAMMED,
            notes = "Macet merayap, disarankan ambil jalan pintas"
        )
    )
}
