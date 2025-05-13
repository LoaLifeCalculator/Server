package jun.watson.service

import jun.watson.api.LostArkApi
import jun.watson.api.LostArkItemResponseDto
import jun.watson.entity.ResourceEntity
import jun.watson.entity.toEntityItem
import jun.watson.repository.ResourceRepository
import jun.watson.data.Item
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ResourceService(
    val resourceRepository: ResourceRepository,
    val lostArkApi: LostArkApi
) {

    @Transactional
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
    fun getResources(): List<ResourceEntity> {
        return resourceRepository.findAll()
    }

    @Transactional
    fun initResources() {
        for (item in Item.entries) {
            resourceRepository.save(ResourceEntity(item.toEntityItem()))
        }
    }

    private fun getAvgPrice(responseDto: LostArkItemResponseDto, item: Item): Double {
        return responseDto.stats[0].avgPrice / item.bundleCount
    }

}
