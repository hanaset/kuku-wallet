package com.hanaset.kuku.common.entity

import com.hanaset.kuku.common.converter.LocalDateTimeConverter
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_token")
class UserTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,

    @Column(name = "account_id")
    val accountId: Long,

    @Column(name = "token_id")
    val tokenId: Long
) {
    @Column(name = "created_at")
    @CreationTimestamp
    @Convert(converter = LocalDateTimeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now()
}