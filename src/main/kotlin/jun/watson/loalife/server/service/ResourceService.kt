package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.api.LostArkItemResponseDto
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
    fun updateResources() {
        val resources = resourceRepository.findAll()
        val searchResults = lostArkApi.searchResourcePrices(Item.entries)

        for (searchResult in searchResults) {
            val resource = resources.first { resource ->
                resource.item.korean == searchResult.name
            }

            resource.avgPrice = getAvgPrice(searchResult, resource.item)
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

    private fun getAvgPrice(responseDto: LostArkItemResponseDto, item: Item): Double {
        val avgPrice = responseDto.stats[0].avgPrice / item.bundleCount

        return avgPrice.toBigDecimal()
            .setScale(3, RoundingMode.HALF_UP)
            .toDouble()
    }

}
