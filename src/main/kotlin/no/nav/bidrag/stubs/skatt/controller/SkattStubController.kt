package no.nav.bidrag.stubs.skatt.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.bidrag.stubs.skatt.dto.BehandlingsstatusResponse
import no.nav.bidrag.stubs.skatt.dto.Konteringsfeil
import no.nav.bidrag.stubs.skatt.dto.KravResponse
import no.nav.bidrag.stubs.skatt.dto.Kravfeil
import no.nav.bidrag.stubs.skatt.dto.Kravliste
import no.nav.bidrag.stubs.skatt.dto.OppdatertStatus
import no.nav.bidrag.stubs.skatt.dto.Vedlikeholdsmodus
import no.nav.bidrag.stubs.skatt.service.SkattStubService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("ekstern/skatt")
@ProtectedWithClaims(issuer = "maskinporten", claimMap = ["scope=nav:bidrag/v1/bidragskrav"])
class SkattStubController(
    val skattStubService: SkattStubService
) {

    @PostMapping("/api/krav")
    @Operation(
        description = "Stub for mottak av krav fra Bidrag-Regnskap som skal sendes til Skatteetaten."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "202",
                description = "Alle konteringer ble overført OK. Returnerer en BatchUid.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = KravResponse::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "En av konteringene gikk ikke gjennom validering. Liste over feilede konteringer returneres.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Kravfeil::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Sikkerhetstoken er ikke gyldig",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Sikkerhetstoken er ikke gyldig, eller det er ikke gitt adgang til kode 6 og 7 (nav-ansatt)",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Ukjent feil.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "503",
                description = "Påløpsmodusen er på.",
                content = [Content()]
            )
        ]
    )
    @Tag(name = "Send krav")
    @ResponseBody
    fun lagreKrav(@RequestBody kravliste: Kravliste): ResponseEntity<Any> {
        return skattStubService.lagreKrav(kravliste)
    }

    @PutMapping("/api/feilPaKrav")
    @Operation(
        description = "Endre om innsending av krav mot stub skal feile eller ikke."
    )
    @Tag(name = "Endre om innsending av krav skal feile")
    fun endreFeilPåKrav(@RequestParam(required = true) skalFeilePåInnsendingAvKrav: Boolean): ResponseEntity<OppdatertStatus> {
        return ResponseEntity.ok(
            OppdatertStatus(
                "Feil ved oversending av krav slått ${
                    if (skattStubService.oppdaterFeilPåKrav(
                            skalFeilePåInnsendingAvKrav
                        )
                    ) "PÅ" else "AV"
                }"
            )
        )
    }

    @PutMapping("/api/behandlingsstatus")
    @Operation(
        description = "Endre om kall på behandlingsstatus med batch-uid skal feile eller ikke."
    )
    @Tag(name = "Endre om kall på behandlingsstatus skal feile")
    fun endreBehandlingsstatus(@RequestParam(required = true) skalFeilePåKallMotBehandlingsstatus: Boolean): ResponseEntity<OppdatertStatus> {
        return ResponseEntity.ok(
            OppdatertStatus(
                "Feil ved kall mot behandlingsstatus slått ${
                    if (skattStubService.oppdatertFeilPåBehandlingsstatus(skalFeilePåKallMotBehandlingsstatus)) "PÅ" else "AV"
                }"
            )
        )
    }

    @PostMapping("api/vedlikeholdsmodus")
    @Operation(
        description = "Stub for slå av og på vedlikeholdsmodus."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Oppdateringen av vedlikeholdsmodus er OK.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Sikkerhetstoken er ikke gyldig",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Sikkerhetstoken er ikke gyldig, eller det er ikke gitt adgang til kode 6 og 7 (nav-ansatt)",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Ukjent feil.",
                content = [Content()]
            )
        ]
    )
    @Tag(name = "Skru av eller på vedlikeholdsmodus")
    @ResponseBody
    fun oppdaterVedlikeholdsmodus(@RequestBody vedlikeholdsmodus: Vedlikeholdsmodus): ResponseEntity<Any> {
        return skattStubService.oppdaterVedlikeholdsmodus(vedlikeholdsmodus)
    }

    @GetMapping("api/liveness")
    @Operation(
        description = "Stub for å sjekke status på vedlikeholdsmodus."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Vedlikeholdsmodus er ikke på.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dersom én av konteringene ikke går gjennom validering forkastes alle konteringene i kravet og en liste over konteringer som har feilet returneres, sammen med informasjon om hva som er feil.\n" +
                        "\n" +
                        "Det er ingen garanti for at konteringer som ikke kommer med på listen over feilede konteringer er feilfrie.\n " +
                        "NB: Dette er ikke implementert i stub.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Konteringsfeil::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Sikkerhetstoken er ikke gyldig",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Sikkerhetstoken er ikke gyldig, eller det er ikke gitt adgang til kode 6 og 7 (nav-ansatt)",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Ukjent feil.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "503",
                description = "Påløpsmodus er på.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Kravfeil::class))
                    )
                ]
            )
        ]
    )
    @Tag(name = "Sjekk status på vedlikeholdsmodus")
    @ResponseBody
    fun liveness(): ResponseEntity<Any> {
        return skattStubService.liveness()
    }
    @GetMapping("api/krav/{batchUid}")
    @Operation(
        description = "Stub for å sjekke behandlingsstatus for batch-uid."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Behandlingsstatus for tidligere leverte konteringer.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = BehandlingsstatusResponse::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "503",
                description = "Påløpsmodus er på.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Kravfeil::class))
                    )
                ]
            )
        ]
    )
    @Tag(name = "Stub for å sjekke behandlingsstatus for batch-uid")
    @ResponseBody
    fun hentBehandlingsstatus(batchUid: String): ResponseEntity<BehandlingsstatusResponse> {
        return skattStubService.hentBehandlingsstatus(batchUid)
    }
}
