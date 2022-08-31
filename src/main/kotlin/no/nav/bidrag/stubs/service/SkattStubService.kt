package no.nav.bidrag.stubs.service

import no.nav.bidrag.regnskap.model.KravRequest
import no.nav.bidrag.regnskap.model.KravResponse
import no.nav.bidrag.stubs.utils.StubUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class SkattStubService(var stubUtils: StubUtils) {
    companion object {
        const val RESOURCES_FILPLASSERING = "responser/ekstern/skatt/"
    }

    fun lagreKrav(kravRequest: KravRequest): ResponseEntity<KravResponse> {
        kravRequest.konteringer.forEach {
            if(stubUtils.hentAlleFilnavn(RESOURCES_FILPLASSERING).contains(it.delytelsesId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stubUtils.jsonToObject(RESOURCES_FILPLASSERING + it.delytelsesId, KravResponse::class.java))
            }
        }
        return ResponseEntity(HttpStatus.OK)
    }
}