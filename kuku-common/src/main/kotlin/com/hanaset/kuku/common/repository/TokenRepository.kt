package com.hanaset.kuku.common.repository

import com.hanaset.kuku.common.entity.TokenEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository: JpaRepository<TokenEntity, Long> {

    fun findBySymbolAndNetworkAndPlatform(symbol: String, network: Network, platform: Platform): TokenEntity?
    fun findByAddressAndNetworkAndPlatform(address: String, network: Network, platform: Platform): TokenEntity?
}