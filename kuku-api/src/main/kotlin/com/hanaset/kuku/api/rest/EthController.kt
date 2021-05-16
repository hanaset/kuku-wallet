package com.hanaset.kuku.api.rest

import com.hanaset.kuku.api.rest.support.RestSupport
import com.hanaset.kuku.api.service.eth.EthAccountService
import com.hanaset.kuku.api.service.eth.EthApiService
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

@Api(tags = ["Ethereum Account API"])
@RestController
@RequestMapping("/api/v1/eth")
class EthController(
    private val ethAccountService: EthAccountService,
    private val ethApiService: EthApiService
) : RestSupport() {

    @ApiImplicitParam(name = "address", defaultValue = "0x192e1922E6F1ea5931Be9c97b3AdEC00514abcd0")
    @GetMapping("/account/{address}")
    fun getAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(ethAccountService.getAccount(address))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0x192e1922E6F1ea5931Be9c97b3AdEC00514abcd0")
    @PostMapping("/account/{address}")
    fun addAccount(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(ethAccountService.addAccount(address))
    }

    @ApiImplicitParams(
        value = [
            ApiImplicitParam(name = "address", defaultValue = "0x192e1922E6F1ea5931Be9c97b3AdEC00514abcd0"),
            ApiImplicitParam(name = "contract", defaultValue = "0x06012c8cf97bead5deae237070f9587f8e7a266d")
        ]
    )
    @PostMapping("/account/{address}/{contract}")
    fun addContractByOwner(
        @PathVariable("address") address: String,
        @PathVariable("contract") contract: String
    ): ResponseEntity<*> {
        return response(ethAccountService.addContractByOwner(address, contract))
    }

    @ApiImplicitParam(name = "address", defaultValue = "0x06012c8cf97bead5deae237070f9587f8e7a266d")
    @GetMapping("/contract/{address}")
    fun getContractInfo(@PathVariable("address") address: String): ResponseEntity<*> {
        return response(ethAccountService.getContract(address))
    }

    @GetMapping("/test")
    fun test(@RequestParam("address") address: String): ResponseEntity<*> {
        return response(ethApiService.getName(address))
    }
}