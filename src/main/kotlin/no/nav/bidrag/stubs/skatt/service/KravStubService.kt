package no.nav.bidrag.stubs.skatt.service

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.bidrag.domene.enums.regnskap.Transaksjonskode
import no.nav.bidrag.domene.enums.regnskap.behandlingsstatus.Batchstatus
import no.nav.bidrag.domene.enums.regnskap.Årsakskode
import no.nav.bidrag.stubs.SECURE_LOGGER
import no.nav.bidrag.transport.regnskap.behandlingsstatus.BehandlingsstatusResponse
import no.nav.bidrag.transport.regnskap.behandlingsstatus.Feilmelding
import no.nav.bidrag.transport.regnskap.krav.KravResponse
import no.nav.bidrag.transport.regnskap.krav.Kravliste
import no.nav.bidrag.transport.regnskap.vedlikeholdsmodus.Vedlikeholdsmodus
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class KravStubService(private val objectMapper: ObjectMapper) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(KravStubService::class.java)

        @Volatile
        private var vedlikeholdsmodusState: Vedlikeholdsmodus = Vedlikeholdsmodus(false, Årsakskode.PAALOEP_BEHANDLET, "Default state.")

        @Volatile
        private var feilePåKravState: Boolean = false

        @Volatile
        private var feilePåBehandlingsstatusState: Boolean = false
    }

    fun lagreKrav(kravliste: Kravliste): ResponseEntity<Any> {
        if (vedlikeholdsmodusState.aktiv) {
            LOGGER.info(
                "Vedlikeholdsmodus er på! Krav blir derfor ikke lagret. " +
                    "\nÅrsakskode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}",
            )
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        }

        if (feilePåKravState) {
            return ResponseEntity.badRequest()
                .body("Feil ved overføring av krav via STUB. Dette er en forventet feil og kan endres ved å kalle bidrag-stubs endepunkt.")
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
                            feilkode = "[XX]",
                            fagsystemId = "[1234567]",
                            transaksjonskode = Transaksjonskode.B1,
                            delytelseId = null,
                            periode = null,
                            feilmelding =
                                "FagsystemId: [1234567]; [XX] step [XX123]. Batch: [$batchUid - ]. " +
                                    "Autentisering mot Maskinporten feilet: Url: https://test.maskinporten.no/token. " +
                                    "invalid_grant Invalid assertion. Client authentication failed. Invalid JWT signature. " +
                                    "(trace_id: Bidrag-Stub_TestData)",
                        ),
                    ),
                    Batchstatus.Failed,
                    batchUid = batchUid,
                    1,
                    0,
                    1,
                ),
            )
        } else {
            ResponseEntity.ok(BehandlingsstatusResponse(emptyList(), Batchstatus.Done, batchUid, 1, 0, 1))
        }
    }

    fun oppdaterVedlikeholdsmodus(vedlikeholdsmodus: Vedlikeholdsmodus): ResponseEntity<Any> {
        vedlikeholdsmodusState = vedlikeholdsmodus
        return ResponseEntity.ok().build()
    }

    fun liveness(): ResponseEntity<Any> {
        return when (vedlikeholdsmodusState.aktiv) {
            true ->
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    "Årsakode: ${vedlikeholdsmodusState.aarsakKode.name}, kommentar: ${vedlikeholdsmodusState.kommentar}",
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
