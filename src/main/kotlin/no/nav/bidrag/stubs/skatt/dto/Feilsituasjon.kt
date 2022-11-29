package no.nav.bidrag.stubs.skatt.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Feilsituasjon", description = "Et kall har ført til feilmelding")
data class Feilsituasjon(

  @field:Schema(
    description = "En kode som beskriver feilsituasjonen. Koden skal være lik for like situasjoner.",
    example = "NOE_GIKK_GALT_EKSEMPEL",
    required = true)
  val feilkode: String,
  @field:Schema(
    description = "En tekst som beskriver denne konkrete feilsituasjonen. Beskrivelsen bør inneholde nok informasjon til at problemet kan løses.",
    example = "Noe gikk galt.",
    required = true)
  val feilmelding: String
)
