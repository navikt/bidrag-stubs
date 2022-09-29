package no.nav.bidrag.stubs.service

import no.nav.bidrag.regnskap.model.KravRequest
import no.nav.bidrag.regnskap.model.KravResponse
import no.nav.bidrag.regnskap.model.KravVellykket
import no.nav.bidrag.stubs.utils.StubUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SkattStubService(var stubUtils: StubUtils) {
    companion object {
        const val RESOURCES_FILPLASSERING = "responser/ekstern/skatt/"
    }

    fun lagreKrav(kravRequest: KravRequest): ResponseEntity<*> {
        kravRequest.konteringer.forEach {
            if(stubUtils.finnesFil(RESOURCES_FILPLASSERING, it.delytelsesId)) {
                val kravResponse: KravResponse? = stubUtils.jsonToObject(RESOURCES_FILPLASSERING, it.delytelsesId, KravResponse::class.java)
                return ResponseEntity.badRequest().body(kravResponse)
            }
        }
        return ResponseEntity.accepted().body(KravVellykket(opprettBatchUid()))
    }

    fun opprettBatchUid(): String {
        return UUID.randomUUID().toString()
    }
}