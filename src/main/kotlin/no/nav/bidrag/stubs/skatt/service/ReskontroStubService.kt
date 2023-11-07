package no.nav.bidrag.stubs.skatt.service

import no.nav.bidrag.stubs.skatt.dto.reskontro.Bidragssak
import no.nav.bidrag.stubs.skatt.dto.reskontro.GjeldendeBetalingsordning
import no.nav.bidrag.stubs.skatt.dto.reskontro.Input
import no.nav.bidrag.stubs.skatt.dto.reskontro.Output
import no.nav.bidrag.stubs.skatt.dto.reskontro.Retur
import no.nav.bidrag.stubs.skatt.dto.reskontro.Skyldner
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ReskontroStubService {

    fun hentBidragssak(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 1) {
            if (input.bidragssaksnummer == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer")))
            }

        } else if (input.aksjonskode == 2) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }
        }
        return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-2, "Ugyldig aksjonskode"))) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentTransaksjoner(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 3) {
            if (input.bidragssaksnummer == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer/datoFom/datoTom/maxAntallTransaksjoner")))
            }
        }
        if (input.aksjonskode == 4) {
            if (input.fodselsOrgnr == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr/datoFom/datoTom/maxAntallTransaksjoner")))
            }

        }
        if (input.aksjonskode == 5) {
            if (input.transaksjonsId == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler transaksjonsId/datoFom/datoTom/maxAntallTransaksjoner")))
            }

        }

        return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-2, "Ugyldig aksjonskode"))) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentInnkrevingssak(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 6) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }

        }

        return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-2, "Ugyldig aksjonskode"))) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun endreRm(input: Input): ResponseEntity<Output> {
        if( input.aksjonskode == 8) {
            if (input.bidragssaksnummer == null || input.fodselsnrGjelder == null || input.fodselsnrNy == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer/fodselsnrGjelder/fodselsnrNy")))
            }
        }
        return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-2, "Ugyldig aksjonskode"))) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    private fun opprettOutputMedFeilkode(input: Input, retur: Retur) = Output(
        innParametre = input,
        skyldner = Skyldner(
            sumLopendeBidrag = 0.0,
            innbetBelopUfordelt = 0.0,
            gjeldIlagtGebyr = 0.0
        ),
        bidragssak = Bidragssak(
            bidragssaksnummer = 1,
            bmGjeldFastsettelsesgebyr = 0.0,
            bmGjeldRest = 0.0,
            bpGjeldFastsettelsesgebyr = 0.0
        ),
        gjeldendeBetalingsordning = GjeldendeBetalingsordning(),
        retur = retur
    )
}