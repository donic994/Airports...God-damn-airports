/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.ws.serveri;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.filgatari.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.filgatari.pomocno.Dnevnik;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author filip
 */
@WebService(serviceName = "AerodromiWS")
public class AerodromiWS {

        @Resource
    private WebServiceContext context;

      /**
     * Poziva upit nad bazom podataka.Uzima zadnji podatak iz baze
 podataka.
     *
     * @param aerodrom identifikator aerodroma za kojeg se dohvaćaju podaci
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return objekt AvionLeti
     */
    public AvionLeti dajZadnjiPodatakAerodroma(String aerodrom, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajZadnjiPodatakAerodroma");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }

        AvionLeti avion = null;

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            avion = bpo.aerodromZadnjiAvion(aerodrom);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);

        return avion;
    }
    
     /**
     * Poziva upit nad bazom podataka. Dohvaća zadnjih n aviona za
     * aerodorm.
     *
     * @param icao identifikator aerodroma za kojeg se dohvaćaju podaci
     * @param n broj podataka
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu aviona iz rezultata upita
     */
    public List<AvionLeti> dajZadnjihNAvionaSAerodroma(String icao, int n, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajZadnjihNAvionaSAerodroma");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }
        
        List<AvionLeti> avioni = null;
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            avioni = bpo.aerodromNAviona(icao, n);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return avioni;
    }
    /**
     * Poziva upit nad bazom podataka. Dohvaća sve avione za aerodrom
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator aerodroma za kojeg se dohvaćaju podaci
     * @param odString početak vremenskog intervala
     * @param doString kraj vremenskog intervala
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu aviona iz rezultata upita
     */
    public List<AvionLeti> dajAvioneSAerodromaZaPeriodTimestamp(String id, String odString, String doString, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajAvioneSAerodromaZaPeriodTimestamp");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }

        List<AvionLeti> sviAvioni = null;

        try {
            Date parsedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsedDate = dateFormat.parse(odString);
            Timestamp odTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            parsedDate = dateFormat.parse(doString);
            Timestamp doTimestamp = new java.sql.Timestamp(parsedDate.getTime());

            long odVrijeme = odTimestamp.getTime();
            long doVrijeme = doTimestamp.getTime();

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviAvioni = bpo.avionLetoviSOdabranogAerodroma(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);

        return sviAvioni;
    }
    
    /**
     * Poziva upit nad bazom podataka. Dohvaća sve avione 
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator AVIONA za kojeg se dohvaćaju podaci
     * @param odString početak vremenskog intervala
     * @param doString kraj vremenskog intervala
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu aviona iz rezultata upita
     */
    public List<AvionLeti> dajAvioneZaPeriodTimestamp(String id, String odString, String doString, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajAvioneZaPeriodTimestamp");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }

        List<AvionLeti> sviAvioni = null;

        try {
            Date parsedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsedDate = dateFormat.parse(odString);
            Timestamp odTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            parsedDate = dateFormat.parse(doString);
            Timestamp doTimestamp = new java.sql.Timestamp(parsedDate.getTime());

            long odVrijeme = odTimestamp.getTime();
            long doVrijeme = doTimestamp.getTime();

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviAvioni = bpo.avionLetoviUIntervalu(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);

        return sviAvioni;
    }
    
        /**
     * Poziva upit nad bazom podataka. Dohvaća sve avione 
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator AVIONA za kojeg se dohvaćaju podaci
     * @param odString početak vremenskog intervala
     * @param doString kraj vremenskog intervala
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu aviona iz rezultata upita
     */
    public List<String> dajAvioneZaPeriodTimestampString(String id, String odString, String doString, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajAvioneZaPeriodTimestampString");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }

        List<String> naziviAerodroma = null;

        try {
            Date parsedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsedDate = dateFormat.parse(odString);
            Timestamp odTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            parsedDate = dateFormat.parse(doString);
            Timestamp doTimestamp = new java.sql.Timestamp(parsedDate.getTime());

            long odVrijeme = odTimestamp.getTime();
            long doVrijeme = doTimestamp.getTime();

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            naziviAerodroma = bpo.avionLetoviUIntervaluNazivAerodroma(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);

        return naziviAerodroma;
    }
    
    /**
     * Poziva upit nad bazom podataka kako bi se dohvatio naziv
     * aerodroma. Za dohvaćeni naziv aerodroma dohvaća trenutne
     * meteopodatke sa web servisa.
     *
     * @param id identifikator aerodroma za kojeg se dohvaćaju podaci
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return objekt meteopodataka
     */
    public MeteoPodaci dajVazeceMeteoPodatke(String id, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        String url = vratiTrenutnuAdresuZahtjeva("dajVazeceMeteoPodatke");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            return null;
        }

        Aerodrom aerodrom = new Aerodrom();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            aerodrom = bpo.aerodromSelectByIcao(id);
            bpo.zatvoriVezu();            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        MeteoPodaci meteo = null;

        try {
            meteo = PomocnaKlasa.dohvatiMeteoPodatke(aerodrom.getLokacija().getLatitude(), aerodrom.getLokacija().getLongitude());
            dnevnik.postaviUspjesanStatus();
        } catch (Exception ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
        
        return meteo;
    }
    
     /**
     * Poziva upit nad bazom podataka.Dodaje korisnika i vraća odgovor o uspjehu
     *
     * @param korisnik objekt korisnika
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return true ako je dodan, inaće false
     */
    public boolean dodajKorisnika(Korisnik korisnik, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();    
        boolean odgovor = false;
        String url = vratiTrenutnuAdresuZahtjeva("dodajKorisnika");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            odgovor = true;
        }
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            odgovor = bpo.korisniciInsert(korisnik);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return odgovor;
    }
    
     /**
     * Poziva upit nad bazom podataka.Azurira korisnika i vraća odgovor o uspjehu
     *
     * @param korisnik objekt korisnika
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return true ako je dodan, inaće false
     */
    public boolean azurirajKorisnika(Korisnik korisnik, String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();    
        boolean odgovor = false;
        String url = vratiTrenutnuAdresuZahtjeva("azurirajKorisnika");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
            odgovor = true;
        }
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            odgovor = bpo.korisniciUpdateKorisnik(korisnik);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return odgovor;
    }
    
    /**
     * Poziva upit nad bazom podataka.Azurira korisnika i vraća odgovor o uspjehu
     *
     * @param korisnik objekt korisnika
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return true ako je dodan, inaće false
     */
    public List<Korisnik> dajSveKorisnike(String korisnickoIme, String lozinka) {
        Dnevnik dnevnik = new Dnevnik();    
        String url = vratiTrenutnuAdresuZahtjeva("dajSveKorisnike");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
        }
        
        List<Korisnik> sviKorisnici = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviKorisnici = bpo.korisniciSelectSviKorisnici();
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return sviKorisnici;
    }
    
        /*_   _                                       _             
 | \ | |   __ _   _ __    _ __    ___    __| |  _ __     ___ 
 |  \| |  / _` | | '_ \  | '__|  / _ \  / _` | | '_ \   / _ \
 | |\  | | (_| | | |_) | | |    |  __/ | (_| | | | | | |  __/
 |_| \_|  \__,_| | .__/  |_|     \___|  \__,_| |_| |_|  \___|
                 |_|                                             
     */
    
    
    public double dajUdaljenostAerodroma(String aerodrom1, String aerodrom2, String korisnickoIme, String lozinka){
        
        Dnevnik dnevnik = new Dnevnik();    
        String url = vratiTrenutnuAdresuZahtjeva("dajUdaljenostAerodroma");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
        }
        
        double udaljenost = 0d;
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            udaljenost = bpo.udaljenostAerodroma(aerodrom1, aerodrom2);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return udaljenost;
    }
    
     public List<Aerodrom> dajAerodromeUdaljeneOdAerodroma(String aerodrom1, double odKm, double doKm, String korisnickoIme, String lozinka){
        
        Dnevnik dnevnik = new Dnevnik();    
        String url = vratiTrenutnuAdresuZahtjeva("dajUdaljenostAerodroma");
        
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);
        }
        
        List<Aerodrom> aerodromi = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            aerodromi = bpo.aerodromiUdaljeniOdAerodroma(aerodrom1, odKm, doKm);
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AerodromiWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, url);        

        return aerodromi;
    }
    
    
    /**
     * Vraća url adresu zahtjeva.
     * @param akcija naziv akcije
     * @return url adresa zahtjeva
     */
    private String vratiTrenutnuAdresuZahtjeva(String akcija){
        HttpServletRequest hsr = (HttpServletRequest) context.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        String adresaZahtjeva = hsr.getRequestURL().toString() + "/" + akcija;
        
        return adresaZahtjeva;
    }
    
}
