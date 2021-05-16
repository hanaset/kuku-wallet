package com.hanaset.kuku.api.rest.dto.response

import java.math.BigDecimal
import java.math.BigInteger

data class EosResponse(
    val address: String,
    val balance: BigDecimal,
    val contracts: List<HoldingContractInfo> = mutableListOf()
)

data class ContractInfo(
    val name: String,
    val symbol: String,
    val address: String
)

data class TokenInfo(
    val tokenId: String,
    val url: String
)

data class HoldingContractInfo(
    val name: String,
    val symbol: String,
    val address: String,
    val totalSupply: BigInteger,
    val balance: Double,
    val tokens: List<TokenInfo>
)

