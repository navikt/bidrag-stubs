package no.nav.bidrag.stubs.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.stream.Collectors


@Component
class StubUtils() {

  companion object {
    val objectMapper = jacksonObjectMapper()
  }

  fun <T> jsonfilTilObjekt(path: String, filnavn: String, kClass: Class<*>?): T? {

    val returKlasse: T
    val javaType: JavaType = objectMapper.typeFactory.constructType(kClass)
    returKlasse = try {
      objectMapper.readValue(hentClassPathResourceSomJsonString(path, filnavn), javaType)
    } catch (e: IOException) {
      throw IllegalStateException(e.message, e)
    }
    return returKlasse
  }

  fun hentClassPathResourceSomJsonString(path: String, filnavn: String): String {
    javaClass.getResourceAsStream("/$path$filnavn.json")!!.use { inputStream ->
      BufferedReader(InputStreamReader(inputStream)).use { reader ->
        return reader.lines().collect(Collectors.joining(System.lineSeparator()))
      }
    }
  }

  fun finnesFil(path: String, filnavn: String): Boolean {
    javaClass.getResource("/$path$filnavn.json")?.let { return true }
    return false
  }
}