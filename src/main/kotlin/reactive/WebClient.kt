package reactive

import io.netty.handler.ssl.SslContextBuilder
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

class WebClient : Client {
    override fun get(uri: String): Mono<String> {
        return HttpClient.create()
            .secure { spec -> spec.sslContext(SslContextBuilder.forClient()) }
            .followRedirect(true)
            .compress(true)
            .get()
            .uri(uri)
            .responseContent()
            .aggregate()
            .asString()
    }
}