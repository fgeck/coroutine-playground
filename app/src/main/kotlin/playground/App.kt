package playground

import mu.KotlinLogging
import playground.channels.MyChannel
import playground.parquet.ParquetVerifier

class App {
    fun runChannels() {
        val logger = KotlinLogging.logger {}

        logger.info { "------- Start Channel Playground -------" }
        val channel = MyChannel()
        channel.run()
        logger.info { "------- Finished Channel Playground -------" }
    }

    fun runParquetVerifier() {
        val logger = KotlinLogging.logger {}
        logger.info { "------- Start Parquet Verifier -------" }
        val verifier = ParquetVerifier()
        logger.info { "------- Verify valid parquetfile -------" }
        verifier.verifyParquetFile("/Users/d068994/Desktop/parquetfiles/healthy.parquet")
        logger.info { "------- Verify corrupted parquetfile -------" }
        verifier.verifyParquetFile("/Users/d068994/Desktop/parquetfiles/corrupted.parquet")
        logger.info { "------- Finished Parquet Verifier -------" }
    }
}

fun main() {
    App().runParquetVerifier()
}

