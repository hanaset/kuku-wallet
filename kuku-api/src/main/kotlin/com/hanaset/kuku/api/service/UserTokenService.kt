package com.hanaset.kuku.api.service

import com.hanaset.kuku.api.exception.BadRequestAccountException
import com.hanaset.kuku.common.entity.TokenEntity
import com.hanaset.kuku.common.entity.UserTokenEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import com.hanaset.kuku.common.repository.AccountRepository
import com.hanaset.kuku.common.repository.TokenRepository
import com.hanaset.kuku.common.repository.UserTokenRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserTokenService(
    private val tokenRepository: TokenRepository,
    private val accountRepository: AccountRepository,
    private val userTokenRepository: UserTokenRepository
) {

    fun getTokens(address: String, network: Network, platform: Platform): List<TokenEntity> {

        val account = accountRepository.findByAddressAndNetworkAndPlatform(address, network, platform)
                ?: return listOf()

        return userTokenRepository.findByAccountId(account.id)
            .mapNotNull { tokenRepository.findByIdOrNull(it.tokenId) }
    }

    fun saveToken(accountAddress: String, tokenName: String, tokenSymbol: String, tokenDecimal: Long, tokenAddress: String, network: Network, platform: Platform) {
        val account = accountRepository.findByAddressAndNetworkAndPlatform(accountAddress, network, platform)
                ?: throw BadRequestAccountException()
        val token = tokenRepository.findByAddressAndNetworkAndPlatform(tokenAddress, network, platform)
                ?: TokenEntity(
                    platform = platform,
                    network = network,
                    name = tokenName,
                    address = tokenAddress,
                    symbol = tokenSymbol,
                    decimals = tokenDecimal
                )

        val tokenId = if(token.id == -1L) tokenRepository.save(token).id else token.id

        if(userTokenRepository.findByAccountIdAndTokenId(accountId = account.id, tokenId = tokenId) == null) {

            val userToken = UserTokenEntity(
                accountId = account.id,
                tokenId = tokenId
            )

            userTokenRepository.save(userToken)
        }
    }
}