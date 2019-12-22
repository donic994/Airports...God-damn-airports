/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.pomocno;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.filgatari.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.web.podaci.DnevnikPodaci;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;
import org.foi.nwtis.filgatari.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 *
 * @author filip
 */
public class BazaPodatakaOperacije {

    private String url;
    private String korisnik;
    private String lozinka;
    private Connection veza;
    private String klasaDrivera;
    BP_Konfiguracija bpk;

    /**
     *
     * @param konfiguracija
     */
    /**
     * Konstruktor klase koji prima kontekst servleta. Učitava potrebne postavke
     * iz konfiguracije i otvara vezu nad bazom. Registrira driver.
     *
     * @param sc kontekst servleta
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public BazaPodatakaOperacije(ServletContext sc) throws SQLException, ClassNotFoundException {
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getAdminDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();

        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }

    /**
     * Konstruktor klase koji čita kontekst servleta iz slušača aplikacije.
     * Učitava potrebne postavke iz konfiguracije i otvara vezu nad bazom.
     * Registrira driver.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public BazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
        ServletContext sc = SlusacAplikacije.servletContext;
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getAdminDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
                
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }

    /**
     * Zatvara vezu nad bazom podataka.
     *
     * @throws SQLException
     */
    public void zatvoriVezu() throws SQLException {
        veza.close();
    }

    /**
     * Dodaje novi zapis u dnevnik
     *
     * @param dnevnik podaci dnevnika
     * @throws SQLException
     */
    public void dnevnikInsert(Dnevnik dnevnik) throws SQLException {
        String upit = "INSERT INTO DNEVNIK (KORISNIK, URL, IPADRESA, TRAJANJE, STATUS)"
                + " VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, dnevnik.getKorisnik());
        preparedStmt.setString(2, dnevnik.getUrl());
        preparedStmt.setString(3, dnevnik.getIpAdresa());
        preparedStmt.setInt(4, dnevnik.getTrajanje());
        preparedStmt.setInt(5, dnevnik.getStatus());

        preparedStmt.execute();
    }
    /**
     * Vraća filtrirane zapise iz dnevnika.
     *
     * @param korisnik
     * @param odVrijeme
     * @param doVrijeme
     * @param adresaZahtjeva
     * @return listu svih zapisa
     * @throws SQLException
     */
    public ArrayList<DnevnikPodaci> dnevnikSelectFiltrirano(String korisnik, Timestamp odVrijeme, Timestamp doVrijeme, String adresaZahtjeva) throws SQLException {
        ArrayList<DnevnikPodaci> dohvaceniZapisi = new ArrayList<>();
        String upit = "SELECT * FROM DNEVNIK WHERE 1=1 ";
        
        if(odVrijeme != null){
            upit += " AND vrijeme>=? ";
        }
        if(doVrijeme != null){
            upit += " AND vrijeme<=? ";
        }
        if(!korisnik.isEmpty()){
            upit += " AND korisnik=? ";
        }
        if(!adresaZahtjeva.isEmpty()){
            upit += " AND url=? ";
        }
                
        upit += " ORDER BY VRIJEME DESC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        
        if(odVrijeme != null){
            preparedStmt.setTimestamp(1, odVrijeme);
        }
        if(doVrijeme != null){
            preparedStmt.setTimestamp(2, doVrijeme);
        }
        if(!korisnik.isEmpty()){
            preparedStmt.setString(3, korisnik);
        }
        if(!adresaZahtjeva.isEmpty()){
            if(korisnik.isEmpty()){
                preparedStmt.setString(3, adresaZahtjeva);
            } else {
                preparedStmt.setString(4, adresaZahtjeva);
            }            
        }
        
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        
        while (rs.next()) {
            DnevnikPodaci d = new DnevnikPodaci();
            d.setId(rs.getInt("ID"));
            d.setStatus(rs.getInt("STATUS"));
            d.setTrajanje(rs.getInt("TRAJANJE"));
            d.setKorisnik(rs.getString("KORISNIK"));
            d.setUrl(rs.getString("URL"));
            d.setIpadresa(rs.getString("IPADRESA"));
            d.setVrijeme(rs.getTimestamp("VRIJEME"));

            dohvaceniZapisi.add(d);
        }

        return dohvaceniZapisi;
    }   
}