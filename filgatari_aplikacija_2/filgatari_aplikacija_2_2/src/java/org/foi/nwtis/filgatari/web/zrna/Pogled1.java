package org.foi.nwtis.filgatari.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.annotation.PostConstruct;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.pomocno.Stranicenje;
import org.foi.nwtis.filgatari.rest.klijenti.KorisniciRESTKlijent;
import org.foi.nwtis.filgatari.rest.klijenti.KorisniciRESTKlijentKorime;
import org.foi.nwtis.filgatari.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;

/**
 * Zrno za prvi pogled.
 *
 * @author filip
 */
@Named(value = "pogled1")
@SessionScoped
public class Pogled1 implements Serializable {

    private String korisnickoIme;
    private String lozinka;

    private int stranicenjeBrojZapisa;

    private String imeReg;
    private String prezimeReg;
    private String emailReg;
    private String lozinkaReg;
    private String korImeReg;

    private String korImePri;
    private String lozinkaPri;

    private String imeAzu;
    private String prezimeAzu;
    private String poruka;

    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private List<Korisnik> listaKorisnikaZaPrikaz = new ArrayList<>();
    private Stranicenje stranicenje;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled1() {
    }

    /**
     * Dohvaća inicijalni popis korisnika. Dohvaća pojedine vrijednosti iz
     * konfiguracije.
     */
    @PostConstruct
    public void init() {
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        stranicenjeBrojZapisa = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenjePogled1"));

        preuzmiSveKorisnikeREST();
        popuniFormuZaAzuriranje();
    }

    /**
     * Preuzima sve korisnike putem REST servisa iz treće aplikacije.
     */
    public void preuzmiSveKorisnikeREST() {
        KorisniciRESTKlijent klijent = new KorisniciRESTKlijent();
        String odgovorJsonTekst = klijent.getJson(String.class, korisnickoIme, lozinka);
        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);

        listaKorisnika = procitaniJsonOdgovor.vratiNizKorisnika();
        stranicenje = new Stranicenje(listaKorisnika, stranicenjeBrojZapisa);
        listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
    }

    /**
     * Popunjava ime i prezime korisnike
     */
    private void popuniFormuZaAzuriranje() {
        for (Korisnik korisnik : listaKorisnika) {
            if (korisnik.getKor_ime().equals(korisnickoIme)) {
                imeAzu = korisnik.getIme();
                prezimeAzu = korisnik.getPrezime();
                return;
            }
        }
    }

    /**
     * Dodaje novog korisnika putem REST servisa iz treće aplikacije.
     */
    public void registracijaKorisnikaREST() {
        if (imeReg.isEmpty() || prezimeReg.isEmpty()) {
            poruka = "Niste popunili ime i/ili prezime za registraciju korisnika.";
            return;
        }

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail_adresa(emailReg);
        korisnik.setIme(imeReg);
        korisnik.setPrezime(prezimeReg);
        korisnik.setKor_ime(korImeReg);
        korisnik.setLozinka(lozinkaReg);
        KorisnikREST klijent = new KorisnikREST();
        klijent.dodajKorisnika(korisnik, lozinka, korisnickoIme);
        String odgovorJsonTekst = klijent.dodajKorisnika(korisnik, lozinka, korisnickoIme);
        poruka = odgovorJsonTekst;

        preuzmiSveKorisnikeREST();
    }

    public void prijavaKorisnikaREST() {
        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korImePri);
        String odgovorJsonTekst = klijent.getJsonId(String.class, korImePri, lozinkaPri);
        poruka = odgovorJsonTekst;

        preuzmiSveKorisnikeREST();
    }

    /**
     * Ažurira korisnika putem REST servisa iz treće aplikacije.
     */
    public void azuriranjeKorisnikaREST() {
        if (imeAzu.isEmpty() || prezimeAzu.isEmpty()) {
            poruka = "Niste popunili ime i/ili prezime za azuriranje korisnika.";
            return;
        }

        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korisnickoIme);
        String odgovorJsonTekst = klijent.azurirajKorisnika(String.class, lozinka, imeAzu, prezimeAzu);
        poruka = odgovorJsonTekst;

        preuzmiSveKorisnikeREST();
    }

    /**
     *
     */
    public void sljedecaStranica() {
        if (stranicenje.sljedeciZapisi() == true) {
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }
    }

    /**
     *
     */
    public void prethodnaStranica() {
        if (stranicenje.prethodniZapisi() == true) {
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }
    }

    /**
     *
     * @return
     */
    public String getImeReg() {
        return imeReg;
    }

    /**
     *
     * @param imeReg
     */
    public void setImeReg(String imeReg) {
        this.imeReg = imeReg;
    }

    /**
     *
     * @return
     */
    public String getPrezimeReg() {
        return prezimeReg;
    }

    /**
     *
     * @param prezimeReg
     */
    public void setPrezimeReg(String prezimeReg) {
        this.prezimeReg = prezimeReg;
    }

    /**
     *
     * @return
     */
    public String getImeAzu() {
        return imeAzu;
    }

    /**
     *
     * @param imeAzu
     */
    public void setImeAzu(String imeAzu) {
        this.imeAzu = imeAzu;
    }

    /**
     *
     * @return
     */
    public String getPrezimeAzu() {
        return prezimeAzu;
    }

    /**
     *
     * @param prezimeAzu
     */
    public void setPrezimeAzu(String prezimeAzu) {
        this.prezimeAzu = prezimeAzu;
    }

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public List<Korisnik> getListaKorisnikaZaPrikaz() {
        return listaKorisnikaZaPrikaz;
    }

    /**
     *
     * @param listaKorisnikaZaPrikaz
     */
    public void setListaKorisnikaZaPrikaz(List<Korisnik> listaKorisnikaZaPrikaz) {
        this.listaKorisnikaZaPrikaz = listaKorisnikaZaPrikaz;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getKorImePri() {
        return korImePri;
    }

    public void setKorImePri(String korImePri) {
        this.korImePri = korImePri;
    }

    public String getLozinkaPri() {
        return lozinkaPri;
    }

    public void setLozinkaPri(String lozinkaPri) {
        this.lozinkaPri = lozinkaPri;
    }

    public String getEmailReg() {
        return emailReg;
    }

    public void setEmailReg(String emailReg) {
        this.emailReg = emailReg;
    }

    public String getLozinkaReg() {
        return lozinkaReg;
    }

    public void setLozinkaReg(String lozinkaReg) {
        this.lozinkaReg = lozinkaReg;
    }

    public String getKorImeReg() {
        return korImeReg;
    }

    public void setKorImeReg(String korImeReg) {
        this.korImeReg = korImeReg;
    }

    static class KorisnikREST {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/filgatari_aplikacija_3_2/webresources";

        public KorisnikREST() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("korisnik");
        }

        public String dodajKorisnika(Object requestEntity, String lozinka, String korisnik) throws ClientErrorException {
            return webTarget.request().header("lozinka", lozinka).header("korisnik", korisnik).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String getJsonId(String id) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/auth", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String getJson() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String azurirajKorisnika(Object requestEntity, String lozinka, String korisnik) throws ClientErrorException {
            return webTarget.request().put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public void close() {
            client.close();
        }
    }
}
