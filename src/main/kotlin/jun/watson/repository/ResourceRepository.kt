package jun.watson.repository

import jun.watson.entity.ResourceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceRepository : JpaRepository<ResourceEntity, Long>
