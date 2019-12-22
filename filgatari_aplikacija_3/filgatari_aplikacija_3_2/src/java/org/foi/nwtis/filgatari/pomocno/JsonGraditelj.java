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
    public static String napraviJsonZaDodajAerodrom(String icao, String naziv, String drzava, String lat, String lon) {
        JsonObject jsonOdgovor;
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                .add("icao", icao)
                .add("naziv", naziv)
                .add("država", drzava)
                .add("lokacija", Json.createObjectBuilder().add("lat", lat)
                        .add("lon", lon)));
        return jsonOdgovor.toString();
    }

    /**
     * Gradi json tekst preko kojeg se šalje podatke aerodroma
     *
     * @param a
     * @return json tekst
     */
    public static String napraviJsonZaAzurirajAerodrom(Aerodrom a) {
        JsonObject jsonOdgovor;
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                .add("icao", a.getIcao())
                    .add("naziv", a.getNaziv())
                    .add("država", a.getDrzava())
                    .add("lokacija",Json.createObjectBuilder().add("lat", a.getLokacija().getLatitude())
                        .add("lon", a.getLokacija().getLongitude())));

        return jsonOdgovor.toString();
    }

}
