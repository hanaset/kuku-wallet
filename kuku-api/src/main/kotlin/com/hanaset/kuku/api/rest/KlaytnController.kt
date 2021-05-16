package com.hanaset.kuku.api.rest

import com.hanaset.kuku.api.rest.support.RestSupport
import com.hanaset.kuku.api.service.kalytn.KlaytnAccountService
import com.hanaset.kuku.api.service.kalytn.KlaytnContractService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Klaytn Account API"])
@RestController
@RequestMapping("/api/v1/klaytn")
class KlaytnController(
    private val klaytnAccountService: KlaytnAccountService,
    private val klaytnContractService: KlaytnContractService
) : RestSupport() {

    @ApiImplicitParam(name = "address", defaultValue = "0x4bdfd18BBF8F32F72Ee935068Fd586cFeD765cd9")
    @GetMapping("/account/{address}")
    fun getAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.getAccount(address))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0x4bdfd18BBF8F32F72Ee935068Fd586cFeD765cd9")
    @PostMapping("/account/{address}")
    fun addAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.addAccount(address))
    }

    @ApiImplicitParams(
        value = [
            ApiImplicitParam(name = "address", defaultValue = "0x4bdfd18BBF8F32F72Ee935068Fd586cFeD765cd9"),
            ApiImplicitParam(name = "contract", defaultValue = "0xc6a2ad8cc6e4a7e08fc37cc5954be07d499e7654")
        ]
    )
    @PostMapping("/account/{address}/{contract}")
    fun addContractByOwner(
        @PathVariable("address") address: String,
        @PathVariable("contract") contract: String
    ): ResponseEntity<*> {
        return response(klaytnAccountService.addContractByOwner(address, contract))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0x4bdfd18BBF8F32F72Ee935068Fd586cFeD765cd9")
    @GetMapping("/contract/{address}")
    fun getContractInfo(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(klaytnAccountService.getContract(address))
    }

    @GetMapping("/test")
    fun test(@RequestParam("address") address: String): ResponseEntity<*> {
        return response(klaytnContractService.getMining(address))
    }
}