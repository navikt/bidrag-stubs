package no.nav.bidrag.stubs.skatt.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Krav", description = "Et krav består av en liste med konteringer.")
data class Krav(
    val konteringer: List<KravKontering>
)
