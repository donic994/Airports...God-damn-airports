package org.foi.nwtis.filgatari.pomocno;

import javax.json.Json;
import javax.json.JsonObject;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;

/**
 * Klasa za stvaranje JSON objekata koje prima REST servis za aerodrome u
 * prvoj aplikaciji.
 *
 * @author filip
 */
public class JsonGraditelj {

    /**
     * Gradi json tekst preko kojeg se šalje podatke aerodroma
     *
     * @param icao
     * @param naziv
     * @param drzava
     * @param lat
     * @param lon
     * @return json tekst
     */
    public static String napraviJsonZaDodajAerodrom(String icao) {
        String odgovor = "{\"icao\": \"" + icao + "\"}";
        return odgovor;
    }

    /**
     * Gradi json tekst preko kojeg se šalje podatke aerodroma
     *
     * @param a
     * @return json tekst
     */
    public static String napraviJsonZaAzurirajAerodrom(Aerodrom a) {
        String odgovor = "{\"naziv\": \"" + a.getNaziv() + "\", \"adresa\": \"" + a.getDrzava() + "\"}";

        return odgovor;
    }

}
