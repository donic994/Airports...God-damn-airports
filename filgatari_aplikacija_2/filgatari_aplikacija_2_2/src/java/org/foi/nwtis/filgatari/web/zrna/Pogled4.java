package org.foi.nwtis.filgatari.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.rest.klijenti.AerodromRESTKlijent;
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
@Named(value = "pogled4")
@SessionScoped
public class Pogled4 implements Serializable {

    private String odVrijeme;
    private String doVrijeme;
    private String odabraniAerodromPol;

    private String odabraniAerodromDol;

    private String poruka;
    private List<Aerodrom> listaAerodroma = new ArrayList<>();
    private List<Izbornik> popisAerodrom = new ArrayList<>();

    private String statusAerodroma;
    private List<AvionLeti> listaAviona = new ArrayList<>();
    private List<AvionLeti> listaLetova = new ArrayList<>();

    MeteoPodaci meteoPodaci;

    private String korisnickoIme;
    private String lozinka;

    /**
     *
     */
    public Pogled4() {
    }

    /**
     * Dohvaća popis svih aerodroma i postavlja ostale inicijalne vrijednosti.
     */
    @PostConstruct
    public void init() {
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        preuzmiSveAerodeomeREST();
        odabraniAerodromPol = popisAerodrom.get(0).getVrijednost();
    }

    /**
     * Preuzima sve aerodrome putem REST servisa iz prve aplikacije.
     */
    public void preuzmiSveAerodeomeREST() {
        AerodromRESTKlijent klijent = new AerodromRESTKlijent();
        String odgovorJsonTekst = klijent.getJson(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaAerodroma = procitaniJsonOdgovor.vratiNizAerodroma();

        popisAerodrom = new ArrayList<>();

        for (Aerodrom a : listaAerodroma) {
            Izbornik i = new Izbornik(a.getNaziv(), a.getIcao());
            popisAerodrom.add(i);

        }
    }

    public String preuzmiUdaljenost() {
        double udaljenost = 0;
        if (odabraniAerodromDol.isEmpty() || odabraniAerodromPol.isEmpty()) {
            poruka = "Treba odabrati polazni i odredišni aerodrom";
        } else {
            udaljenost = WSKlijent.dajUdaljenostAerodroma(odabraniAerodromPol, odabraniAerodromDol, korisnickoIme, lozinka);

            poruka = "Udaljenost između aerodroma je " + udaljenost + " kilometara";
        }
        return "";
    }

    public String getOdVrijeme() {
        return odVrijeme;
    }

    public void setOdVrijeme(String odVrijeme) {
        this.odVrijeme = odVrijeme;
    }

    public String getDoVrijeme() {
        return doVrijeme;
    }

    public void setDoVrijeme(String doVrijeme) {
        this.doVrijeme = doVrijeme;
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

    public String getOdabraniAerodromPol() {
        return odabraniAerodromPol;
    }

    public void setOdabraniAerodromPol(String odabraniAerodromPol) {
        this.odabraniAerodromPol = odabraniAerodromPol;
    }

    public String getOdabraniAerodromDol() {
        return odabraniAerodromDol;
    }

    public void setOdabraniAerodromDol(String odabraniAerodromDol) {
        this.odabraniAerodromDol = odabraniAerodromDol;
    }

    public List<AvionLeti> getListaLetova() {
        return listaLetova;
    }

    public void setListaLetova(List<AvionLeti> listaLetova) {
        this.listaLetova = listaLetova;
    }
}
