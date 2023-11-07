package no.nav.bidrag.stubs.skatt.dto.reskontro

data class Input(
    val aksjonskode: Int,
    val bidragssaksnummer: Long? = null,
    val fodselsOrgnr: String? = null,
    val transaksjonsId: Long? = null,
    val datoFom: String? = null,
    val datoTom: String? = null,
    val maxAntallTransaksjoner: Int? = null,
    val fodselsnrGjelder: String? = null,
    val fodselsnrNy: String? = null
)
