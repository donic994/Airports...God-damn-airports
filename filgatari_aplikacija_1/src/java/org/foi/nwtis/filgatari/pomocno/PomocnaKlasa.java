/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.pomocno;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.foi.nwtis.filgatari.konfiguracije.Konfiguracija;
import org.foi.nwtis.filgatari.web.dretve.RadnaDretva;
import org.foi.nwtis.filgatari.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author filip
 */
public class PomocnaKlasa {
        private static Properties konfiguracija;
    
        /**
     *
     * @param konfiguracija
     */
    public void setKonfiguracija(Properties konfiguracija) {
        this.konfiguracija = konfiguracija;
    }
    
    
    /**
     * Provjerava da li korisnik postoji
     *
     * @param korisnickoIme
     * @param lozinka
     * @return true ako postoji, inače false
     */
    public static boolean autentificirajKorisnika(String korisnickoIme, String lozinka) {
        boolean postoji = false;
        KorisnikPodaci korisnik = new KorisnikPodaci(korisnickoIme, lozinka);

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            postoji = bpo.korisniciSelectKorisnikPostoji(korisnik);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }

        return postoji;
    }
   

    /**
     * Čita niz znakova sa konzole koje je poslao korisnik.
     *
     * @param socket
     * @return vraća pročitani niz znakova koji predstavlja komandu serveru
     */
    public static String procitajKomandu(Socket socket) {
        StringBuffer buffer = new StringBuffer();

        try {
            InputStream is = socket.getInputStream();

            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                buffer.append((char) znak);
            }
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buffer.toString();
    }

    /**
     * Ispituje znakovni niz prema zadanom regularnom izrazu
     *
     * @param komanda znakovni niz koji predstavlja komandu za server
     * @param regularniIzraz regularni izraz koji komanda mora zadovoljiti
     * @return true ako je komanda ispravna, false ako nije
     */
    public static Matcher provjeriIspravnostKomande(String komanda, String regularniIzraz) {
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);

        return m;
    }

    /**
     * Služi za slanje odgovora na konzolu korisnika sustava.
     *
     * @param odgovor tekst odgovora koji se šalje
     * @param socket
     */
    public static void posaljiOdgovor(String odgovor, Socket socket) {
        try {
            OutputStream os;
            os = socket.getOutputStream();
            os.write(odgovor.getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Dohvaća postavku konfiguracije prema nazivu vrijednosti
     *
     * @param naziv
     * @return
     */
    public static String dohvatiPostavku(String naziv) {
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");
        String vrijednost = konf.dajPostavku(naziv);

        return vrijednost;
    }
    
        /**
     * Vraca trenutnu IP adresu na kojoj se vrti aplikacija
     *
     * @return
     */
    public static String dajTrenutnuIPAdresu() {
        String ipAdresa = "";
        try {
            ipAdresa = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ipAdresa;
    }
    
     /**
     * Dohvaca meteopodatke pomocu OpenWeatherMaps servisa
     *
     * @param latitude geografska širina
     * @param longitude geografska dužina
     * @return objekt koji sadrži sve dostupne meteopodatke za lokaciju
     */
    public static MeteoPodaci dohvatiMeteoPodatke(String latitude, String longitude) {
        String apiKey = dohvatiPostavku("OpenWeatherMap.apikey");
        OWMKlijent owmk = new OWMKlijent(apiKey);
        MeteoPodaci meteo = null;
        try {
            meteo = owmk.getRealTimeWeather(latitude, longitude);
        } catch (NullPointerException ex) {
            Logger.getLogger("Nije moguće dohvatiti meteo podatke!");
        }
        return meteo;
    }
    
     /**
     * Dohvaca lokaciju pomocu LocationIQ servisa
     *
     * @param naziv naziv aerodroma za koji se trazi lokacija
     * @return objekt Lokacija
     */
    public static Lokacija dohvatiGPSLokaciju(String naziv) {
        String token = dohvatiPostavku("LocationIQ.token");
        LIQKlijent liqk = new LIQKlijent(token);
        return liqk.getGeoLocation(naziv);
    }
    
    
       /**
     * Dohvaća korisnicko ime i lozinku za SVN iz konfiguracije
     *
     * @return
     */
    public static KorisnikPodaci dohvatiKorisnickePodatkeZaSvn() {
        String korisnickoIme;
        String lozinka;
        
        korisnickoIme = dohvatiPostavku("svn.user");
        lozinka = dohvatiPostavku("svn.password");
        KorisnikPodaci korisnik = new KorisnikPodaci(korisnickoIme, lozinka);

        return korisnik;
    }
    /**
     * Zapisuje novi zapis u tablicu dnevnik
     *
     * @param dnevnik
     */
    public static void zapisiUDnevnik(Dnevnik dnevnik) {
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            bpo.dnevnikInsert(dnevnik);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

}
