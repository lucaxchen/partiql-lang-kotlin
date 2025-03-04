import org.partiql.cli.ErrorCodeString
import org.partiql.cli.pipeline.ErrorMessageFormatter
import org.partiql.spi.catalog.Catalog
import org.partiql.spi.catalog.Session
import org.partiql.spi.catalog.Table
import org.partiql.spi.errors.PRuntimeException
import org.partiql.spi.value.Datum
import org.partiql.spi.value.DatumReader
import org.partiql.spi.value.ValueUtils
import java.io.File
import org.partiql.spi.value.io.PartiQLValueTextWriter
import org.partiql.spi.catalog.Name
import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str


import java.io.InputStream
import java.io.SequenceInputStream
import java.util.Collections;

var dir: File? = null // manually give the File object
var files: Array<File>? = null // manually give the files list
var program: Pair<String?, File?>? = null
var strict: Boolean = false
const val SHEBANG_PREFIX = "#!"

fun main() {
    val statement: String = "SELECT * FROM {'a':1,'b':2, 'c': 2};"
//    val statement: String = "SELECT t.a, b, c FROM << { 'a': 1 }, { 'a': 2 } >> AS t LET t.a * 2 AS b, t.a * 3 AS c;"
//    val statement: String = "SELECT t.a FROM << { 'a': 1 , 'a': 3 }, { 'a': 2 , 'b': 2 } >> AS t;"
//    val statement: String = "SELECT t.a FROM <<{ 'a': 1 , 'a': 3 }>> AS t;"
//    val statement: String = "SELECT * FROM [1, 2, 3];"
    val config = getPipelineConfig()
    val pipeline = when (strict){
        true -> Pipeline.strict(System.out, config)
        false -> Pipeline.default(System.out,config)
    }

    val session = session()
    val result = try {
        pipeline.execute(statement, session)
    } catch (e: Pipeline.PipelineException) {
        e.message?.let { error(it) }
        return
    }

    try {
        val writer = PartiQLValueTextWriter(System.out)
        val p = ValueUtils.newPartiQLValue(result)
        writer.append(p)
    } catch (e: PRuntimeException) {
        val msg = ErrorMessageFormatter.message(e.error)
        error(msg)
    }
}



private fun getPipelineConfig(): Pipeline.Config{
    var warningsAsErrors: Array<ErrorCodeString> = emptyArray()
    return Pipeline.Config(999,false, warningsAsErrors)
}


private fun session() = Session.builder()
    .identity(System.getProperty("user.name"))
    .namespace(emptyList())
    .catalog("default")
    .catalogs(*catalogs().toTypedArray())
    .build()

private fun catalogs(): List<Catalog>{
    val stream = stream()
    val datum = if(stream != null){
        val reader = DatumReader.ion(stream)
        val values = reader.readAll()
        when(values.size){
            0 -> Datum.nullValue()
            1 -> values.first()
            else -> Datum.bag(values)
        }
    }else{
        Datum.nullValue()
    }

    val catalog = Catalog.builder()
        .name("default")
        .define(
            Table.standard(
                name = Name.of("stdin"),
                schema = datum.type,
                datum = datum,
            )
        )
        .build()
    return listOf(catalog)
}

private fun DatumReader.readAll(): List<Datum>{
    val values = mutableListOf<Datum>()
    val next = next()
    while(next != null){
        values.add(next)
    }
    return values
}

private fun stream(): InputStream? {
    val streams: MutableList<InputStream> = mutableListOf()
    if (program?.second != null) {
        streams.add(program!!.second!!.inputStream())
    }
    if (files != null) {
        streams.addAll(files!!.map { it.inputStream() })
    }
    if (streams.isEmpty() && System.`in`.available() != 0) {
        streams.add(System.`in`)
    }
    return when (streams.size) {
        0 -> null
        1 -> streams.first()
        else -> SequenceInputStream(Collections.enumeration(streams))
    }
}


