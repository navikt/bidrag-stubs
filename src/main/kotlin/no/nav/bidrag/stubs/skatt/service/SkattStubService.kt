package no.nav.bidrag.stubs.skatt.service

import no.nav.bidrag.stubs.SECURE_LOGGER
import no.nav.bidrag.stubs.skatt.dto.Feilsituasjon
import no.nav.bidrag.stubs.skatt.dto.krav.Krav
import no.nav.bidrag.stubs.skatt.dto.krav.KravResponse
import no.nav.bidrag.stubs.utils.StubUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SkattStubService(var stubUtils: StubUtils) {
  companion object {
    const val RESOURCES_FILPLASSERING = "responser/ekstern/skatt/"
  }

  fun lagreKrav(krav: Krav): ResponseEntity<*> {
    krav.konteringer.forEach {
      if (stubUtils.finnesFil(RESOURCES_FILPLASSERING, it.delytelsesId)) {
        val feilsituasjon: Feilsituasjon? =
          stubUtils.jsonfilTilObjekt(RESOURCES_FILPLASSERING, it.delytelsesId, Feilsituasjon::class.java)
        return ResponseEntity.badRequest().body(feilsituasjon)
      }
    }
    SECURE_LOGGER.info(stubUtils.objektTilJson(krav))
    return ResponseEntity.accepted().body(KravResponse(opprettBatchUid()))
  }

  fun opprettBatchUid(): String {
    return UUID.randomUUID().toString()
  }
}