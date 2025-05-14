package jun.watson.loalife.server.repository

import jun.watson.loalife.server.entity.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, Long>
