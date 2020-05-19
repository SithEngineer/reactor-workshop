package reactive

import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.util.*

class EventData {

    fun coldProducer(count: Int = 5): Flux<Event> = (0..count).toFlux().map { Event() }

    data class Event(val id: String = UUID.randomUUID().toString(), val type: Type = Type.values().random()) {
        override fun toString(): String {
            return "{" +
                    "\"id\": \"$id\"," +
                    "\"type\":\"$type\"" +
                    "}"
        }
    }

    enum class Type {
        A, B, C
    }
}