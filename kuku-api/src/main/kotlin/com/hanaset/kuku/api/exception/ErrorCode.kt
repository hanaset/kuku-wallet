package com.hanaset.kuku.api.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "Not Found Token"),
    KLAYTN_RPC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Klaytn RPC Error"),
    KLAYTN_INVALID_ACCOUNT_ADDRESS(HttpStatus.BAD_REQUEST, "잘못된 지갑 주소입니다."),
    KLAYTN_INVALID_CONTRACT_ADDRESS(HttpStatus.BAD_REQUEST, "잘못된 컨트랙트 주소입니다.")
}