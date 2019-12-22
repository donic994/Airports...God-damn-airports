package org.foi.nwtis.filgatari.pomocno;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.filgatari.konfiguracije.Konfiguracija;
import org.foi.nwtis.filgatari.web.slusaci.SlusacAplikacije;

/**
 * Klasa sa svim pomocnim operacijama koje se koriste na više mjesta.
 *
 * @author filip
 */
public class PomocnaKlasa {

    /**
     * Dohvaca postavku iz konfiguracije prema nazivu.
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
     * Šalje komandu poslužitelju preko socketa.
     *
     * @param komanda
     * @return
     */
    public static String posaljiKomanduPosluzitelju(String komanda) {
        String adresa = PomocnaKlasa.dohvatiPostavku("adresa");
        int port = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("port"));
        String odgovor = "";

        try {
            Socket socket = new Socket(adresa, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            os.write(komanda.getBytes());
            os.flush();
            socket.shutdownOutput();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                buffer.append((char) znak);
            }
            is.close();
            odgovor = buffer.toString();
        } catch (SocketTimeoutException ex) {
            odgovor = "APLIKACIJA3 | Predugo cekanje na odgovor. Server je zaustavljen ili ne vraca odgovor. " + ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }

        return odgovor;
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
