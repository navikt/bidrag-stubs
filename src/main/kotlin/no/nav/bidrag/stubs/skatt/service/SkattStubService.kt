package no.nav.bidrag.stubs.skatt.service

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.bidrag.stubs.SECURE_LOGGER
import no.nav.bidrag.stubs.skatt.dto.Batchstatus
import no.nav.bidrag.stubs.skatt.dto.BehandlingsstatusResponse
import no.nav.bidrag.stubs.skatt.dto.Feilmelding
import no.nav.bidrag.stubs.skatt.dto.KravResponse
import no.nav.bidrag.stubs.skatt.dto.Kravfeil
import no.nav.bidrag.stubs.skatt.dto.Kravliste
import no.nav.bidrag.stubs.skatt.dto.Vedlikeholdsmodus
import no.nav.bidrag.stubs.skatt.dto.enumer.ÅrsakKode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SkattStubService(private val objectMapper: ObjectMapper) {

    companion object {

        private val LOGGER = LoggerFactory.getLogger(SkattStubService::class.java)

        @Volatile
        private var vedlikeholdsmodusState: Vedlikeholdsmodus = Vedlikeholdsmodus(false, ÅrsakKode.PAALOEP_BEHANDLET, "Default state.")

        @Volatile
        private var feilePåKravState: Boolean = false

        @Volatile
        private var feilePåBehandlingsstatusState: Boolean = false
    }

    fun lagreKrav(kravliste: Kravliste): ResponseEntity<Any> {
        if (vedlikeholdsmodusState.aktiv) {
            LOGGER.info(
                "Vedlikeholdsmodus er på! Krav blir derfor ikke lagret. " +
                    "\nÅrsakskode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}"
            )
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }

        if (feilePåKravState) {
            return ResponseEntity.badRequest()
                .body(Kravfeil("Feil ved overføring av krav via STUB. Dette er en forventet feil og kan endres ved å kalle bidrag-stubs endepunkt."))
        }

        SECURE_LOGGER.info(objectMapper.writeValueAsString(kravliste))
        return ResponseEntity.accepted().body(KravResponse(opprettBatchUid()))
    }

    fun hentBehandlingsstatus(batchUid: String): ResponseEntity<BehandlingsstatusResponse> {
        return if (feilePåBehandlingsstatusState) {
            ResponseEntity.ok(
                BehandlingsstatusResponse(
                    listOf(
                        Feilmelding(
                            "FagsystemId: [1234567]; [XX] step [XX123]. Batch: [$batchUid - ]. " +
                                "Autentisering mot Maskinporten feilet: Url: https://test.maskinporten.no/token. invalid_grant Invalid assertion. Client authentication failed. Invalid JWT signature. " +
                                "(trace_id: Bidrag-Stub_TestData)"
                        )
                    ),
                    Batchstatus.Failed,
                    1,
                    0,
                    1
                )
            )
        } else {
            ResponseEntity.ok(BehandlingsstatusResponse(emptyList(), Batchstatus.Done, 1, 0, 1))
        }
    }

    fun oppdaterVedlikeholdsmodus(vedlikeholdsmodus: Vedlikeholdsmodus): ResponseEntity<Any> {
        vedlikeholdsmodusState = vedlikeholdsmodus
        return ResponseEntity.ok().build()
    }

    fun liveness(): ResponseEntity<Any> {
        return when (vedlikeholdsmodusState.aktiv) {
            true -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Kravfeil(
                    "Årsakode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}"
                )
            )

            false -> ResponseEntity.ok().build()
        }
    }

    fun oppdaterFeilPåKrav(skalFeilePåInnsendingAvKrav: Boolean): Boolean {
        feilePåKravState = skalFeilePåInnsendingAvKrav
        return feilePåKravState
    }

    fun oppdatertFeilPåBehandlingsstatus(skalFeilePåKallMotBehandlingsstatus: Boolean): Boolean {
        feilePåBehandlingsstatusState = skalFeilePåKallMotBehandlingsstatus
        return feilePåBehandlingsstatusState
    }

    private fun opprettBatchUid(): String {
        return UUID.randomUUID().toString()
    }
}
