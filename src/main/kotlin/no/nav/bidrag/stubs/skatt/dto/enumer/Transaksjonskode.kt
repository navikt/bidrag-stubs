package no.nav.bidrag.stubs.skatt.dto.enumer

enum class Transaksjonskode(beskrivelse: String, korreksjonskode: Boolean) {
  A1("Bidragsforskudd", false),
  A3("Bidragsforskudd", true),
  B1("Underholdsbidrag (m/u tilleggsbidrag)", false),
  B3("Underholdsbidrag (m/u tilleggsbidrag)", true),
  D1("18årsbidrag", false),
  D3("18årsbidrag", true),
  E1("Bidrag til særlige utgifter (særtilskudd)", false),
  E3("Bidrag til særlige utgifter (særtilskudd)", true),
  F1("Ektefellebidrag", false),
  F3("Ektefellebidrag", true),
  G1("Gebyr", false),
  G3("Gebyr", true),
  H1("Tilbakekreving", false),
  H3("Tilbakekreving", true),
  I1("Motregning", false),
  K1("Ettergivelse", false),
  K2("Direkte oppgjør (innbetalt beløp)", false),
  K3("Tilbakekreving ettergivelse", false),
}