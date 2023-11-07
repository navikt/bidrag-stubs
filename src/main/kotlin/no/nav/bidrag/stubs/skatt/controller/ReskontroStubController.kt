package no.nav.bidrag.stubs.skatt.controller

import no.nav.bidrag.stubs.skatt.dto.reskontro.Input
import no.nav.bidrag.stubs.skatt.dto.reskontro.Output
import no.nav.bidrag.stubs.skatt.service.ReskontroStubService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("ekstern/skatt")
@ProtectedWithClaims(issuer = "maskinporten", claimMap = ["scope=nav:bidrag/v1/bidragskrav"])
class ReskontroStubController(
    private val reskontroStubService: ReskontroStubService
) {

    @PostMapping("/BisysResk/bidragssak")
    fun hentBidragssak(@RequestBody input: Input): ResponseEntity<Output> {
        return reskontroStubService.hentBidragssak(input)
    }

    @PostMapping("/BisysResk/transaksjoner")
    fun hentTransaksjoner(@RequestBody input: Input): ResponseEntity<Output> {
        return reskontroStubService.hentTransaksjoner(input)
    }

    @PostMapping("/BisysResk/innkrevingssak")
    fun hentInnkrevingssak(@RequestBody input: Input): ResponseEntity<Output> {
        return reskontroStubService.hentInnkrevingssak(input)
    }

    @PatchMapping("/BisysResk/endrerm")
    fun endreRm(@RequestBody input: Input): ResponseEntity<Output> {
        return reskontroStubService.endreRm(input)
    }
}

