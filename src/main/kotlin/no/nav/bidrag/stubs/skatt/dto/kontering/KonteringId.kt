package no.nav.bidrag.stubs.skatt.dto.kontering

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.stubs.skatt.dto.enumer.Transaksjonskode

@Schema(name = "KonteringId", description = "Identifiserer en kontering unikt.")
data class KonteringId(

    @field:Schema(description = "Type transaksjon.", example = "B1", required = true)
    val transaksjonskode: Transaksjonskode,

    @field:Schema(
        description = "Angir hvilken periode (måned og år) konteringen gjelder.",
        format = "yyyy-mm",
        example = "2022-04",
        required = true
    )
    val periode: String,

    @field:Schema(
        description = "Unik referanse til perioden i vedtaket. " +
            "I bidragssaken kan en periode strekke over flere måneder, og samme referanse blir da benyttet for alle månedene. " +
            "Samme referanse kan ikke benyttes to ganger for samme transaksjonskode i samme måned.",
        example = "123456789",
        required = true
    )
    val delytelsesId: String
)
