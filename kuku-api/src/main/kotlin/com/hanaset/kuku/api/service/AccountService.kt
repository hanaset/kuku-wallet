package com.hanaset.kuku.api.service

import com.hanaset.kuku.common.entity.AccountEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import com.hanaset.kuku.common.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun getAccountOrNull(address: String, network: Network, platform: Platform): AccountEntity? {
       return accountRepository.findByAddressAndNetworkAndPlatform(address, network, platform)
    }

    fun saveAccount(address: String, network: Network, platform: Platform): AccountEntity {
        return accountRepository.save(AccountEntity(platform = platform, network = network, address = address))
    }
}