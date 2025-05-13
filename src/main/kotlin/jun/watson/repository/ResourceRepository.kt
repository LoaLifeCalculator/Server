package jun.watson.repository

import jun.watson.entity.Resource
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceRepository : JpaRepository<Resource, Long>
