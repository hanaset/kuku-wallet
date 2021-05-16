package com.hanaset.kuku.api.service

import com.hanaset.kuku.api.rest.dto.response.ContractInfo
import com.hanaset.kuku.api.rest.dto.response.EosResponse
import com.hanaset.kuku.common.entity.AccountEntity
import java.math.BigDecimal

abstract class AbstractAccountService {

    abstract fun getAccount(address: String): EosResponse

    abstract fun addAccount(address: String): AccountEntity

    abstract fun getContract(contract: String): ContractInfo

    abstract fun getBalance(address: String): BigDecimal

    abstract fun addContractByOwner(address: String, contract: String): ContractInfo

    abstract fun makeAccountResponse(address: String): EosResponse

    abstract fun makeContractInfo(address: String): ContractInfo
}