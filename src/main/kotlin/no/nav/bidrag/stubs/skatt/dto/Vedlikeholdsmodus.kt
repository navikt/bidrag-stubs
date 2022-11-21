package no.nav.bidrag.stubs.skatt.dto

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.stubs.skatt.dto.enumer.ÅrsakKode

@Schema(name = "Vedlikeholdsmodus", description = "Kontroll av vedlikeholdsmodus.")
data class Vedlikeholdsmodus(

  @field:Schema(
    description = "Angir om vedlikeholdsmodus skal skrus på eller ikke.",
    example = "true",
    defaultValue = "true"
  )
  val aktiv: Boolean,

  @field:Schema(
    description = "Forhåndsavklart kode som beskriver årsaken til vedlikeholdsmodus.",
    example = "PAALOP_GENERERES",
    required = true
  )
  val aarsakKode: ÅrsakKode,

  @field:Schema(
    description = "Påløp for 2022-12 genereres hos NAV.",
    example = "true",
    required = true
  )
  val kommentar: String,
)