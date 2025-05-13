package jun.watson.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class LevelParser: JsonDeserializer<Double>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Double {
        if (p.text == null) {
            return 0.0
        }

        val text = p.text.replace(",", "")

        return text.toDoubleOrNull() ?: 0.0
    }

}
