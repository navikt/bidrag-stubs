package no.nav.bidrag.stubs.skatt.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Feilsituasjon", description = "Et kall har ført til feilmelding")
data class Kravfeil(

    @field:Schema(
        description = "En beskrivelse av feilen som har oppstått. " +
            "Feilmeldingen er ment å være forståelig for et menneske ved manuell gjennomgang.",
        example = "Tolkning feilet i Elin."
    )
    val message: String
)
