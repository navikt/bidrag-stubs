package no.nav.bidrag.stubs.skatt.dto.krav

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "KravBatch", description = "Er sett med krav.")
data class KravBatch(
    val krav: List<Krav>
)
