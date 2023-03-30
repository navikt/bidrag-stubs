package no.nav.bidrag.stubs.skatt.dto.krav

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.stubs.skatt.dto.kontering.Konteringsfeil

@Schema(name = "Kravfeil", description = "Lister feil i et krav.")
data class Kravfeil(
    val konteringsfeil: List<Konteringsfeil>
)
