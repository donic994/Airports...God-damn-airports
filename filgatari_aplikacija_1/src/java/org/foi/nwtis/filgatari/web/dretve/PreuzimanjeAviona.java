package org.foi.nwtis.filgatari.web.dretve;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.filgatari.konfiguracije.Konfiguracija;
import org.foi.nwtis.filgatari.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class PreuzimanjeAviona extends Thread {

    private static ServletContext sc = null;
    private BazaPodatakaOperacije bpo;

    private boolean radi = false;
    private String username;
    private String password;
    private int pocetakIntervala;
    private int krajIntervala;

    private int inicijalniPocetakIntervala;
    private int trajanjeIntervala;
    private int ciklusDretve;
    private int redniBrojCiklusa = 0;

    /**
     * javna staticna lista aerodroma
     */
    public static List<Aerodrom> myAirports = null;

    public PreuzimanjeAviona(ServletContext sc) {
        this.sc = sc;
    }

    @Override
    public void interrupt() {
        radi = false;
        try {
            bpo.zatvoriVezu();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            bpo = new BazaPodatakaOperacije(sc);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
        }

        radi = true;
        radiPosao();
    }

    private void radiPosao() {
        while (radi) {
            long pocetak = System.currentTimeMillis();
            if (ServerSustava.serverPotpunoPokrenut == false) {
                System.out.println("AVIONI | Server je POTPUNO ZAUSTAVLJEN - vise ne preuzima avione. Zavrsavam rad dretve...");
                radi = false;
                continue;
            }
            try {
                if (ServerSustava.serverAvioniAktivan == true) {
                    preuzmiAvione();
                } else {
                    System.out.println("AVIONI | Preuzimanje aviona je u statusu PASIVNO");
                }
                pocetakIntervala = krajIntervala;
                krajIntervala = pocetakIntervala + (trajanjeIntervala * 60 * 60);

                long trajanje = System.currentTimeMillis() - pocetak;
                long spavaj = ((ciklusDretve * 60 * 1000) - trajanje);
                if (spavaj < 0) {
                    spavaj = 0;
                }
                redniBrojCiklusa++;
                Thread.sleep(spavaj);
            } catch (InterruptedException ex) {
                System.out.println("AVIONI | Dretva je zaustavljena prilikom spavanja");
            } catch (SQLException ex) {
                Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Greska kod preuzimanja aviona");
            }
        }
    }

    /**
     * dohvaća departures za avione sa odredenog aerodroma
     *
     * @param aerodromi lisa aerodroma pohranjenih i MYAIRPORTS
     * @param departures lista aviona u listi aerodroma
     * @return avioni
     */
    private List<AvionLeti> dohvatiAvione(List<Aerodrom> aerodromi, List<List<AvionLeti>> departures) {
        OSKlijent osk = new OSKlijent(username, password);
        List<AvionLeti> avioni = new ArrayList<>();

        for (Aerodrom ar : aerodromi) {
            departures.add(osk.getDepartures(ar.getIcao(), pocetakIntervala, krajIntervala));
        }
        for (List<AvionLeti> listaAviona : departures) {
            for (AvionLeti avion : listaAviona) {
                avioni.add(avion);
            }
        }
        return avioni;
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        username = k.dajPostavku("OpenSkyNetwork.korisnik");
        password = k.dajPostavku("OpenSkyNetwork.lozinka");

        inicijalniPocetakIntervala = Integer.parseInt(k.dajPostavku("preuzimanje.pocetak"));
        trajanjeIntervala = Integer.parseInt(k.dajPostavku("preuzimanje.trajanje"));
        ciklusDretve = Integer.parseInt(k.dajPostavku("preuzimanje.ciklus"));
        pocetakIntervala = (int) (new Date().getTime() / 1000)
                - (inicijalniPocetakIntervala * 60 * 60);
        krajIntervala = pocetakIntervala + (trajanjeIntervala * 60 * 60);
    }

    /**
     *
     * @return
     */
    public static ServletContext getSc() {
        return sc;
    }

    /**
     *
     * @param sc
     */
    public void setSc(ServletContext sc) {
        PreuzimanjeAviona.sc = sc;
    }

    private void preuzmiAvione() throws SQLException {
        List<Aerodrom> aerodromi = bpo.aerodromiSelectSviMyairports();
        List<AvionLeti> avioni = new ArrayList<>();
        List<List<AvionLeti>> departures = new ArrayList<>();

        avioni = dohvatiAvione(aerodromi, departures);
        bpo.avionSpremiPoParametrima(avioni);
    }
}
