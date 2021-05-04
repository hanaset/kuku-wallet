package com.hanaset.kuku.api.rest

import com.hanaset.kuku.api.rest.support.RestSupport
import com.hanaset.kuku.api.service.kalytn.KlaytnAccountService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Klaytn Account API"])
@RestController
@RequestMapping("/api/v1/klaytn")
class KlaytnController(
    private val klaytnAccountService: KlaytnAccountService
) : RestSupport() {

    @ApiImplicitParam(name = "address", defaultValue = "0xd92c4b5b3c8bbeff63a80bc6df161232a4a266bc")
    @GetMapping("/account/{address}")
    fun getAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.getAccount(address))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0xd92c4b5b3c8bbeff63a80bc6df161232a4a266bc")
    @PostMapping("/account/{address}")
    fun addAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.addAccount(address))
    }

    @ApiImplicitParams(
        value = [
            ApiImplicitParam(name = "address", defaultValue = "0xd92c4b5b3c8bbeff63a80bc6df161232a4a266bc"),
            ApiImplicitParam(name = "contract", defaultValue = "0xe517ad1ddcb59fc00f0a27fce055dab18df857ac")
        ]
    )
    @PostMapping("/account/{address}/{contract}")
    fun addContractByOwner(
        @PathVariable("address") address: String,
        @PathVariable("contract") contract: String
    ): ResponseEntity<*> {
        return response(klaytnAccountService.addContractByOwner(address, contract))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0xe517ad1ddcb59fc00f0a27fce055dab18df857ac")
    @GetMapping("/contract/{address}")
    fun getContractInfo(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.getContract(address))
    }
}