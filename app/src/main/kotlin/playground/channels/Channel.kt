package playground.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel {
    private val logger = KotlinLogging.logger {}
    private val channel = Channel<Int>()
    private val consumers = mutableListOf<Job>()
    private val someNumbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    suspend fun startProducer(scope: CoroutineScope) {
        val producer = scope.launch {
            for (x in someNumbers) {
                logger.info { "Sending $x" }
                channel.send(x)
            }
            channel.close()
        }
    }

    fun startConsumer(scope: CoroutineScope) {
        repeat(3) {
            val consumer = scope.launch {
                for (y in channel) {
                    logger.info { "Received $y" }
                    delay(y*500L)
                    logger.info { "DONE with $y" }
                    logger.info { "----------------" }
                }
            }
            consumers.add(consumer)
        }
    }

    fun run() {
        runBlocking {
            startProducer(this)
            startConsumer(this)
            consumers.forEach { it.join() }
        }
    }
}