package no.nav.bidrag.template.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.bidrag.regnskap.model.KravRequest
import no.nav.bidrag.regnskap.model.KravResponse
import no.nav.bidrag.template.service.SkattStubService
import no.nav.security.token.support.core.api.Protected
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("ekstern/skatt")
@Protected
class SkattStubController(val skattStubService: SkattStubService
) {

    @PostMapping("/api/krav")
    @Operation(
        description = "Stub for mottak av krav fra Bidrag-Regnskap som skal sendes til Skatteetaten.",
        security = [SecurityRequirement(name = "bearer-key")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200",
                description = "Alle konteringer ble oppdatert OK. Tom response body.",
                content = [Content()]),
            ApiResponse(responseCode = "400",
                description = "En av konteringene gikk ikke gjennom validering. Liste over feilede konteringer returneres."),
            ApiResponse(responseCode = "401",
                description = "Sikkerhetstoken er ikke gyldig",
                content = [Content()]),
            ApiResponse(
                responseCode = "403",
                description = "Sikkerhetstoken er ikke gyldig, eller det er ikke gitt adgang til kode 6 og 7 (nav-ansatt)",
                content = [Content()]
            ),
        ]
    )
    @Tag(name = "ekstern")
    @ResponseBody
    fun lagreKrav (@RequestBody kravRequest: KravRequest): ResponseEntity<KravResponse> {
        return skattStubService.lagreKrav(kravRequest)
    }

}