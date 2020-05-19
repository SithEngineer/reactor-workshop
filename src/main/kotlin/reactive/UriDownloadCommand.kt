package reactive

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.time.Duration

class UriDownloadCommand(private val client: Client) {

    fun load(arguments: Array<String>): Flux<String> {
        val argumentsFlux = arguments.toFlux()
            .switchIfEmpty(Mono.error(usageError()))
            .buffer(2)
            .map { partList ->
                val (key, value) = partList
                Pair(key, value)
            }
            .filter { pair -> pair.first.startsWith("--") }

        // <ugly_blocking_code>
        val countPair: Pair<String, String>? = argumentsFlux.filter { it.first.equals("--count", true) }.blockFirst()
        val count = countPair?.second?.toLong(10)?.minus(1L) ?: 0L

        val delayPair: Pair<String, String>? = argumentsFlux.filter { it.first.equals("--delay", true) }.blockFirst()
        val delay = delayPair?.second?.toLong(10) ?: 0L
        // </ugly_blocking_code>

        return argumentsFlux
            .repeat(count)
            .filter { it.first.equals("--source", true) }
            .delayElements(Duration.ofSeconds(delay))
            .flatMap { client.get(it.second) }
            .doOnError { it.printStackTrace() }
    }

    private fun usageError(): Exception {
        return IllegalArgumentException(
            """
            Reactive cyclic client caller
            Usage: 
                --source <source>
                --count <integer, default 0> - number of times <source> is requested
                --delay <integer, default 0> - delay in seconds between each request
            """
        )
    }
}