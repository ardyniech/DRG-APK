package com.example.core.repository

import com.example.shared.models.DriverMember

object SeedData {
    fun getInitialMembers(): List<DriverMember> = SeedDataMembers.getInitialMembers()
}
