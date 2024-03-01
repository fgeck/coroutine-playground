package playground.channels

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel {
    private companion object {
        private const val CONSUMERS_SIZE = 5
        private const val CHANNEL_SIZE = 9
        private const val NUMBERS = 20
    }

    private val logger = KotlinLogging.logger {}
    private val channel = Channel<Int>(CHANNEL_SIZE)
    private val consumers = mutableListOf<Job>()
    private val someNumbers = (1..NUMBERS).toList()

    suspend fun startProducer(scope: CoroutineScope) {
        val producer = scope.launch {
            for (x in someNumbers) {
                logger.info { "Sending $x" }
                channel.send(x)
            }
            logger.info { "-------- Done Sending All --------" }
            channel.close()
        }
    }

    fun startConsumer(scope: CoroutineScope) {
        repeat(CONSUMERS_SIZE) {
            val consumer = scope.launch(CoroutineName("Consumer #${it + 1}")) {
                delay(500L)
                for (y in channel) {
                    logger.info { "Coroutine: ${this.coroutineContext[CoroutineName]} Received $y" }
                    delay(y*500L)
                    logger.info { "DONE with $y. Waited: ${y*500}ms" }
                    logger.info { "----------------" }
                }
            }
            consumers.add(consumer)
        }
    }

    fun run() {
        runBlocking {
            startProducer(this)
            // delay(1000L)
            startConsumer(this)
            consumers.forEach { it.join() }
        }
    }
}