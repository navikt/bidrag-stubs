package no.nav.bidrag.template.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.nio.file.Paths


@Component
class StubUtils(var resourceLoader: ResourceLoader) {

  fun hentAlleFilnavn(endepunkt: String): List<String> {
    val filnavnListe = mutableListOf<String>()

    File(hentPathResponserForEndepunkt(endepunkt)).walk()
      .filter { item -> item.toString().endsWith(".json") }.forEach {
        filnavnListe.add(it.nameWithoutExtension)
      }
    return filnavnListe
  }

  fun <T> jsonToObject(path: String, kClass: Class<*>?): T? {
    val objectMapper = jacksonObjectMapper()

    val returKlasse: T
    val javaType: JavaType = objectMapper.typeFactory.constructType(kClass)
    returKlasse = try {
      objectMapper.readValue(hentJsonString(path), javaType)
    } catch (e: IOException) {
      throw IllegalStateException(e.message, e)
    }
    return returKlasse
  }

  private fun hentPathResponserForEndepunkt(endepunkt: String) =
    Paths.get("").toAbsolutePath().toString() + "/src/main/resources/" + endepunkt

  private fun hentJsonString(path: String): String {
    return resourceLoader.getResource("classpath:$path.json").file.readText(charset = Charsets.UTF_8)
  }
}