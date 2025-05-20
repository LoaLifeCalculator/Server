package jun.watson.loalife.server.controller

import jun.watson.loalife.server.service.ResourceService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

@Controller
class DataUpdateController(
    private val resourceService: ResourceService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun initializeDatabase() {
        resourceService.initResources()
        resourceService.updateMarketResources()
        resourceService.updateAuctionResources()
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun dailyUpdate() {
        resourceService.updateMarketResources()
    }

}
