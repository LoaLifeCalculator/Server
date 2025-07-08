package jun.watson.loalife.server.service

import org.springframework.util.StringUtils

const val DEFAULT_NAME = "Suchu"

fun getQueryName(name: String?): String {
    if (name == null || !StringUtils.hasText(name)) {
        return DEFAULT_NAME
    }

    return name.lowercase().trim()
}
