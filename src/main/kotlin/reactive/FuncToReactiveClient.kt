package reactive

import reactor.core.publisher.Mono

class FuncToReactiveClient(private val func: (String) -> String) : Client {
    override fun get(uri: String): Mono<String> = Mono.fromCallable { func(uri) }
}