package no.nav.bidrag.stubs.skatt.service

import no.nav.bidrag.stubs.SECURE_LOGGER
import no.nav.bidrag.stubs.skatt.dto.Feilsituasjon
import no.nav.bidrag.stubs.skatt.dto.Vedlikeholdsmodus
import no.nav.bidrag.stubs.skatt.dto.enumer.ÅrsakKode
import no.nav.bidrag.stubs.skatt.dto.krav.Krav
import no.nav.bidrag.stubs.skatt.dto.krav.KravResponse
import no.nav.bidrag.stubs.utils.StubUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SkattStubService(var stubUtils: StubUtils) {
    companion object {
        const val RESOURCES_FILPLASSERING = "responser/ekstern/skatt/"
        private val LOGGER = LoggerFactory.getLogger(SkattStubService::class.java)

        @Volatile
        private var vedlikeholdsmodusState: Vedlikeholdsmodus =
            Vedlikeholdsmodus(false, ÅrsakKode.PAALOEP_BEHANDLET, "Default state.")
    }

    fun lagreKrav(krav: Krav): ResponseEntity<Any> {
        if (vedlikeholdsmodusState.aktiv) {
            LOGGER.info(
                "Vedlikeholdsmodus er på! Krav blir derfor ikke lagret. " +
                    "\nÅrsakskode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}"
            )
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
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

    fun oppdaterVedlikeholdsmodus(vedlikeholdsmodus: Vedlikeholdsmodus): ResponseEntity<Any> {
        vedlikeholdsmodusState = vedlikeholdsmodus
        return ResponseEntity.ok().build()
    }

    fun liveness(): ResponseEntity<Any> {
        return when (vedlikeholdsmodusState.aktiv) {
            true -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Feilsituasjon(
                    "VEDLIKEHOLD_MODUS",
                    "Årsakode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}"
                )
            )
            false -> ResponseEntity.ok().build()
        }
    }
}
