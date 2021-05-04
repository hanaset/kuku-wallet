package com.hanaset.kuku.api.rest

import com.hanaset.kuku.api.rest.support.RestSupport
import com.hanaset.kuku.api.service.kalytn.KlaytnApiService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Klaytn RPC API"])
@RestController
@RequestMapping("/api/v1/klaytn/rpc")
class KlaytnRPCController(
    private val klaytnApiService: KlaytnApiService
) : RestSupport() {

    @GetMapping
    fun getAccount(@RequestParam("address") address: String): ResponseEntity<*> {
        return response(klaytnApiService.getAccount(address))
    }

    @GetMapping("/code")
    fun getCode(@RequestParam("address") address: String): ResponseEntity<*> {
        return response(klaytnApiService.getCode(address))
    }

    @GetMapping("/call")
    fun call(
        @RequestParam("address") address: String,
        @RequestParam("function") fnname: String,
        @RequestParam("inputClass") inputClass: List<String>,
        @RequestParam("inputs") inputs: List<String>,
        @RequestParam("ouputClass") outputClass: List<String>
    ): ResponseEntity<*> {
        return response(klaytnApiService.call(address, fnname, inputClass, inputs, outputClass))
    }

    @GetMapping("/transaction")
    fun getTransactionByHash(@RequestParam("hash") hash: String): ResponseEntity<*> {
        return response(klaytnApiService.getTransactionByHash(hash))
    }
    @GetMapping("/block")
    fun getBlock(@RequestParam("hast") hash:String): ResponseEntity<*> {
        return response(klaytnApiService.getBlockByHash(hash))
    }
}