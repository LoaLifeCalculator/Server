package jun.watson.loalife.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ServerConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .build()
    }

}
