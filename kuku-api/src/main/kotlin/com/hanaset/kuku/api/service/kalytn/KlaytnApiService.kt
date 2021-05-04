package com.hanaset.kuku.api.service.kalytn

import com.klaytn.caver.Caver
import com.klaytn.caver.abi.FunctionEncoder
import com.klaytn.caver.abi.FunctionReturnDecoder
import com.klaytn.caver.methods.request.CallObject
import com.klaytn.caver.methods.response.Account
import com.klaytn.caver.methods.response.Bytes
import org.springframework.stereotype.Service

@Service
class KlaytnApiService(
    private val caver: Caver
) {

    private val rpc = caver.rpc

    fun getAccount(address: String): Account {
        val account = rpc.klay.getAccount(address).send()
        return account
    }

    fun getCode(address: String): Bytes {
        val code = rpc.klay.getCode(address).send()
        return code
    }

    fun getTransactionByHash(hash: String): Any {
        val res = rpc.klay.getTransactionReceiptBySenderTxHash(hash).send()
        return res
    }

    fun getBlockByHash(hash: String): Any {
        val res = rpc.klay.getBlockByHash(hash).send()
        return res
    }


    fun call(address: String, fnname: String, inputClass: List<String>, inputs: List<String>, outputClass: List<String>): Any {

        val function = FunctionEncoder.makeFunction(fnname, inputClass, inputs, outputClass)
        val callObject = CallObject.createCallObject()
            .apply {
                this.data = FunctionEncoder.encode(function)
                this.to = address
            }

        val res = caver.rpc.klay.call(callObject).send()

        val decode = FunctionReturnDecoder.decode(res.result, function.outputParameters)

        return decode
    }
}