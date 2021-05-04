package com.hanaset.kuku.api

import com.klaytn.caver.abi.Function
import com.klaytn.caver.abi.FunctionEncoder
import java.math.BigDecimal
import java.math.BigInteger


fun BigInteger.getKlay(): BigDecimal {
    return BigDecimal(this).divide(BigDecimal.TEN.pow(18))
}

fun Function.encode(): String {
    return FunctionEncoder.encode(this)
}