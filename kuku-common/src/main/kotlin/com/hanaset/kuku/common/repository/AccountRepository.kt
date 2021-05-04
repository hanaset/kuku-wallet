package com.hanaset.kuku.common.repository

import com.hanaset.kuku.common.entity.AccountEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<AccountEntity, Long> {

    fun findByAddressAndNetworkAndPlatform(address: String, network: Network, platform: Platform): AccountEntity?
}