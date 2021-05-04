package com.hanaset.kuku.api.config.klaytn

import com.klaytn.caver.Caver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KlaytnConfig(
    @Value("\${klaytn.endpoint}") private val endpoint: String
) {

    @Bean
    fun caver(): Caver {
        return Caver(endpoint)
    }
}