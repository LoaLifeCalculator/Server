package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.api.LostArkAuctionResponseDto
import jun.watson.loalife.server.api.LostArkMarketResponseDto
import jun.watson.loalife.server.entity.Resource
import jun.watson.loalife.server.repository.ResourceRepository
import jun.watson.loalife.server.data.Item
import jun.watson.loalife.server.redis.CacheName.RESOURCE
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.RoundingMode

@Service
class ResourceService(
    val resourceRepository: ResourceRepository,
    val lostArkApi: LostArkApi
) {

    @Transactional
    @CacheEvict(value = [RESOURCE], allEntries = true)
    fun updateMarketResources() {
        val resources = resourceRepository.findAll()

        val searchResults = lostArkApi.searchMarketPrices(Item.marketItems)
        for (searchResult in searchResults) {
            val resource = resources.first { resource ->
                resource.item.korean == searchResult.name
            }

            resource.avgPrice = getDividedPrice(searchResult, resource.item)
        }
    }

    @Transactional
    @CacheEvict(value = [RESOURCE], allEntries = true)
    fun updateAuctionResources() {
        val resources = resourceRepository.findAll()

        val searchResults = lostArkApi.searchAuctionPrices(Item.auctionItems)
        for (searchResult in searchResults) {
            val resource = resources.first { resource ->
                resource.item.korean == searchResult.items[0].name
            }

            resource.avgPrice = getDividedPrice(searchResult, resource.item)
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(RESOURCE)
    fun getResources(): List<Resource> {
        return resourceRepository.findAll()
    }

    @Transactional
    fun initResources() {
        for (item in Item.entries) {
            resourceRepository.save(Resource(item))
        }
    }

    private fun getDividedPrice(responseDto: LostArkMarketResponseDto, item: Item): Double {
        val dividedAvgPrice = responseDto.avgPrice / item.priceDivider

        return dividedAvgPrice.toBigDecimal()
            .setScale(3, RoundingMode.HALF_UP)
            .toDouble()
    }

    private fun getDividedPrice(responseDto: LostArkAuctionResponseDto, item: Item): Double {
        val dividedPrice = responseDto.price.toDouble() / item.priceDivider

        return dividedPrice.toBigDecimal()
            .setScale(3, RoundingMode.HALF_UP)
            .toDouble()
    }

}
