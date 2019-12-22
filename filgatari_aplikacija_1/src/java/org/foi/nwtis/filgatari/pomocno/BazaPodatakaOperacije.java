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
        this.klasaDrivera = bpk.getDriverDatabase();

        Class.forName(klasaDrivera);
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
        this.klasaDrivera = bpk.getDriverDatabase();

        Class.forName(klasaDrivera);
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
     * Provjerava da li korisnik postoji
     *
     * @param korisnik objekt korisnika
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectKorisnikPostoji(KorisnikPodaci korisnik) throws SQLException {
        String upitSelect = "SELECT * FROM KORISNICI WHERE USERNAME = ? AND PASSWORD = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, korisnik.getKorisnickoIme());
        preparedStmt.setString(2, korisnik.getLozinka());
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Vraća jednog korisnika.
     *
     * @param korisnickoime
     * @return jednog korisnika
     * @throws SQLException
     */
    public Korisnik korisniciSelectKorimeKorisnik(String korisnickoime) throws SQLException {
        String upit = "SELECT * FROM KORISNICI WHERE USERNAME = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, korisnickoime);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        Korisnik k = new Korisnik();
        k.setId(rs.getInt("ID"));
        k.setIme(rs.getString("FIRSTNAME"));
        k.setPrezime(rs.getString("LASTNAME"));
        k.setKor_ime(rs.getString("USERNAME"));
        k.setEmail_adresa(rs.getString("EMAIL"));
        k.setDatum_pohrane(rs.getTimestamp("STORED"));

        return k;
    }

    /**
     * Provjerava da li korisnik postoji
     *
     * @param ime
     * @param prezime
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectImePrezimePostoji(String ime, String prezime) throws SQLException {
        String upitSelect = "SELECT * FROM KORISNICI WHERE FIRSTNAME = ? AND LASTNAME = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, ime);
        preparedStmt.setString(2, prezime);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Provjerava da li korisnik postoji
     *
     * @param korisnickoIme
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectKorimePostoji(String korisnickoIme) throws SQLException {
        String upitSelect = "SELECT * FROM KORISNICI WHERE USERNAME = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, korisnickoIme);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Vraća sve korisnike.
     *
     * @return listu svih korisnika
     * @throws SQLException
     */
    public ArrayList<Korisnik> korisniciSelectSviKorisnici() throws SQLException {
        ArrayList<Korisnik> dohvaceniKorisnici = new ArrayList<>();
        String upit = "SELECT * FROM KORISNICI";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Korisnik k = new Korisnik();
            k.setId(rs.getInt("ID"));
            k.setIme(rs.getString("FIRSTNAME"));
            k.setPrezime(rs.getString("LASTNAME"));
            k.setKor_ime(rs.getString("USERNAME"));
            k.setEmail_adresa(rs.getString("EMAIL"));
            k.setDatum_pohrane(rs.getTimestamp("STORED"));
            dohvaceniKorisnici.add(k);
        }

        return dohvaceniKorisnici;
    }

    /**
     * Dodaje novi zapis za korisnike
     *
     * @param korisnik
     * @throws SQLException
     */
    public boolean korisniciInsert(Korisnik korisnik) {
        boolean odgovor = false;
        try {
            String upit = "INSERT INTO KORISNICI (USERNAME, FIRSTNAME, LASTNAME, PASSWORD, EMAIL)"
                    + " VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = veza.prepareStatement(upit);

            preparedStmt.setString(1, korisnik.getKor_ime());
            preparedStmt.setString(2, korisnik.getIme());
            preparedStmt.setString(3, korisnik.getPrezime());
            preparedStmt.setString(4, korisnik.getLozinka());
            preparedStmt.setString(5, korisnik.getEmail_adresa());

            preparedStmt.execute();

            odgovor = true;
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodatakaOperacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        return odgovor;
    }

    /**
     *
     * Ažurira korisnika s novim imenom i prezimenom.
     *
     * @param korisnik
     * @return false ako ne postoji, inače true
     * @throws SQLException
     */
    public boolean korisniciUpdateKorisnik(Korisnik korisnik) {
        boolean odgovor = false;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vrijeme = format.format(date);
        try {
            String upit = "UPDATE KORISNICI SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, STORED=? WHERE USERNAME=?";

            PreparedStatement preparedStmt = veza.prepareStatement(upit);

            preparedStmt.setString(1, korisnik.getIme());
            preparedStmt.setString(2, korisnik.getPrezime());
            preparedStmt.setString(3, korisnik.getEmail_adresa());
            preparedStmt.setTimestamp(4, Timestamp.valueOf(vrijeme));
            preparedStmt.setString(5, korisnik.getKor_ime());

            preparedStmt.execute();

            odgovor = true;
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodatakaOperacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        return odgovor;
    }

    /**
     * Ažurira korisnika s novim imenom i prezimenom.
     *
     * @param korisnickoIme
     * @param ime
     * @param prezime
     * @return false ako ne postoji, inače true
     * @throws SQLException
     */
    public boolean korisniciUpdatePrezimeIme(String korisnickoIme, String ime, String prezime) throws SQLException {
        String upit = "UPDATE KORISNICI SET FIRSTNAME=?, LASTNAME=? WHERE USERNAME=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, ime);
        preparedStmt.setString(2, prezime);
        preparedStmt.setString(3, korisnickoIme);

        preparedStmt.execute();

        return true;
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

        if (odVrijeme != null) {
            upit += " AND vrijeme>=? ";
        }
        if (doVrijeme != null) {
            upit += " AND vrijeme<=? ";
        }
        if (!korisnik.isEmpty()) {
            upit += " AND korisnik=? ";
        }
        if (!adresaZahtjeva.isEmpty()) {
            upit += " AND url=? ";
        }

        upit += " ORDER BY VRIJEME DESC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        if (odVrijeme != null) {
            preparedStmt.setTimestamp(1, odVrijeme);
        }
        if (doVrijeme != null) {
            preparedStmt.setTimestamp(2, doVrijeme);
        }
        if (!korisnik.isEmpty()) {
            preparedStmt.setString(3, korisnik);
        }
        if (!adresaZahtjeva.isEmpty()) {
            if (korisnik.isEmpty()) {
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

    /**
     * Vraca posljednih n aviona za odabrani aerodom
     *
     * @param icao
     * @param n
     * @return List<AvionLeti>
     * @throws SQLException
     */
    public List<AvionLeti> aerodromNAviona(String icao, int n) throws SQLException {
        List<AvionLeti> avioni = new ArrayList<>();
        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT= ? ORDER BY STORED DESC LIMIT ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);
        preparedStmt.setInt(2, n);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            AvionLeti avion = postaviAvion(rs);
            avioni.add(avion);
        }
        return avioni;
    }

    /**
     * Vraca podatke zadnjeg preuzetog aviona za odabrani aerodrom
     *
     * @param icao
     * @return AvionLeti
     * @throws SQLException
     */
    public AvionLeti aerodromZadnjiAvion(String icao) throws SQLException {
        AvionLeti avion = new AvionLeti();
        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT=? ORDER BY STORED DESC LIMIT 1";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            avion = postaviAvion(rs);
        }
        return avion;
    }

    /**
     * Podaci o letovima aviona koji su poletjeli s odabranog aerodroma u nekom
     * vremenskom intervalu
     *
     * @param icao sifra aerodroma
     * @param odDatuma
     * @param doDatuma
     * @return List<AvionLeti>
     */
    public List<AvionLeti> avionLetoviSOdabranogAerodroma(String icao, long odDatuma, long doDatuma) throws SQLException {
        List<AvionLeti> avioni = new ArrayList<>();

        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT=? AND FIRSTSEEN BETWEEN ? AND ? ORDER BY FIRSTSEEN ASC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);
        preparedStmt.setLong(2, odDatuma / 1000);
        preparedStmt.setLong(3, doDatuma / 1000);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            AvionLeti avion = postaviAvion(rs);
            avioni.add(avion);
        }
        return avioni;
    }

    /**
     * Podaci o svim letovima odabranog aviona u nekom vremenskom intervalu
     *
     * @param icao24 sifra aviona
     * @param odDatuma
     * @param doDatuma
     * @return List<AvionLeti>
     */
    public List<AvionLeti> avionLetoviUIntervalu(String icao24, long odDatuma, long doDatuma) throws SQLException {
        List<AvionLeti> avioni = new ArrayList<>();
        String upit = "SELECT * FROM AIRPLANES WHERE ICAO24=? AND FIRSTSEEN BETWEEN ? AND ? ORDER BY FIRSTSEEN ASC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao24);
        preparedStmt.setLong(2, odDatuma / 1000);
        preparedStmt.setLong(3, doDatuma / 1000);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            AvionLeti avion = postaviAvion(rs);
            avioni.add(avion);
        }
        return avioni;
    }

    /**
     * Podaci o svim letovima odabranog aviona u nekom vremenskom intervalu
     *
     * @param icao24 sifra aviona
     * @param icao sifra aerodroma
     * @param odDatuma
     * @param doDatuma
     * @return List<AvionLeti>
     */
    public List<String> avionLetoviUIntervaluNazivAerodroma(String icao24, long odDatuma, long doDatuma) throws SQLException {
        List<String> avioni = new ArrayList<>();
        String upit = "SELECT MA.NAME AS NAME FROM AIRPLANES A, MYAIRPORTS MA WHERE "
                + "MA.IDENT = A.ESTDEPARTUREAIRPORT"
                + " AND A.ICAO24=? AND A.FIRSTSEEN BETWEEN ? AND ? ORDER BY A.FIRSTSEEN ASC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao24);
        preparedStmt.setLong(2, odDatuma / 1000);
        preparedStmt.setLong(3, doDatuma / 1000);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            String nazivAerodroma = "";
            try {
                nazivAerodroma = rs.getString("NAME");
                avioni.add(nazivAerodroma);
            } catch (SQLException ex) {
                Logger.getLogger(BazaPodatakaOperacije.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return avioni;
    }

    /**
     * Postavlja vrijednosti u avion
     *
     * @param rs ResultSet
     * @return AvionLeti
     */
    private AvionLeti postaviAvion(ResultSet rs) {
        AvionLeti avion = new AvionLeti();
        try {
            avion.setIcao24(rs.getString("ICAO24"));
            avion.setFirstSeen(Integer.parseInt(rs.getString("FIRSTSEEN")));
            avion.setEstDepartureAirport(rs.getString("ESTDEPARTUREAIRPORT"));
            avion.setLastSeen(Integer.parseInt(rs.getString("LASTSEEN")));
            avion.setEstArrivalAirport(rs.getString("ESTARRIVALAIRPORT"));
            avion.setCallsign(rs.getString("CALLSIGN"));
            avion.setEstDepartureAirportHorizDistance(Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTHORIZDISTANCE")));
            avion.setEstDepartureAirportVertDistance(Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTVERTDISTANCE")));
            avion.setEstArrivalAirportHorizDistance(Integer.parseInt(rs.getString("ESTARRIVALAIRPORTHORIZDISTANCE")));
            avion.setEstArrivalAirportVertDistance(Integer.parseInt(rs.getString("ESTARRIVALAIRPORTVERTDISTANCE")));
            avion.setDepartureAirportCandidatesCount(Integer.parseInt(rs.getString("DEPARTUREAIRPORTCANDIDATESCOUNT")));
            avion.setArrivalAirportCandidatesCount(Integer.parseInt(rs.getString("ARRIVALAIRPORTCANDIDATESCOUNT")));
        } catch (SQLException ex) {
            Logger.getLogger(BazaPodatakaOperacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        return avion;
    }

    /**
     * Sprema avione prema zadanim parametrima
     *
     * @param avioni
     * @throws SQLException
     */
    public void avionSpremiPoParametrima(List<AvionLeti> avioni) throws SQLException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vrijeme = format.format(date);

        for (AvionLeti avion : avioni) {

            String upit = "SELECT * FROM AIRPLANES WHERE ICAO24= ? AND FIRSTSEEN=? AND ESTARRIVALAIRPORT IS NOT NULL";

            PreparedStatement preparedStmt = veza.prepareStatement(upit);
            preparedStmt.setString(1, avion.getIcao24());
            preparedStmt.setLong(2, avion.getFirstSeen());
            preparedStmt.execute();
            try (Statement s = preparedStmt; ResultSet rs = preparedStmt.executeQuery()) {
                if (!rs.next()) {
                    String upitInsert = avioniInsert(avion, vrijeme);
                    s.executeUpdate(upitInsert);
                    System.out.println("Avioni uspjesno uneseni");
                }/*
                else {
                    String upitUpdate = avioniUpdate(avion, vrijeme);
                    s.executeUpdate(upitUpdate);
                    System.out.println("Avioni uspjesno azurirani");
                }*/
            }

        }
    }

    /**
     * Podaci o avionima koji se nalaze na aerodromu
     *
     * @param icao parametar sifre aerodroma
     * @return lista aviona po aerodromu
     * @throws SQLException
     */
    public List<AvionLeti> avioniAerodroma(String icao) throws SQLException {
        List<AvionLeti> avioni = new ArrayList<>();
        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            AvionLeti avion = postaviAvion(rs);
            avioni.add(avion);
        }

        return avioni;
    }

    /**
     * upit za azuriranje tablice AIRPLANES
     *
     * @param avion
     * @param vrijeme
     * @return String
     */
    public String avioniUpdate(AvionLeti avion, String vrijeme) {
        return "UPDATE AIRPLANES SET STORED='" + vrijeme
                + "', ESTDEPARTUREAIRPORTHORIZDISTANCE=" + avion.getEstDepartureAirportHorizDistance()
                + ", ESTDEPARTUREAIRPORTVERTDISTANCE=" + avion.getEstArrivalAirportVertDistance()
                + ", ESTARRIVALAIRPORTHORIZDISTANCE=" + avion.getEstArrivalAirportHorizDistance()
                + ", ESTARRIVALAIRPORTVERTDISTANCE=" + avion.getEstArrivalAirportVertDistance()
                + " WHERE ICAO24='" + avion.getIcao24()
                + "' AND FIRSTSEEN=" + avion.getFirstSeen()
                + " AND LASTSEEN=" + avion.getLastSeen();
    }

    /**
     * upit za dodavanje u AIRPLANES
     *
     * @param avion avion koji se unosi
     * @param vrijeme vrijeme spremanja
     * @return String
     */
    public String avioniInsert(AvionLeti avion, String vrijeme) {
        return "INSERT INTO AIRPLANES(ICAO24,FIRSTSEEN,ESTDEPARTUREAIRPORT,LASTSEEN,"
                + "ESTARRIVALAIRPORT,CALLSIGN,ESTDEPARTUREAIRPORTHORIZDISTANCE,ESTDEPARTUREAIRPORTVERTDISTANCE,"
                + "ESTARRIVALAIRPORTHORIZDISTANCE,ESTARRIVALAIRPORTVERTDISTANCE,DEPARTUREAIRPORTCANDIDATESCOUNT,"
                + "ARRIVALAIRPORTCANDIDATESCOUNT,STORED) VALUES ('" + avion.getIcao24() + "', "
                + avion.getFirstSeen() + ", '"
                + avion.getEstDepartureAirport() + "', "
                + avion.getLastSeen() + ", '"
                + avion.getEstArrivalAirport() + "', '"
                + avion.getCallsign() + "', "
                + avion.getEstDepartureAirportHorizDistance() + ", "
                + avion.getEstDepartureAirportVertDistance() + ", "
                + avion.getEstArrivalAirportHorizDistance() + ", "
                + avion.getEstArrivalAirportVertDistance() + ", "
                + avion.getDepartureAirportCandidatesCount() + ", "
                + avion.getArrivalAirportCandidatesCount() + ", '"
                + vrijeme + "')";
    }

    /**
     * Vraća sve aerodrome.
     *
     * @return listu svih korisnika
     * @throws SQLException
     */
    public ArrayList<Aerodrom> aerodromiSelectSviMyairports() throws SQLException {
        ArrayList<Aerodrom> dohvaceniAerodromi = new ArrayList<>();
        String upit = "SELECT * FROM MYAIRPORTS";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Aerodrom a = new Aerodrom();
            a.setDrzava(rs.getString("ISO_COUNTRY"));
            a.setIcao(rs.getString("IDENT"));
            a.setNaziv(rs.getString("NAME"));
            String[] lokacija = rs.getString("COORDINATES").split(", ");
            a.setLokacija(new Lokacija(lokacija[1], lokacija[0]));
            dohvaceniAerodromi.add(a);
        }

        return dohvaceniAerodromi;
    }

    /**
     * Provjerava da li aerodrom s traženim nazivom postoji
     *
     * @param icao
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean aerodromSelectIcaoPostoji(String icao) throws SQLException {
        String upitSelect = "SELECT * FROM MYAIRPORTS WHERE IDENT = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, icao);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Dodaje novi aerodrom.
     *
     * @param aerodrom objekt aerodroma
     * @return false ako aerodrom s tim nazivom već postoji, inače true
     * @throws SQLException
     */
    public boolean aerodromInsert(Aerodrom aerodrom) throws SQLException {
        if (aerodromSelectIcaoPostoji(aerodrom.getNaziv())) {
            return false;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vrijeme = format.format(date);

        String upit = "INSERT INTO MYAIRPORTS(IDENT, NAME, ISO_COUNTRY, COORDINATES, STORED) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, aerodrom.getIcao());
        preparedStmt.setString(2, aerodrom.getNaziv());
        preparedStmt.setString(3, aerodrom.getDrzava());
        preparedStmt.setString(4, aerodrom.getLokacija().getLatitude() + ", " + aerodrom.getLokacija().getLongitude());
        preparedStmt.setTimestamp(5, Timestamp.valueOf(vrijeme));

        preparedStmt.execute();

        return true;
    }

    /**
     * Ažurira aerodrom s novim podacima.
     *
     * @param aerodrom
     * @return false ako aerodorm ne postoji, inače true
     * @throws SQLException
     */
    public boolean aerodromiUpdate(Aerodrom aerodrom) throws SQLException {
        if (!aerodromSelectIcaoPostoji(aerodrom.getIcao())) {
            return false;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vrijeme = format.format(date);

        String upit = "UPDATE MYAIRPORTS SET NAME=?, COORDINATES=?, STORED=? WHERE IDENT=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, aerodrom.getNaziv());
        preparedStmt.setString(2, aerodrom.getLokacija().getLatitude() + ", " + aerodrom.getLokacija().getLongitude());
        preparedStmt.setTimestamp(3, Timestamp.valueOf(vrijeme));
        preparedStmt.setString(4, aerodrom.getIcao());

        preparedStmt.execute();

        return true;
    }

    /**
     * Briše AERODROM s traženim id.
     *
     * @param id
     * @return false ako aerodrom ne postoji, inače true
     * @throws SQLException
     */
    public boolean aerodromDelete(String id) throws SQLException {
        if (!aerodromSelectIcaoPostoji(id)) {
            return false;
        }

        String upit = "DELETE FROM MYAIRPORTS WHERE IDENT=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, id);
        preparedStmt.execute();

        return true;
    }

    /**
     * Provjerava da li aerodrom s traženim nazivom postoji
     *
     * @param icao
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public Aerodrom aerodromAirportsSelectByIcao(String icao) throws SQLException {
        Aerodrom dohvaceniAerodrom = new Aerodrom();
        String upit = "SELECT * FROM AIRPORTS WHERE IDENT = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Aerodrom a = new Aerodrom();
            a.setDrzava(rs.getString("ISO_COUNTRY"));
            a.setIcao(rs.getString("IDENT"));
            a.setNaziv(rs.getString("NAME"));
            String[] lokacija = rs.getString("COORDINATES").split(", ");
            a.setLokacija(new Lokacija(lokacija[1], lokacija[0]));
            dohvaceniAerodrom = a;
        }

        return dohvaceniAerodrom;
    }

    /**
     * Provjerava da li aerodrom s traženim nazivom postoji
     *
     * @param icao
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public Aerodrom aerodromSelectByIcao(String icao) throws SQLException {
        Aerodrom dohvaceniAerodrom = new Aerodrom();
        String upit = "SELECT * FROM MYAIRPORTS WHERE IDENT = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, icao);

        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Aerodrom a = new Aerodrom();
            a.setDrzava(rs.getString("ISO_COUNTRY"));
            a.setIcao(rs.getString("IDENT"));
            a.setNaziv(rs.getString("NAME"));
            String[] lokacija = rs.getString("COORDINATES").split(", ");
            a.setLokacija(new Lokacija(lokacija[1], lokacija[0]));
            dohvaceniAerodrom = a;
        }

        return dohvaceniAerodrom;
    }

    /*_   _                                       _             
 | \ | |   __ _   _ __    _ __    ___    __| |  _ __     ___ 
 |  \| |  / _` | | '_ \  | '__|  / _ \  / _` | | '_ \   / _ \
 | |\  | | (_| | | |_) | | |    |  __/ | (_| | | | | | |  __/
 |_| \_|  \__,_| | .__/  |_|     \___|  \__,_| |_| |_|  \___|
                 |_|                                             
     */
    /**
     * Vraća popis letova koje je potrebno letjeti od jednog aerodroma do drugog
     *
     * @param aerodrom1
     * @param aerodrom2
     * @return lista letova
     * @throws SQLException
     */
    public List<AvionLeti> letoviPresjedanja(String aerodrom1, String aerodrom2) throws SQLException {
        List<AvionLeti> listaLetova = new ArrayList<>();

        Aerodrom prviAerodrom = new Aerodrom();

        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT = ? AND ESTARRIVALAIRPORT = 2 AND FIRSTSEEN<=";

        PreparedStatement preparedStmt1 = veza.prepareStatement(upit);
        preparedStmt1.setString(1, aerodrom1);

        preparedStmt1.execute();
        ResultSet rs1 = preparedStmt1.executeQuery();
        while (rs1.next()) {

        }

        return listaLetova;
    }

    /**
     * Računa udaljenost između dva aerodroma
     *
     * @param aerodrom1
     * @param aerodrom2
     * @return udaljest
     * @throws SQLException
     */
    public Double udaljenostAerodroma(String aerodrom1, String aerodrom2) throws SQLException {
        Double udaljenost;

        Aerodrom prviAerodrom = new Aerodrom();
        String upit1 = "SELECT * FROM MYAIRPORTS WHERE IDENT = ?";

        PreparedStatement preparedStmt1 = veza.prepareStatement(upit1);
        preparedStmt1.setString(1, aerodrom1);

        preparedStmt1.execute();
        ResultSet rs1 = preparedStmt1.executeQuery();
        while (rs1.next()) {
            Aerodrom a = new Aerodrom();
            a.setDrzava(rs1.getString("ISO_COUNTRY"));
            a.setIcao(rs1.getString("IDENT"));
            a.setNaziv(rs1.getString("NAME"));
            String[] lokacija = rs1.getString("COORDINATES").split(", ");
            a.setLokacija(new Lokacija(lokacija[1], lokacija[0]));
            prviAerodrom = a;
        }

        Aerodrom drugiAerodrom = new Aerodrom();
        String upit2 = "SELECT * FROM MYAIRPORTS WHERE IDENT = ?";

        PreparedStatement preparedStmt2 = veza.prepareStatement(upit2);
        preparedStmt2.setString(1, aerodrom2);

        preparedStmt2.execute();
        ResultSet rs2 = preparedStmt2.executeQuery();
        while (rs2.next()) {
            Aerodrom a = new Aerodrom();
            a.setDrzava(rs2.getString("ISO_COUNTRY"));
            a.setIcao(rs2.getString("IDENT"));
            a.setNaziv(rs2.getString("NAME"));
            String[] lokacija = rs2.getString("COORDINATES").split(", ");
            a.setLokacija(new Lokacija(lokacija[1], lokacija[0]));
            drugiAerodrom = a;
        }
        udaljenost = distance(Double.parseDouble(prviAerodrom.getLokacija().getLatitude()),
                Double.parseDouble(drugiAerodrom.getLokacija().getLatitude()),
                Double.parseDouble(prviAerodrom.getLokacija().getLongitude()),
                Double.parseDouble(drugiAerodrom.getLokacija().getLongitude()));

        return udaljenost;
    }

    /**
     * Računa udaljenost između dvije točke zadane sa latitude i longitude
     *
     * @param lat1 latitude prvog aerodroma
     * @param lat2 latitude drugog aerodroma
     * @param lon1 longitude prvog aerodroma
     * @param lon2 longitude drugog aerodroma
     * @return
     * @returns udaljenost u km
     */
    public double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    /**
     * Preuzima podatke koji se nalaze unutar zadanih udaljenosti od zadanog
     * aerodroma
     *
     * @param icao
     * @param odKm
     * @param doKm
     * @return
     * @throws SQLException
     */
    public List<Aerodrom> aerodromiUdaljeniOdAerodroma(String icao, double odKm, double doKm) throws SQLException {
        ArrayList<Aerodrom> dohvaceniAerodromi = aerodromiSelectSviMyairports();
        Aerodrom odabraniAerodrom = aerodromSelectByIcao(icao);
        ArrayList<Aerodrom> aerodromiUnutarParametara = new ArrayList<>();

        for (Aerodrom a : dohvaceniAerodromi) {
            double udaljenost = 0d;
            udaljenost = distance(Double.parseDouble(a.getLokacija().getLatitude()),
                    Double.parseDouble(odabraniAerodrom.getLokacija().getLatitude()),
                    Double.parseDouble(a.getLokacija().getLongitude()),
                    Double.parseDouble(odabraniAerodrom.getLokacija().getLongitude()));

            if (udaljenost >= odKm && udaljenost <= doKm) {
                aerodromiUnutarParametara.add(a);
            }
        }
        return aerodromiUnutarParametara;
    }
}
