package reactive

import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.Duration

class MessageGenerator {

    fun hello(): Flux<String> = arrayOf("Hello", ", " , "World").toFlux()

    fun slowHello(): Flux<String> = hello().delaySubscription(Duration.ofSeconds(10))

}