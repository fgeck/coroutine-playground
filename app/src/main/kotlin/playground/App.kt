package playground

import mu.KotlinLogging
import playground.channels.Channel

class App {
    fun run() {
        val logger = KotlinLogging.logger {}

        logger.info { "------- Start Channel Playground -------" }
        val channel = Channel()
        channel.run()
        logger.info { "------- Finished Channel Playground -------" }
    }
}

fun main() {
    App().run()
}

