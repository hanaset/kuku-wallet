package com.hanaset.kuku.api.exception

open class KukuException(
    val code: ErrorCode,
    override val message: String? = code.message
): RuntimeException(
    message ?: code.message
)

class KukuKlaytnRPCException: KukuException(ErrorCode.KLAYTN_RPC_ERROR)
class BadRequestAccountException: KukuException(ErrorCode.KLAYTN_INVALID_ACCOUNT_ADDRESS)
class BadRequestContractException: KukuException(ErrorCode.KLAYTN_INVALID_CONTRACT_ADDRESS)