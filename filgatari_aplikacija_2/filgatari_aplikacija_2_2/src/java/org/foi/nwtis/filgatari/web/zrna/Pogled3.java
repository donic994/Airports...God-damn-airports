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
 * Zrno za pogled 3.
 *
 * @author filip
 */
@Named(value = "pogled3")
@SessionScoped
public class Pogled3 implements Serializable {

    private String odVrijeme;
    private String doVrijeme;
    private String odabraniAerodrom;
    private String odabraniAvion;

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
    public Pogled3() {
    }

    /**
     * Dohvaća popis svih aerodroma i postavlja ostale inicijalne vrijednosti.
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

    /**
     * Ispisuje poruku stanja aerodroma iz liste.
     */
    public void dajStatusAerodroma() {
        Aerodrom a = dajOdabraniAerodrom();
        statusAerodroma = a.getStatus();

        poruka = "Dohvaćen je status.";
    }

    /**
     * Poziva operaciju za dohvat aviona aerodoma putem SOAP servisa iz prve
     * aplikacije.
     */
    public void preuzmiAvioneAerodroma() {
        if (provjeriUnosVremena(odVrijeme) || provjeriUnosVremena(doVrijeme)) {
            listaAviona = WSKlijent.dajAvioneSAerodromaZaPeriodTimestamp(odabraniAerodrom, promjeniFormatVremena(odVrijeme), promjeniFormatVremena(doVrijeme), korisnickoIme, lozinka);

            if (listaAviona == null) {
                poruka = "Nije moguće dohvatiti avione";
                return;
            }

            poruka = "Dohvaceno je " + listaAviona.size() + " aviona u intervalu";
        } else {
            poruka = "Pogrešni format datuma. Ispravni format=dd-MM-yyyy HH:mm:ss";
        }

    }
    
     /**
     * Poziva operaciju za dohvat letova aviona putem SOAP servisa iz prve
     * aplikacije.
     * @param icao
     */
    public void preuzmiLetoveAviona(String icao) {
        if (provjeriUnosVremena(odVrijeme) || provjeriUnosVremena(doVrijeme)) {
            listaLetova = WSKlijent.dajAvioneZaPeriodTimestamp_1(icao, promjeniFormatVremena(odVrijeme), promjeniFormatVremena(doVrijeme), korisnickoIme, lozinka);

            if (listaLetova == null) {
                poruka = "Nije moguće dohvatiti letova aviona";
                return;
            }

            poruka = "Dohvaceno je " + listaLetova.size() + " letova u intervalu";
        } else {
            poruka = "Pogrešni format datuma. Ispravni format=dd-MM-yyyy HH:mm:ss";
        }

    }

    /**
     * Metoda prima unos vremena iz sucelja te provjerava je li sintaksa unosa
     * vremena ispravna. Ako je, vraća true, inace false.
     *
     * @param unos vrijednost iz sucelja doVremena
     * @return
     */
    public boolean provjeriUnosVremena(String unos) {
        String sintaksa = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-\\d\\d\\d\\d (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-5][0-9])";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(unos);
        boolean status = m.matches();
        return status;
    }
    
    public String promjeniFormatVremena(String unos){
        String[] vrijeme = unos.split("-");
        String[] godinaSat = vrijeme[2].split(" ");
        return godinaSat[0]+"-"+vrijeme[1]+"-"+vrijeme[0]+" "+godinaSat[1];
    }

    /**
     * Dohvaća objekt aerodrom prema odabiru iz liste na sučelju.
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
     *
     * @param epoch datum u Integer
     * @return String
     */
    public String pretvoriEpochUDatum(Integer epoch) {
        Long vrijeme = new Long(epoch);
        Date date = new Date(vrijeme * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(date);
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
        public String getOdabraniAvion() {
        return odabraniAvion;
    }

    public void setOdabraniAvion(String odabraniAvion) {
        this.odabraniAvion = odabraniAvion;
    }
        public List<AvionLeti> getListaLetova() {
        return listaLetova;
    }

    public void setListaLetova(List<AvionLeti> listaLetova) {
        this.listaLetova = listaLetova;
    }
}
