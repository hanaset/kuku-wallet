package com.hanaset.kuku.api.service.eth

import com.hanaset.kuku.api.exception.BadRequestAccountException
import com.hanaset.kuku.api.exception.BadRequestContractException
import com.hanaset.kuku.api.getStandardValue
import com.hanaset.kuku.api.rest.dto.response.ContractInfo
import com.hanaset.kuku.api.rest.dto.response.EosResponse
import com.hanaset.kuku.api.service.AbstractAccountService
import com.hanaset.kuku.api.service.AccountService
import com.hanaset.kuku.api.service.UserTokenService
import com.hanaset.kuku.common.entity.AccountEntity
import com.hanaset.kuku.common.entity.enums.Network
import com.hanaset.kuku.common.entity.enums.Platform
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class EthAccountService(
    private val ethApiService: EthApiService,
    private val accountService: AccountService,
    private val userTokenService: UserTokenService,
    @Value("\${ethereum.chain}") private val chain: String
): AbstractAccountService() {

    private val network = when(chain) {
        "MAIN" -> Network.MAIN
        else -> Network.TEST
    }

    override fun getAccount(address: String): EosResponse {
        val accountEntity = accountService.getAccountOrNull(address, network, Platform.ETHEREUM)
                ?: throw BadRequestAccountException()

        TODO()
        return makeAccountResponse(accountEntity.address)
    }

    override fun addAccount(address: String): AccountEntity {
        val accountEntity = accountService.getAccountOrNull(address, network, Platform.ETHEREUM)

        if(accountEntity != null) {
            return accountEntity
        }

        val result = ethApiService.getCode(address)

        if(result.result.length > 3 || result.error == null) {
            throw BadRequestAccountException()
        }

        return accountService.saveAccount(address, network, Platform.ETHEREUM)
    }

    override fun getBalance(address: String): BigDecimal {
        val balance = ethApiService.getBalance(address)
        return balance.balance.getStandardValue(18)
    }

    override fun addContractByOwner(address: String, contract: String): ContractInfo {
        val account = ethApiService.getCode(contract)

        if(account.result.length < 3 || account.error == null) {
            throw BadRequestContractException()
        }

        val name = ethApiService.getName(contract)
        val symbol = ethApiService.getSymbol(contract)
        val decimal = ethApiService.getDecimal(contract)

        userTokenService.saveToken(address, name, symbol, decimal, contract, network, Platform.ETHEREUM)
        return ContractInfo(name, symbol, contract)
    }

    override fun getContract(contract: String): ContractInfo {
        val account = ethApiService.getCode(contract)

        if(account.result.length < 3 || account.error == null) {
            throw BadRequestContractException()
        }

        return makeContractInfo(contract)
    }

    override fun makeAccountResponse(address: String): EosResponse {

        val holdingTokens = userTokenService.getTokens(address, network, Platform.ETHEREUM)
            .map {

                val balance = ethApiService.balanceOf(it.address, address).getStandardValue(it.decimals).toDouble()
            }

        TODO()
    }

    override fun makeContractInfo(contract: String): ContractInfo {
        val name = ethApiService.getName(contract)
        val symbol = ethApiService.getSymbol(contract)
        return ContractInfo(
            name = name,
            symbol = symbol,
            address = contract
        )
    }
}