package jun.watson.loalife.server.api

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api")
data class ApiKeyManager(
    private val keys: Map<String, String>
) {
    fun getRandomKey(): String {
        return keys.values.random()
    }
}
