package jun.watson.loalife.server.redis

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jun.watson.loalife.server.redis.CacheName.API_CHARACTER_RESPONSE
import jun.watson.loalife.server.redis.CacheName.RESOURCE
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        val objectMapper = ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)

        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            )
    }

    @Bean
    fun cacheManager(redisConnectionFactory: LettuceConnectionFactory): RedisCacheManager {
        val defaultConfig = redisCacheConfiguration()

        val apiResponseConfig = defaultConfig.entryTtl(Duration.ofMinutes(10))
        val resourceConfig = defaultConfig.entryTtl(Duration.ofHours(1))

        return RedisCacheManager.builder(redisConnectionFactory)
            .withCacheConfiguration(API_CHARACTER_RESPONSE, apiResponseConfig)
            .withCacheConfiguration(RESOURCE, resourceConfig)
            .cacheDefaults(defaultConfig)
            .build()
    }

}
