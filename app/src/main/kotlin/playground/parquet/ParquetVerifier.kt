package playground.parquet

import org.apache.hadoop.fs.Path
import org.apache.parquet.avro.AvroReadSupport
import org.apache.parquet.example.data.Group
import org.apache.parquet.hadoop.ParquetReader
import org.apache.parquet.hadoop.example.GroupReadSupport
import org.apache.parquet.hadoop.util.HadoopInputFile

import java.io.IOException


class ParquetVerifier {
    private val logger = mu.KotlinLogging.logger {}
    fun verifyParquetFile(path: String) {
        val apachePath = Path(path)
        // ParquetReader.builder<HadoopInputFile>(AvroReadSupport(), apachePath).build().use { reader ->
        ParquetReader.builder(GroupReadSupport(), apachePath).build().use { reader ->
            var record: Group?
            try {
                do {
                    record = reader.read()
                } while (record != null)
                logger.info { "Parquet file seems to be valid" }
            } catch (ioe: IOException) {
                logger.warn("Error reading parquet file. It may be corrupted: ${ioe.message}")
            } catch (rte: RuntimeException) {
                logger.warn("Error reading parquet file. It may be corrupted: ${rte.message}")
            }
        }
    }
}
