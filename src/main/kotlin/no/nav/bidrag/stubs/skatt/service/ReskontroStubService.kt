package no.nav.bidrag.stubs.skatt.service

import no.nav.bidrag.commons.util.PersonidentGenerator
import no.nav.bidrag.stubs.skatt.dto.reskontro.Aktivitet
import no.nav.bidrag.stubs.skatt.dto.reskontro.BarnISak
import no.nav.bidrag.stubs.skatt.dto.reskontro.Bidragssak
import no.nav.bidrag.stubs.skatt.dto.reskontro.GjeldendeBetalingsordning
import no.nav.bidrag.stubs.skatt.dto.reskontro.Input
import no.nav.bidrag.stubs.skatt.dto.reskontro.NyBetalingsordning
import no.nav.bidrag.stubs.skatt.dto.reskontro.Output
import no.nav.bidrag.stubs.skatt.dto.reskontro.Retur
import no.nav.bidrag.stubs.skatt.dto.reskontro.Skyldner
import no.nav.bidrag.stubs.skatt.dto.reskontro.Transaksjon
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
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
                    bidragssak =
                        Bidragssak(
                            bidragssaksnummer = input.bidragssaksnummer,
                            bmGjeldFastsettelsesgebyr = Random.Default.nextInt(0, 10000).toDouble(),
                            bmGjeldRest = Random.Default.nextInt(0, 10000).toDouble(),
                            bpGjeldFastsettelsesgebyr = Random.Default.nextInt(0, 10000).toDouble(),
                            perBarnISak =
                                listOf(
                                    BarnISak(
                                        fodselsnummer = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2001, 1, 1)),
                                        restGjeldOffentlig = Random.Default.nextInt(0, 10000).toDouble(),
                                        restGjeldPrivat = Random.Default.nextInt(0, 10000).toDouble(),
                                        sumIkkeUtbetalt = Random.Default.nextInt(0, 10000).toDouble(),
                                        sumForskuddUtbetalt = Random.Default.nextInt(0, 10000).toDouble(),
                                        periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).toString(),
                                        periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).toString(),
                                        restGjeldPrivatAndel = 0.0,
                                        sumInnbetaltAndel = 0.0,
                                        sumForskuddUtbetaltAndel = 0.0,
                                        stoppUtbetaling = if ((1..2).random() > 1) "J" else "N",
                                    ),
                                ),
                        ),
                    retur = Retur(0),
                ),
            )
        } else if (input.aksjonskode == 2) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    skyldner =
                        Skyldner(
                            fodselsOrgnr = input.fodselsOrgnr,
                            sumLopendeBidrag = 0.0,
                            innbetBelopUfordelt = Random.Default.nextInt(0, 10000).toDouble(),
                            gjeldIlagtGebyr = Random.Default.nextInt(0, 10000).toDouble(),
                        ),
                    bidragssak =
                        Bidragssak(
                            bidragssaksnummer = Random.Default.nextLong(),
                            bmGjeldFastsettelsesgebyr = Random.Default.nextInt(0, 10000).toDouble(),
                            bmGjeldRest = Random.Default.nextInt(0, 10000).toDouble(),
                            bpGjeldFastsettelsesgebyr = Random.Default.nextInt(0, 10000).toDouble(),
                            perBarnISak =
                                listOf(
                                    BarnISak(
                                        fodselsnummer = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2001, 1, 2)),
                                        restGjeldOffentlig = Random.Default.nextInt(0, 10000).toDouble(),
                                        restGjeldPrivat = Random.Default.nextInt(0, 10000).toDouble(),
                                        sumIkkeUtbetalt = 0.0,
                                        sumForskuddUtbetalt = Random.Default.nextInt(0, 10000).toDouble(),
                                        restGjeldPrivatAndel = Random.Default.nextInt(0, 10000).toDouble(),
                                        sumInnbetaltAndel = Random.Default.nextInt(0, 10000).toDouble(),
                                        sumForskuddUtbetaltAndel = Random.Default.nextInt(0, 10000).toDouble(),
                                    ),
                                ),
                        ),
                    retur = Retur(0),
                ),
            )
        }
        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode"),
            ),
        ) // Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentTransaksjoner(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 3) {
            if (input.bidragssaksnummer == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(
                    opprettOutputMedFeilkode(
                        input,
                        Retur(-1, "Mangler bidragssaksnummer/datoFom/datoTom/maxAntallTransaksjoner"),
                    ),
                )
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    transaksjoner =
                        listOf(
                            Transaksjon(
                                transaksjonsId = Random.Default.nextLong(),
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = input.bidragssaksnummer,
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                            Transaksjon(
                                transaksjonsId = Random.Default.nextLong(),
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = input.bidragssaksnummer,
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                        ),
                    retur = Retur(0),
                ),
            )
        }
        if (input.aksjonskode == 4) {
            if (input.fodselsOrgnr == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(
                    opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr/datoFom/datoTom/maxAntallTransaksjoner")),
                )
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    transaksjoner =
                        listOf(
                            Transaksjon(
                                transaksjonsId = Random.Default.nextLong(),
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = input.fodselsOrgnr,
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = Random.Default.nextLong(),
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                            Transaksjon(
                                transaksjonsId = Random.Default.nextLong(),
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = input.fodselsOrgnr,
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = Random.Default.nextLong(),
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                        ),
                    retur = Retur(0),
                ),
            )
        }
        if (input.aksjonskode == 5) {
            if (input.transaksjonsId == null || input.datoFom == null || input.datoTom == null || input.maxAntallTransaksjoner == null) {
                return ResponseEntity.ok(
                    opprettOutputMedFeilkode(input, Retur(-1, "Mangler transaksjonsId/datoFom/datoTom/maxAntallTransaksjoner")),
                )
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    transaksjoner =
                        listOf(
                            Transaksjon(
                                transaksjonsId = input.transaksjonsId,
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = Random.Default.nextLong(),
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(2).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                            Transaksjon(
                                transaksjonsId = input.transaksjonsId,
                                kode = "B1",
                                beskrivelse = "Bidrag",
                                dato = LocalDateTime.now().toString(),
                                kildeFodselsOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                mottakerFodslesOrgNr = PersonidentGenerator.genererFødselsnummer(),
                                opprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                restBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutaOpprinneligBeloep = Random.Default.nextInt(0, 10000).toDouble(),
                                valutakode = "NOK",
                                bidragssaksnummer = Random.Default.nextLong(),
                                periodeSisteDatoFom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                periodeSisteDatoTom = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).toString(),
                                barnFodselsnr = PersonidentGenerator.genererFødselsnummer(LocalDate.of(2000, 1, 1)),
                                bidragsId = UUID.randomUUID().toString(),
                                soeknadsType = "Ukjent",
                            ),
                        ),
                    retur = Retur(0),
                ),
            )
        }

        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode"),
            ),
        ) // Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun hentInnkrevingssak(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 6) {
            if (input.fodselsOrgnr == null) {
                return ResponseEntity.ok(opprettOutputMedFeilkode(input, Retur(-1, "Mangler fodselsOrgnr")))
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    skyldner =
                        Skyldner(
                            fodselsOrgnr = input.fodselsOrgnr,
                            sumLopendeBidrag = Random.Default.nextInt(0, 10000).toDouble(),
                            statusInnkrevingssak = "Ukjent",
                            fakturamaate = "Vanlig giro",
                            sisteAktivitet = "Annulert innbet",
                            innbetBelopUfordelt = 0.0,
                            gjeldIlagtGebyr = 0.0,
                        ),
                    gjeldendeBetalingsordning =
                        GjeldendeBetalingsordning(
                            typeBetalingsordning = "Lønnstrekk",
                            kildeOrgnummer = "889640782",
                            kildeNavn = "NAV",
                            datoSisteGiro = LocalDateTime.now().toString(),
                            datoNesteForfall = LocalDateTime.now().plusMonths(1).toString(),
                            belop = Random.Default.nextInt(0, 10000).toDouble(),
                            datoSistEndret = LocalDateTime.now().minusMonths(6).toString(),
                            aarsakSistEndret = "MAN",
                            sumUbetalt = Random.Default.nextInt(0, 10000).toDouble(),
                        ),
                    nyBetalingsordning =
                        NyBetalingsordning(
                            datoFraOgMed = LocalDateTime.now().toString(),
                            belop = Random.Default.nextInt(0, 10000).toDouble(),
                        ),
                    innkrevingssaksHistorikk =
                        listOf(
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(6).toString(),
                            ),
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(5).toString(),
                            ),
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(4).toString(),
                            ),
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(3).toString(),
                            ),
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(2).toString(),
                            ),
                            Aktivitet(
                                beskrivelse = "OCR Innbetaling",
                                fodselsOrgNr = input.fodselsOrgnr,
                                navn = "Navnesen",
                                dato = LocalDateTime.now().minusMonths(1).toString(),
                            ),
                        ),
                ),
            )
        }

        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode"),
            ),
        ) // Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    fun endreRm(input: Input): ResponseEntity<Output> {
        if (input.aksjonskode == 8) {
            if (input.bidragssaksnummer == null || input.fodselsnrGjelder == null || input.fodselsnrNy == null) {
                return ResponseEntity.ok(
                    opprettOutputMedFeilkode(input, Retur(-1, "Mangler bidragssaksnummer/fodselsnrGjelder/fodselsnrNy")),
                )
            }

            return ResponseEntity.ok(
                Output(
                    innParametre = input,
                    retur = Retur(0),
                ),
            )
        }
        return ResponseEntity.ok(
            opprettOutputMedFeilkode(
                input,
                Retur(-2, "Ugyldig aksjonskode"),
            ),
        ) // Følger kravspec fra skatt om 200 response med feilkode i retur objektet. Ikke frivillig.
    }

    private fun opprettOutputMedFeilkode(
        input: Input,
        retur: Retur,
    ) = Output(
        innParametre = input,
        skyldner =
            Skyldner(
                sumLopendeBidrag = 0.0,
                innbetBelopUfordelt = 0.0,
                gjeldIlagtGebyr = 0.0,
            ),
        bidragssak =
            Bidragssak(
                bidragssaksnummer = 1,
                bmGjeldFastsettelsesgebyr = 0.0,
                bmGjeldRest = 0.0,
                bpGjeldFastsettelsesgebyr = 0.0,
            ),
        gjeldendeBetalingsordning = GjeldendeBetalingsordning(),
        retur = retur,
    )
}
