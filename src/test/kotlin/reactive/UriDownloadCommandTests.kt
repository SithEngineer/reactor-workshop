package reactive

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test
import reactor.test.StepVerifier
import java.time.Duration

class UriDownloadCommandTests {

    private lateinit var command: UriDownloadCommand

    @BeforeEach
    fun setup() {
        val echoClient = FuncToReactiveClient { uri -> uri }
        command = UriDownloadCommand(echoClient)
    }

    @Test
    fun `GIVEN a base uri WHEN executing command THEN it provides the success result only once, without delay`() {
        val arguments = arrayOf("--source", "www.google.com")

        command.load(arguments)
            .test()
            .expectNext("www.google.com")
            .verifyComplete()
    }

    @Test
    fun `GIVEN a base uri AND a count of 2 WHEN executing command THEN it provides the success result twice, without delay`() {
        val arguments = arrayOf(
            "--source", "www.google.com",
            "--count", "2"
        )

        command.load(arguments)
            .test()
            .expectNext("www.google.com")
            .expectNext("www.google.com")
            .verifyComplete()
    }

    @Test
    fun `GIVEN a base uri AND a count of 3 AND delay 5 WHEN executing command THEN it provides the success result 3 times, with 5 seconds delay between each result`() {
        val arguments = arrayOf(
            "--source", "www.google.com",
            "--count", "3",
            "--delay", "5"
        )

        StepVerifier.withVirtualTime { command.load(arguments) }
            .thenAwait(Duration.ofSeconds(5))
            .expectNext("www.google.com")
            .thenAwait(Duration.ofSeconds(5))
            .expectNext("www.google.com")
            .thenAwait(Duration.ofSeconds(5))
            .expectNext("www.google.com")
            .verifyComplete()
    }
}