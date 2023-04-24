package no.nav.bidrag.stubs.skatt.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "KravResponse", description = "Svar med referanse til videre behandling av konteringene.")
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class KravResponse(
    val BatchUid: String
)
