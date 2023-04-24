package no.nav.bidrag.stubs.skatt.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "KravResponse", description = "Svar med referanse til videre behandling av konteringene.")
data class KravResponse(
    val BatchUid: String
)
