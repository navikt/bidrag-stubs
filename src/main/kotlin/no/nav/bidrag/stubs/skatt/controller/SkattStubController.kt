package no.nav.bidrag.stubs.skatt.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.bidrag.stubs.skatt.dto.krav.Krav
import no.nav.bidrag.stubs.skatt.service.SkattStubService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
    description = "Stub for mottak av krav fra Bidrag-Regnskap som skal sendes til Skatteetaten.")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "202",
        description = "Alle konteringer ble overført OK. Returnerer en BatchUid."
      ),
      ApiResponse(
        responseCode = "400",
        description = "En av konteringene gikk ikke gjennom validering. Liste over feilede konteringer returneres."
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
  @Tag(name = "ekstern")
  @ResponseBody
  fun lagreKrav(@RequestBody krav: Krav): ResponseEntity<*> {
    return skattStubService.lagreKrav(krav)
  }
}