package jun.watson.loalife.server.controller

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicLong

@Component
class ApiCallCounter {
    private val counter = AtomicLong(0)

    val createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    val count: Long
        get() = counter.get()

    fun increment(): Long = counter.incrementAndGet()
}
