# Bidrag-stubs

[![continuous integration](https://github.com/navikt/bidrag-stubs/actions/workflows/ci.yaml/badge.svg)](https://github.com/navikt/bidrag-stubs/actions/workflows/ci.yaml)

## Beskrivelse

Bidrag-stubs er en applikasjon for å tilby stubber mot eksterne eller interne endepunkter. Bidrag-stubs lever kun på https://bidrag-stubs.intern.dev.nav.no og kan ikke prodsettes.


Applikasjon tilbyr stubber for følgende endepunkter:

| Endepunkt              | Beskrivelse                                                                                                                                                                                                                                           | Responser                                                       | Testdata                                   |
|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|--------------------------------------------|
| ekstern/skatt/api/krav | Endepunkt for motak av krav fra Bidrag-Regnskap som skal sendes til Skatteetaten.<br/> Stubben er konstruert slik at kall med delytelsesId som ikke finnes i testdata<br/>returnerer OK, mens delytelsesId'er som finnes returnerer en konteringfeil. | 200: Tom response body<br/>400: Liste over feilede konteringer  | delytelsesId: <br/>123456789<br/>123456780 |


## Kjøre applikasjonen lokalt

Start opp applikasjonen ved å kjøre [BidragStubsLocal.kt](src/test/kotlin/no/nav/bidrag/stubs/BidragStubsLocal.kt).
Dette starter applikasjonen med profil `local` og henter miljøvariabler fra filen [application-local.yaml](src/test/resources/application-local.yaml).

Her mangler det noen miljøvariabler som ikke bør committes til Git (Miljøvariabler for passord/secret osv).<br/>
Når du starter applikasjon må derfor følgende miljøvariabl(er) settes:
```bash
-DAZURE_APP_CLIENT_SECRET=<secret>
-DAZURE_APP_CLIENT_ID=<id>
```
Disse kan hentes ved å kjøre kan hentes ved å kjøre 
```bash
kubectl exec --tty deployment/bidrag-stubs -- printenv | grep -e AZURE_APP_CLIENT_ID -e AZURE_APP_CLIENT_SECRET
```

### Live reload
Med `spring-boot-devtools` har Spring støtte for live-reload av applikasjon. Dette betyr i praksis at Spring vil automatisk restarte applikasjonen når en fil endres. Du vil derfor slippe å restarte applikasjonen hver gang du gjør endringer. Dette er forklart i [dokumentasjonen](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-devtools.html#using-boot-devtools-restart).
For at dette skal fungere må det gjøres noe endringer i Intellij instillingene slik at Intellij automatisk re-bygger filene som er endret:

* Gå til `Preference -> Compiler -> check "Build project automatically"`
* Gå til `Preference -> Advanced settings -> check "Allow auto-make to start even if developed application is currently running"`