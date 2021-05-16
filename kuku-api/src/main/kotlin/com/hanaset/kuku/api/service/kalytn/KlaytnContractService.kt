package com.hanaset.kuku.api.service.kalytn

import com.hanaset.kuku.api.encode
import com.hanaset.kuku.api.getStandardValue
import com.hanaset.kuku.api.rest.dto.response.TokenInfo
import com.klaytn.caver.Caver
import com.klaytn.caver.abi.Function
import com.klaytn.caver.abi.FunctionEncoder
import com.klaytn.caver.abi.FunctionReturnDecoder
import com.klaytn.caver.abi.datatypes.Type
import com.klaytn.caver.methods.request.CallObject
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class KlaytnContractService(
    private val caver: Caver
) {

    fun getName(address: String): String {
        val function = FunctionEncoder.makeFunction("name", listOf(), listOf(), listOf("string"))
        val res = call(function, address)
        return res[0].value as String
    }

    fun getSymbol(address: String): String {
        val function = FunctionEncoder.makeFunction("symbol", listOf(), listOf(), listOf("string"))
        val res = call(function, address)
        return res[0].value as String
    }

    fun getDecimal(address: String): Long {
        val function = FunctionEncoder.makeFunction("decimal", listOf(), listOf(), listOf("string"))
        val res = call(function, address)

        return if (res.isEmpty()) {
            0L
        } else {
            res[0].value as Long
        }
    }

    fun getTotalSupply(address: String): BigInteger {
        val function = FunctionEncoder.makeFunction("totalSupply", listOf(), listOf(), listOf("uint256"))
        val res = call(function, address)
        return res[0].value as BigInteger
    }

    fun isNFT(contract: String, owner: String): Boolean {
        val function = FunctionEncoder.makeFunction("tokenOfOwnerByIndex", listOf("address", "uint256"), listOf(owner, 0), listOf("uint256"))
        val res = call(function, contract)

        return res.isNotEmpty()
    }

    fun getTokens(contract: String, owner: String, decimal: Long): List<TokenInfo> {
        val balance = balanceOf(contract, owner).getStandardValue(decimal)
        return (0 until balance.toLong()).map { index ->
            val tokenId = getToken(contract, owner, index.toString())
            val url = getTokenURI(contract, tokenId.toString(16))
            TokenInfo(
                tokenId = tokenId.toString(),
                url = url
            )
        }
    }


    fun getTokenURI(contract: String, tokenId: String): String {
        val function = FunctionEncoder.makeFunction("tokenURI", listOf("uint256"), listOf(tokenId), listOf("string"))
        val res = call(function, contract)

        return if (res.isEmpty()) {
            ""
        } else {
            res[0].value as String
        }
    }

    fun getToken(contract: String, owner: String, index: String): BigInteger {
        val function = FunctionEncoder.makeFunction("tokenOfOwnerByIndex", listOf("address", "uint256"), listOf(owner, index), listOf("uint256"))
        val res = call(function, contract)

        return if(res.isEmpty()) {
            BigInteger.ZERO
        } else {
            res[0].value as BigInteger
        }
    }

    fun balanceOf(contract: String, owner: String): BigInteger {
        val function = FunctionEncoder.makeFunction("balanceOf", listOf("address"), listOf(owner), listOf("uint256"))
        val res = call(function, contract)
        return res[0].value as BigInteger
    }

    fun call(function: Function, to: String, from: String? = null): List<Type<Any>> {

        val call = CallObject.createCallObject()
            .apply {
                this.data = function.encode()
                this.to = to
                this.from = from
            }

        val res = caver.rpc.klay.call(call).send()
        return FunctionReturnDecoder.decode(res.result, function.outputParameters)
    }

    fun getCode(address: String): Any {
        return caver.rpc.klay.getCode(address).send()
    }

    fun getTokenA(address: String): String {
        val function = FunctionEncoder.makeFunction("tokenA", listOf(), listOf(), listOf("address"))
        val res = call(function, address)
        return res[0].value as String
    }

    fun getMining(address: String): String {
        val function = FunctionEncoder.makeFunction("mining", listOf(), listOf(), listOf("uint256"))
        val res = call(function, address)
        return res[0].value as String
    }
}