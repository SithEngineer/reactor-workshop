package reactive

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux

class Counter {

    fun count(): Flux<Int> = (0..10).toFlux()

    fun countFiltered(): Flux<Int> =
        (0..10_000).toFlux()
            .parallel(8)
            .runOn(Schedulers.newParallel("Worker", 8))
            .filter {
                println("I am filtering in thread ${Thread.currentThread().name}")
                if (Thread.currentThread().id == 20L) {
                    throw Exception("Invalid Thread ID")
                }
                it % 2 == 0 //only pairs
            }
            .sequential()
            //.onBackpressureError()
            //.onErrorContinue { error, _ -> Mono.just("Caught error: ${error.message}") }
            .sort()

    fun generator(fromState: Int, toState: Int): Flux<Int> =
        Flux.generate(
            { fromState },
            { state: Int, sink: SynchronousSink<Int> ->
                sink.next(state)
                if (state == toState) {
                    sink.complete()
                }
                state + 1
            }
        )

}