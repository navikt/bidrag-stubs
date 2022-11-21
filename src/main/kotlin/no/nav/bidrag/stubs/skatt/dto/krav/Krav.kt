package no.nav.bidrag.stubs.skatt.dto.krav

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.stubs.skatt.dto.kontering.Kontering

@Schema(name = "Krav", description = "Et krav består av en liste med konteringer.")
data class Krav(
  val konteringer: List<Kontering>
)
