package jun.watson.loalife.server.repository

import jun.watson.loalife.server.entity.Resource
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceRepository : JpaRepository<Resource, Long>
