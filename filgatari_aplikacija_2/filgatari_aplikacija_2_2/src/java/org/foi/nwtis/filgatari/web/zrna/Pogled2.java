package org.foi.nwtis.filgatari.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.foi.nwtis.filgatari.pomocno.Dnevnik;
import org.foi.nwtis.filgatari.pomocno.JsonGraditelj;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.rest.klijenti.AerodromRESTKlijent;
import org.foi.nwtis.filgatari.rest.klijenti.AerodromRESTKlijentId;
import org.foi.nwtis.filgatari.rest.klijenti.AerodromiRESTKlijentIdAviona;
import org.foi.nwtis.filgatari.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.web.podaci.Izbornik;
import org.foi.nwtis.filgatari.ws.klijenti.AvionLeti;
import org.foi.nwtis.filgatari.ws.klijenti.MeteoPodaci;
import org.foi.nwtis.filgatari.ws.klijenti.WSKlijent;



/**
 * Zrno za pogled 2.
 *
 * @author filip
 */
@Named(value = "pogled2")
@SessionScoped
public class Pogled2 implements Serializable {


    private String icao = "";
    private String odabraniAerodrom;
    private String poruka;
    private List<Aerodrom> listaAerodroma = new ArrayList<>();
    private List<Izbornik> popisAerodrom = new ArrayList<>();

    private String statusAerodroma;
    private List<AvionLeti> listaAviona = new ArrayList<>();
    MeteoPodaci meteoPodaci;

    private String korisnickoIme;
    private String lozinka;

    /**
     *
     */
    public Pogled2() {
    }

    /**
     * Dohvaća popis svih aerodroma i postavlja ostale inicijalne
     * vrijednosti.
     */
    @PostConstruct
    public void init() {
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        preuzmiSveAerodeomeREST();
        odabraniAerodrom = popisAerodrom.get(0).getVrijednost();
    }

    /**
     * Preuzima sve aerodrome putem REST servisa iz prve aplikacije.
     */
    public void preuzmiSveAerodeomeREST() {
        //Dnevnik dnevnik = new Dnevnik();
        AerodromRESTKlijent klijent = new AerodromRESTKlijent();
        String odgovorJsonTekst = klijent.getJson(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaAerodroma = procitaniJsonOdgovor.vratiNizAerodroma();

        popisAerodrom = new ArrayList<>();

        for (Aerodrom a : listaAerodroma) {
                Izbornik i = new Izbornik(a.getNaziv(),a.getIcao());
                popisAerodrom.add(i);
            
        }
      // dnevnik.postaviUspjesanStatus();
       //dnevnik.zavrsiISpremiDnevnik(korisnickoIme, "preuzmiSveAerodrome");
    }

    /**
     * Ispisuje poruku stanja aerodroma iz liste.
     */
    public void dajStatusAerodroma() {
        Aerodrom a = dajOdabraniAerodrom();
        statusAerodroma = a.getStatus();

        poruka = "Dohvaćen je status." + statusAerodroma;
    }

    /**
     * Poziva operaciju za dodavanje novog aerodroma putem REST servisa iz
     * prve aplikacije.
     */
    public void dodajAerodromREST() {
        if (icao.isEmpty()) {
            poruka = "Niste popunili sve podatke za unos aerodroma.";
            return;
        }

        String aerodromPodaci = JsonGraditelj.napraviJsonZaDodajAerodrom(icao);
        AerodromRESTKlijent klijent = new AerodromRESTKlijent();
        String odgovorJsonTekst = klijent.postJson(aerodromPodaci, String.class, korisnickoIme, lozinka);

        preuzmiSveAerodeomeREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za brisanje aerodroma putem REST servisa iz prve
     * aplikacije.
     */
    public void obrisiAerodromREST() {
        AerodromRESTKlijentId klijent = new AerodromRESTKlijentId(odabraniAerodrom);
        String odgovorJsonTekst = klijent.deleteJson(String.class, korisnickoIme, lozinka);

        preuzmiSveAerodeomeREST();
        odabraniAerodrom = popisAerodrom.get(0).getVrijednost();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za ažuriranje aerodroma putem REST servisa iz prve
     * aplikacije. Mijenja status u AKTIVAN.
     */
    public void aktivirajAerodromREST() {
        Aerodrom a = dajOdabraniAerodrom();
        a.setStatus("AKTIVAN");
        String aerodromPodaci = JsonGraditelj.napraviJsonZaAzurirajAerodrom(a);

        AerodromRESTKlijentId klijent = new AerodromRESTKlijentId(odabraniAerodrom);
        String odgovorJsonTekst = klijent.putJson(aerodromPodaci, String.class, korisnickoIme, lozinka);

        preuzmiSveAerodeomeREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za ažuriranje aerodroma putem REST servisa iz prve
     * aplikacije. Mijenja status u PASIVAN.
     */
    public void blokirajAerodromREST() {
        Aerodrom a = dajOdabraniAerodrom();
        a.setStatus("PASIVAN");
        String aerodromPodaci = JsonGraditelj.napraviJsonZaAzurirajAerodrom(a);

        AerodromRESTKlijentId klijent = new AerodromRESTKlijentId(odabraniAerodrom);
        String odgovorJsonTekst = klijent.putJson(aerodromPodaci, String.class, korisnickoIme, lozinka);

        preuzmiSveAerodeomeREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za dohvat aviona parkirališta putem REST servisa iz prve
     * aplikacije.
     */
    public void preuzmiAvioneAerodromaREST() {

        AerodromiRESTKlijentIdAviona klijent = new AerodromiRESTKlijentIdAviona(odabraniAerodrom);
        String odgovorJsonTekst = klijent.getJsonAviona(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaAviona = procitaniJsonOdgovor.vratiNizAviona();

        if (listaAviona.isEmpty()) {
            poruka = "Nije dohvacen nijedan avion za odabrani aerodrom.";
            return;
        }

        poruka = "Dohvaceno je " + listaAviona.size() + " aviona.";
    }

    /**
     * Poziva operaciju za dohvat vazecih meteopodataka aerodroma putem SOAP
     * servisa iz prve aplikacije.
     */
    public void dohvatiVazeceMeteoSOAP() {
        meteoPodaci = WSKlijent.dajVazeceMeteoPodatke(odabraniAerodrom, korisnickoIme, lozinka);

        if (meteoPodaci == null) {
            poruka = "Nije moguće dohvatiti vazece meteopodatke.";
            return;
        }

        poruka = "Dohvaceni su vazeci meteopodaci.";
    }

    /**
     * Dohvaća objekt aerodroma prema odabiru iz liste na sučelju.
     *
     * @return
     */
    private Aerodrom dajOdabraniAerodrom() {
        Aerodrom aerodrom = null;
        for (Aerodrom a : listaAerodroma) {
            if (a.getIcao().equals(odabraniAerodrom)) {
                aerodrom = a;
            }
        }

        return aerodrom;
    }
    
    /**
     * pretvara datum iz Epoch u String
     * @param epoch datum u Integer
     * @return String
     */
    public String pretvoriEpochUDatum(Integer epoch){
        Long vrijeme= new Long(epoch);
        Date date = new Date(vrijeme * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(date);
    }    
    
    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getOdabraniAerodrom() {
        return odabraniAerodrom;
    }

    public void setOdabraniAerodrom(String odabraniAerodrom) {
        this.odabraniAerodrom = odabraniAerodrom;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public List<Aerodrom> getListaAerodroma() {
        return listaAerodroma;
    }

    public void setListaAerodroma(List<Aerodrom> listaAerodroma) {
        this.listaAerodroma = listaAerodroma;
    }

    public List<Izbornik> getPopisAerodrom() {
        return popisAerodrom;
    }

    public void setPopisAerodrom(List<Izbornik> popisAerodrom) {
        this.popisAerodrom = popisAerodrom;
    }

    public String getStatusAerodroma() {
        return statusAerodroma;
    }

    public void setStatusAerodroma(String statusAerodroma) {
        this.statusAerodroma = statusAerodroma;
    }

    public List<AvionLeti> getListaAviona() {
        return listaAviona;
    }

    public void setListaAviona(List<AvionLeti> listaAviona) {
        this.listaAviona = listaAviona;
    }

    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}