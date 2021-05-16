package com.hanaset.kuku.api.service.eth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetCode
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import javax.annotation.PostConstruct

@Service
class EthApiService(
    @Value("\${ethereum.endpoint}") private val endpoint: String
) {

    private lateinit var web3j: Web3j

    @PostConstruct
    fun init() {
        this.web3j = Web3j.build(HttpService(endpoint))
    }

    fun getBalance(address: String): EthGetBalance {
        return web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send()
    }

    fun getCode(address: String): EthGetCode {
        return web3j.ethGetCode(address, DefaultBlockParameter.valueOf("latest")).send()
    }


    fun call(function: Function, to: String, from: String? = null): List<Type<Any>> {

        val transaction = Transaction.createEthCallTransaction(from, to, FunctionEncoder.encode(function))
        val res = web3j.ethCall(transaction, DefaultBlockParameter.valueOf("latest")).send()

        return FunctionReturnDecoder.decode(res.result, function.outputParameters)
    }

    fun getName(address: String): String {
        val function = FunctionEncoder.makeFunction("name", listOf(), listOf(), listOf("string"))
        val res = call(function, address)
        return res.first().value as String
    }

    fun getSymbol(address: String): String {
        val function = FunctionEncoder.makeFunction("symbol", listOf(), listOf(), listOf("string"))
        val res = call(function, address)
        return res.first().value as String
    }

    fun getDecimal(address: String): Long {
        val function = FunctionEncoder.makeFunction("decimal", listOf(), listOf(), listOf("string"))
        val res = call(function, address)

        return if(res.isEmpty()) {
            0
        } else {
            res.first().value as Long
        }
    }

    fun balanceOf(contract: String, owner: String): BigInteger {
        val function = FunctionEncoder.makeFunction("balanceOf", listOf("address"), listOf(owner), listOf("uint256"))
        val res = call(function, contract)

        return res[0].value as BigInteger
    }
}