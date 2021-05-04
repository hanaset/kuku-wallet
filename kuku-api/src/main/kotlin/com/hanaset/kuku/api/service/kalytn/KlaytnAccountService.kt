package com.hanaset.kuku.api.service.kalytn

import com.hanaset.kuku.api.exception.BadRequestAccountException
import com.hanaset.kuku.api.exception.BadRequestContractException
import com.hanaset.kuku.api.getKlay
import com.hanaset.kuku.api.rest.dto.response.ContractInfo
import com.hanaset.kuku.api.rest.dto.response.EosResponse
import com.hanaset.kuku.api.rest.dto.response.HoldingContractInfo
import com.hanaset.kuku.api.service.AccountService
import com.hanaset.kuku.api.service.UserTokenService
import com.hanaset.kuku.common.entity.AccountEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import com.klaytn.caver.Caver
import com.klaytn.caver.methods.response.IAccountType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class KlaytnAccountService(
    private val caver: Caver,
    private val klaytnContractService: KlaytnContractService,
    private val userTokenService: UserTokenService,
    private val accountService: AccountService,
    @Value("\${klaytn.api.chain-id}") private val chainId: Long
) {

    private val rpc = caver.rpc
    private val network = when (chainId) {
        8217L -> Network.MAIN
        else -> Network.TEST
    }

    // Klaytn EOS Account
    fun getAccount(address: String): EosResponse {
        val accountEntity = accountService.getAccountOrNull(address, network, Platform.KLAYTN)
                ?: throw BadRequestAccountException()
        return makeAccountResponse(accountEntity.address)
    }

    fun addAccount(address: String): AccountEntity {
        val accountEntity = accountService.getAccountOrNull(address, network, Platform.KLAYTN)

        if (accountEntity != null) {
            return accountEntity
        }

        val account = rpc.klay.getAccount(address).send()

        if (account.result == null || account.result.account.type != IAccountType.AccType.EOA) {
            throw BadRequestAccountException()
        }

        return accountService.saveAccount(address, network, Platform.KLAYTN)
    }

    // Klaytn Smart Contract
    fun getContract(contract: String): ContractInfo {
        val account = rpc.klay.getAccount(contract).send()

        if (account.result == null || account.result.account.type != IAccountType.AccType.SCA) {
            throw BadRequestContractException()
        }

        return makeContractInfo(contract)
    }

    private fun getBalance(address: String): BigDecimal {
        val balance = caver.rpc.klay.getBalance(address).send()
        return balance.value.getKlay()
    }

    fun addContractByOwner(address: String, contract: String): ContractInfo {
        val account = rpc.klay.getAccount(contract).send()

        if (account.result == null || account.result.account.type != IAccountType.AccType.SCA) {
            throw BadRequestContractException()
        }

        val name = klaytnContractService.getName(contract)
        val symbol = klaytnContractService.getSymbol(contract)

        userTokenService.saveToken(accountAddress = address, tokenName = name, tokenSymbol = symbol, tokenAddress = contract, network = network, platform = Platform.KLAYTN)
        return ContractInfo(name, symbol, contract)
    }

    fun makeAccountResponse(address: String): EosResponse {

        val holdingTokens = userTokenService.getTokens(address, network, Platform.KLAYTN)
            .map {

                val tokens = klaytnContractService.getTokens(it.address, address)
                HoldingContractInfo(
                    name = it.name,
                    symbol = it.symbol,
                    address = it.address,
                    totalSupply = klaytnContractService.getTotalSupply(it.address),
                    tokens = tokens,
                    balance = tokens.size
                )
            }

        return EosResponse(
            address = address,
            balance = getBalance(address),
            contracts = holdingTokens
        )
    }

    fun makeContractInfo(contract: String): ContractInfo {
        val name = klaytnContractService.getName(contract)
        val symbol = klaytnContractService.getSymbol(contract)
        val totalSupply = klaytnContractService.getTotalSupply(contract)
        return ContractInfo(
            name = name,
            symbol = symbol,
            address = contract
        )
    }
}