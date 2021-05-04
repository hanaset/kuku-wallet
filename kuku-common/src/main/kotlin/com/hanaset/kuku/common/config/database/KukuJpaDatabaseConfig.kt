package com.hanaset.kuku.common.config.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jmx.export.MBeanExporter
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource


@Configuration
@ComponentScan(basePackages = [
    "com.hanaset.kuku.common.repository"
])
@EnableJpaRepositories(
    basePackages = ["com.hanaset.kuku.common.repository"],
    entityManagerFactoryRef = "kukuEntityManagerFactory",
    transactionManagerRef = "kukuTransactionManager"
)
@PropertySource("classpath:properties/database/kuku-database-\${spring.profiles.active}.properties")
class KukuJpaDatabaseConfig(private val mbeanExporter: MBeanExporter) {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "kuku.jpa")
    fun kukuJpaProperties(): JpaProperties {
        return JpaProperties()
    }

    @Bean
    @Primary
    fun kukuHibernateSettings(): HibernateSettings {
        return HibernateSettings()
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "kuku")
    fun kukuHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun kukuDataSource(): DataSource {
        val dataSource = HikariDataSource(kukuHikariConfig())
        mbeanExporter.addExcludedBean("kukuDataSource")
        return dataSource
    }

    @Bean
    @Primary
    fun kukuEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(kukuDataSource())
            .packages("com.hanaset.kuku.common.entity")
            .persistenceUnit("kukuPersistenceUnit")
            .properties(getVendorProperties(kukuDataSource()))
            .build()
    }

    private fun getVendorProperties(dataSource: DataSource): Map<String, String> {
        var properties = kukuJpaProperties().properties
//        properties.put("hibernate.dialec", "org.hibernate.dialect.MySQL5InnoDBDialect")
        return properties
    }

    @Bean(name = ["kukuJdbcTemplate"])
    fun kukuJdbcTemplate(@Qualifier("kukuDataSource") dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    @Primary
    fun kukuTransactionManager(builder: EntityManagerFactoryBuilder): PlatformTransactionManager {
        return JpaTransactionManager(kukuEntityManagerFactory(builder).getObject()!!)
    }

    @Bean
    @Primary
    fun kukuTransactionTemplate(builder: EntityManagerFactoryBuilder): TransactionTemplate {
        return TransactionTemplate(kukuTransactionManager(builder))
    }
}