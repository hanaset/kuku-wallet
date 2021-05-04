package com.hanaset.kuku.common.repository

import com.hanaset.kuku.common.entity.UserTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTokenRepository: JpaRepository<UserTokenEntity, Long> {

    fun findByAccountId(accountId: Long): List<UserTokenEntity>

    fun findByAccountIdAndTokenId(accountId: Long, tokenId: Long): UserTokenEntity?
}