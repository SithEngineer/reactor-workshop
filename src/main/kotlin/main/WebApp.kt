package main

import reactive.*
import reactor.netty.http.server.HttpServer
import reactor.core.publisher.Mono
import java.time.Duration

fun main() {
    val port = 9999
    val host = "127.0.0.1"

    val message = MessageGenerator()
    val counter = Counter()
    val troubleMaker = TroubleMaker()
    val eventData = EventData()

    HttpServer.create()
        .compress(true)
        .port(port)
        .host(host)
        .route { routes ->
            routes.get("/hello") { _, response -> response.sendString(message.hello()) }

            routes.get("/slow") { _, response -> response.sendString(message.slowHello()) }

            routes.get("/count") { _, response ->
                response.sendString(
                    counter.count()
                        .map { "$it " }
                )
            }

            routes.get("/count-filtered") { _, response ->
                response.sendString(
                    counter.countFiltered()
                        .map { "$it " }
                )
            }

            routes.get("/generator/{from}/{to}") { request, response ->
                val from = request.param("from")?.toInt() ?: 0
                val to = request.param("to")?.toInt() ?: 10
                response.sendString(
                    counter.generator(from, to)
                        .map { "$it " }
                )
            }

            routes.get("/ups") { _, response -> response.sendString(troubleMaker.messAround()) }

            routes.get("/controlled") { _, response ->
                response.sendString(
                    troubleMaker.messAround().onErrorResume { error -> Mono.just("${error.message}") }
                )
            }

            routes.get("/sample-data") { _, response ->
                response.sendString(
                    eventData
                        .coldProducer()
                        .take(Duration.ofSeconds(5L))
                        .map { "$it\n" }
                        .onErrorResume { error -> Mono.just("${error.message}") }
                )
            }

        }
        .bindUntilJavaShutdown(Duration.ofSeconds(5)) {
            println("HTTP server available at $host:$port")
        }
}