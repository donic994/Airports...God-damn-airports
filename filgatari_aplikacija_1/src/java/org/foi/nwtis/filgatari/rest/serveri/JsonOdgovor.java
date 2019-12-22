package org.foi.nwtis.filgatari.rest.serveri;

import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.foi.nwtis.dkermek.ws.serveri.Avion;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Klasa za izgradnju odgovora u JSON formatu. Koristi se u REST servisu.
 *
 * @author filip
 */
public class JsonOdgovor {

    private boolean uspjesno;
    private String poruka;
    private String status;

    /**
     * Konstruktor klase
     *
     * @param uspjesno
     * @param poruka
     */
    public JsonOdgovor(boolean uspjesno, String poruka) {
        this.uspjesno = uspjesno;
        this.poruka = poruka;
        this.status = uspjesno ? "OK" : "ERR";
    }

    /**
     * Gradi json niz od liste dobivenih aerodroma.
     *
     * @param sviAerodromi lista aerodroma
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviSveAerodromeJsonDio(List<Aerodrom> sviAerodromi) {
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        for (Aerodrom a : sviAerodromi) {
            if(a.getStatus()== null){
                a.setStatus(" ");
            }
            jsonBuilder.add(Json.createObjectBuilder()
                    .add("icao", a.getIcao())
                    .add("naziv", a.getNaziv())
                    .add("država", a.getDrzava())
                    .add("lokacija", Json.createObjectBuilder().add("lat", a.getLokacija().getLatitude())
                            .add("lon", a.getLokacija().getLongitude()))
                    .add("status", a.getStatus()));
        }
        return jsonBuilder.build();
    }

    /**
     * Gradi json objekt od dobivenog aerodroma.
     *
     * @param aerodrom jedan aerodrom
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviAerodromJsonDio(Aerodrom a) {
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();

        jsonBuilder.add(Json.createObjectBuilder()
                .add("icao", a.getIcao())
                .add("naziv", a.getNaziv())
                .add("država", a.getDrzava())
                .add("lokacija", Json.createObjectBuilder().add("lat", a.getLokacija().getLatitude())
                        .add("lon", a.getLokacija().getLongitude())));

        return jsonBuilder.build();
    }

    /**
     * Gradi json niz od liste dobivenih aerodroma.
     *
     * @param sviAvioni lista aviona
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviSveAvioneJsonDio(List<AvionLeti> sviAvioni) {
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        for (AvionLeti a : sviAvioni) {
            jsonBuilder.add(Json.createObjectBuilder()
                    .add("icao24", a.getIcao24())
                    .add("callSign", a.getCallsign())
                    .add("estDepartuerAirport", a.getEstDepartureAirport())
                    .add("firstSeen", a.getFirstSeen())
                    .add("lastseen", a.getLastSeen())
                    .add("estArrivalAirport", a.getEstArrivalAirport()));
        }

        return jsonBuilder.build();
    }

    /**
     * Gradi json objekt od dobivenog korisnika.
     *
     * @param korisnik
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviKorisnikJsonDio(Korisnik korisnik) {
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();

        jsonBuilder.add(Json.createObjectBuilder()
                .add("ki", korisnik.getKor_ime())
                .add("prezime", korisnik.getPrezime())
                .add("ime", korisnik.getIme())
                .add("email", korisnik.getEmail_adresa()));

        return jsonBuilder.build();
    }

    /**
     * Gradi json niz od liste dobivenih korisnika.
     *
     * @param sviKorisnici
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviSviKorisniciJsonDio(List<Korisnik> sviKorisnici) {
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        for (Korisnik korisnik : sviKorisnici) {
            jsonBuilder.add(Json.createObjectBuilder()
                    .add("korisnicko ime", korisnik.getKor_ime())
                    .add("prezime", korisnik.getPrezime())
                    .add("ime", korisnik.getIme())
                    .add("email", korisnik.getEmail_adresa()));
        }

        return jsonBuilder.build();
    }

    /**
     * Gradi kompletnu strukturu json odgovora koji mora vracati REST servis.
     * Unutar odgovora postoji atribut odgovor koji je u ovom slučaju prazan.
     *
     * @return json rezultat u obliku stringa
     */
    public String vratiKompletanJsonOdgovor() {
        JsonObject jsonOdgovor;

        if (uspjesno) {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", "[]")
                    .add("status", "OK")
                    .build());
        } else {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", "[]")
                    .add("status", "ERR")
                    .add("poruka", poruka)
                    .build());
        }

        return jsonOdgovor.toString();
    }

    /**
     * Gradi kompletnu strukturu json odgovora koji mora vracati REST servis.
     * Unutar odgovora se šalje poseban dio. Primjerice poseban dio može
     * sadržavati podatke o jednom aerodromu ili o svim aerodromima.
     *
     * @param odgovor
     * @return json rezultat u obliku stringa
     */
    public String vratiKompletanJsonOdgovor(JsonArray odgovor) {
        JsonObject jsonOdgovor;

        if (uspjesno) {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", odgovor)
                    .add("status", "OK")
                    .build());
        } else {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", odgovor)
                    .add("status", "ERR")
                    .add("poruka", poruka)
                    .build());
        }

        return jsonOdgovor.toString();
    }

}
