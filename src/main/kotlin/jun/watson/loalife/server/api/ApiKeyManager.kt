package jun.watson.loalife.server.api

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api")
data class ApiKeyManager(
    val keys: Map<String, String>
) {
    fun get(keyFromClient: String? = null): String {
        return keyFromClient ?: keys.values.random()
    }
}
