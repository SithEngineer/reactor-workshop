package reactive

import reactor.core.publisher.Mono

interface Client {
    fun get(uri: String): Mono<String>
}
