object DependencyVersions {
	const val SWAGGER_VERSION = "2.9.2"
}

dependencies {

	implementation(project(":kuku-common"))

	//swagger
	implementation("io.springfox:springfox-swagger2:${DependencyVersions.SWAGGER_VERSION}")
	implementation("io.springfox:springfox-swagger-ui:${DependencyVersions.SWAGGER_VERSION}")

	implementation("org.springframework.boot:spring-boot-starter-aop")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	implementation("org.web3j:core:4.8.4")
	implementation("org.web3j:utils:4.8.4")

	implementation("com.klaytn.caver:core:1.6.0")
	implementation("com.google.code.gson:gson:2.8.5")

	implementation("com.squareup.okhttp3:okhttp:4.3.1")
}

springBoot.buildInfo { properties { } }

configurations {
	val archivesBaseName = "kuku-api-staging"
}