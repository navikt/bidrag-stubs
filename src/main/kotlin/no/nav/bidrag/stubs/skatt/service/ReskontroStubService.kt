package no.nav.bidrag.stubs.skatt.service

import no.nav.bidrag.commons.util.PersonidentGenerator
import no.nav.bidrag.stubs.skatt.dto.reskontro.BarnISak
import no.nav.bidrag.stubs.skatt.dto.reskontro.Bidragssak
import no.nav.bidrag.stubs.skatt.dto.reskontro.GjeldendeBetalingsordning
import no.nav.bidrag.stubs.skatt.dto.reskontro.Input
import no.nav.bidrag.stubs.skatt.dto.reskontro.Output
import no.nav.bidrag.stubs.skatt.dto.reskontro.Retur
import no.nav.bidrag.stubs.skatt.dto.reskontro.Skyldner
import no.nav.bidrag.stubs.skatt.dto.reskontro.Transaksjon
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class ReskontroStubService {

    fun hentBidragssak(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 1) {
            if (input.bidragssaksnummer == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer")))
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    bidragssak = Bidragssak(
                        bidragssaksnummer = input.bidragssaksnummer,
                        bmGjeldFastsettelsesgebyr = Random.Default.nextDouble(),
                        bmGjeldRest = Random.Default.nextDouble(),
                        bpGjeldFastsettelsesgebyr = Random.Default.nextDouble(),
                        perBarnISak = listOf(
                            BarnISak(
                                fodselsnummer = PersonidentGenerator.genererPersonnummer(LocalDate.of(1, 1, 1)),
                                restGjeldOffentlig = Random.Default.nextDouble(),
                                restGjeldPrivat = Random.Default.nextDouble(),
                                sumIkkeUtbetalt = Random.Default.nextDouble(),
                                sumForskuddUtbetalt = Random.Default.nextDouble(),
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).toString(),
                                restGjeldPrivatAndel = 0.0,
                                sumInnbetaltAndel = 0.0,
                                sumForskuddUtbetaltAndel = 0.0,
                                stoppUtbetaling = if ((1..2).random() > 1) "J" else "N"
                            )
                        )
                    )
                )
            )

        } else if (input.aksjonskode == 2) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    skyldner = Skyldner(
                        fodselsOrgnr = input.fodselsOrgnr,
                        sumLopendeBidrag = 0.0,
                        innbetBelopUfordelt = Random.Default.nextDouble(),
                        gjeldIlagtGebyr = Random.Default.nextDouble()
                    ),
                    bidragssak = Bidragssak(
                        bidragssaksnummer = Random.Default.nextLong(),
                        bmGjeldFastsettelsesgebyr = Random.Default.nextDouble(),
                        bmGjeldRest = Random.Default.nextDouble(),
                        bpGjeldFastsettelsesgebyr = Random.Default.nextDouble(),
                        perBarnISak = listOf(
                            BarnISak(
                                fodselsnummer = PersonidentGenerator.genererPersonnummer(LocalDate.of(1, 1, 2)),
                                restGjeldOffentlig = Random.Default.nextDouble(),
                                restGjeldPrivat = Random.Default.nextDouble(),
                                sumIkkeUtbetalt = 0.0,
                                sumForskuddUtbetalt = Random.Default.nextDouble(),
                                restGjeldPrivatAndel = Random.Default.nextDouble(),
                                sumInnbetaltAndel = Random.Default.nextDouble(),
                                sumForskuddUtbetaltAndel = Random.Default.nextDouble(),
                            )
                        )
                    )
                )
            )

        }
        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode")
            )
        ) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentTransaksjoner(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 3) {
            if (input.bidragssaksnummer == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(
                    opprettOutputMedFeilkode(
                        input,
                        Retur(-1, "Mangler bidragssaksnummer/datoFom/datoTom/maxAntallTransaksjoner")
                    )
                )
            }

//            return ResponseEntity.ok(
//                Output(
//                    innParametre = input,
//                    transaksjoner = listOf(
//                        Transaksjon(
//                            transaksjonsId = Random.Default.nextLong(),
//                            kode = arrayOf("A1", "B1", "")[(1..10).random()]
//                        )
//                    )
//                )
//            )
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

        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode")
            )
        ) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentInnkrevingssak(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 6) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }

        }

        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode")
            )
        ) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun endreRm(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 8) {
            if (input.bidragssaksnummer == null || input.fodselsnrGjelder == null || input.fodselsnrNy == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer/fodselsnrGjelder/fodselsnrNy")))
            }
        }
        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode")
            )
        ) //Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
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