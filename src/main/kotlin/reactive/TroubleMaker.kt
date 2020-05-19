package reactive

import reactor.core.publisher.Mono

class TroubleMaker {
    fun messAround(): Mono<String> = Mono.error(Exception("Some issue was found"))
}