package no.nav.bidrag.stubs.skatt.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.bidrag.stubs.skatt.dto.Feilsituasjon
import no.nav.bidrag.stubs.skatt.dto.Vedlikeholdsmodus
import no.nav.bidrag.stubs.skatt.dto.kontering.Konteringsfeil
import no.nav.bidrag.stubs.skatt.dto.krav.Krav
import no.nav.bidrag.stubs.skatt.dto.krav.KravResponse
import no.nav.bidrag.stubs.skatt.service.SkattStubService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
        description = "Alle konteringer ble overført OK. Returnerer en BatchUid.",
        content = [Content(
          mediaType = "application/json",
          array = ArraySchema(schema = Schema(implementation = KravResponse::class))
        )]
      ),
      ApiResponse(
        responseCode = "400",
        description = "En av konteringene gikk ikke gjennom validering. Liste over feilede konteringer returneres.",
        content = [Content(
          mediaType = "application/json",
          array = ArraySchema(schema = Schema(implementation = Feilsituasjon::class))
        )]
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
  fun lagreKrav(@RequestBody krav: Krav): ResponseEntity<Any> {
    return skattStubService.lagreKrav(krav)
  }

  @PostMapping("api/vedlikeholdsmodus")
  @Operation(
    description = "Stub for slå av og på vedlikeholdsmodus.")
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
    description = "Stub for å sjekke status på vedlikeholdsmodus.")
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
        content = [Content(
          mediaType = "application/json",
          array = ArraySchema(schema = Schema(implementation = Konteringsfeil::class))
        )]
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
        content = [Content(
          mediaType = "application/json",
          array = ArraySchema(schema = Schema(implementation = Feilsituasjon::class))
        )]
      )
    ]
  )
  @Tag(name = "Sjekk status på vedlikeholdsmodus")
  @ResponseBody
  fun liveness(): ResponseEntity<Any> {
    return skattStubService.liveness()
  }
}