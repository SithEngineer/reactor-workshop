import org.junit.jupiter.api.Test
import reactive.Counter
import reactive.MessageGenerator
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.test.test

class FluxTests {

    @Test
    fun `GIVEN an empty source WHEN subscribing THEN nothing happens AND complete is called`() {
        val flux = emptyArray<Unit>().toFlux()

        flux.test()
            .verifyComplete()
    }

    @Test
    fun `GIVEN a message producer WHEN the message is retrieved THEN all message components must be present`() {
        val flux: Flux<String> = MessageGenerator().hello()

        flux.test()
            .expectNext("Hello", "World")
    }

    @Test
    fun `GIVEN a counter WHEN counting from 0 to 10 THEN the 11 elements are received`() {
        val flux: Flux<Int> = Counter().count()

        flux.test()
            .expectNext(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }
}