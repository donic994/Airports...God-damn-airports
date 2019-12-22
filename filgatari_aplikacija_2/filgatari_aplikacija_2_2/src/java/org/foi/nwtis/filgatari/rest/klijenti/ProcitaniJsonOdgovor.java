package org.foi.nwtis.filgatari.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;
import org.foi.nwtis.filgatari.ws.klijenti.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa koja iz teksta odgovora kojeg je poslao REST servis cita sve elemente.
 *
 * @author filip
 */
public class ProcitaniJsonOdgovor {

    private JsonObject odgovorAtribut;
    private JsonArray odgovorNiz;
    private String status;
    private String poruka;

    /**
     * Cita sve elemente odgovora.
     *
     * @param jsonOdgovor
     */
    public ProcitaniJsonOdgovor(String jsonOdgovor) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonOdgovor));
        JsonObject jsonObject = jsonReader.readObject();

        try {
            odgovorNiz = jsonObject.getJsonArray("odgovor");
            this.odgovorAtribut = odgovorNiz.getJsonObject(0);
        } catch (Exception e) {
            this.odgovorAtribut = Json.createObjectBuilder().build();
        }

        this.status = jsonObject.getString("status");
        try {
            this.poruka = jsonObject.getString("poruka");
        } catch (NullPointerException e) {
            this.poruka = "";
        }
    }

    /**
     * Iz odgovora koji je objket vraca vrijednost po nazivu atributa tj. kljucu.
     *
     * @param nazivAtributa
     * @return
     */
    public String vratiVrijednostAtributaIzOdgovora(String nazivAtributa) {
        return odgovorAtribut.getString(nazivAtributa);
    }

    private String vratiVrijednostString(JsonObject atribut, String nazivAtributa) {
        return atribut.getString(nazivAtributa);
    }

    private int vratiVrijednostInt(JsonObject atribut, String nazivAtributa) {
        return atribut.getInt(nazivAtributa);
    }

    /**
     * Iz pristiglog odgovora u JSON formatu koji sadrzi niz aerodroma cita
     * sve aerodrome i formira listu.
     *
     * @return
     */
    public List<Aerodrom> vratiNizAerodroma() {
        List<Aerodrom> listaAerodroma = new ArrayList<>();
        for (JsonValue jsonValue : odgovorNiz) {
            String objektString = jsonValue.toString();
            JsonReader jsonReader = Json.createReader(new StringReader(objektString));
            JsonObject jsonObject = jsonReader.readObject();

            Aerodrom a = new Aerodrom();
            a.setIcao(vratiVrijednostString(jsonObject, "icao"));
            a.setNaziv(vratiVrijednostString(jsonObject, "naziv"));
            a.setDrzava(vratiVrijednostString(jsonObject, "država"));
            /*Lokacija l = new Lokacija();
            l.setLatitude(vratiVrijednostString(jsonObject, "lat"));
            l.setLongitude(vratiVrijednostString(jsonObject, "lon"));
             */
            a.setStatus(vratiVrijednostString(jsonObject, "status"));
            listaAerodroma.add(a);
        }

        return listaAerodroma;
    }

    /**
     * Iz pristiglog odgovora u JSON formatu koji sadrzi niz avioona cita sve
     * avione i formira listu.
     *
     * @return
     */
    public List<AvionLeti> vratiNizAviona() {
        List<AvionLeti> listaAviona = new ArrayList<>();
        for (JsonValue jsonValue : odgovorNiz) {
            String objektString = jsonValue.toString();
            JsonReader jsonReader = Json.createReader(new StringReader(objektString));
            JsonObject jsonObject = jsonReader.readObject();

            AvionLeti a = new AvionLeti();
            a.setIcao24(vratiVrijednostString(jsonObject, "icao24"));
            a.setCallsign(vratiVrijednostString(jsonObject, "callSign"));
            a.setEstDepartureAirport(vratiVrijednostString(jsonObject, "estDepartuerAirport"));
            a.setEstArrivalAirport(vratiVrijednostString(jsonObject, "estArrivalAirport"));
            a.setFirstSeen(vratiVrijednostInt(odgovorAtribut, "firstSeen"));
            a.setLastSeen(vratiVrijednostInt(odgovorAtribut, "lastseen"));

            listaAviona.add(a);
        }

        return listaAviona;
    }
    

    /**
     * Iz pristiglog odgovora u JSON formatu koji sadrzi niz korisnika cita sve
     * korisnike i formira listu.
     *
     * @return
     */
    public List<Korisnik> vratiNizKorisnika() {
        List<Korisnik> listaKorisnika = new ArrayList<>();
        for (JsonValue jsonValue : odgovorNiz) {
            String objektString = jsonValue.toString();
            JsonReader jsonReader = Json.createReader(new StringReader(objektString));
            JsonObject jsonObject = jsonReader.readObject();

            Korisnik k = new Korisnik();
            k.setKor_ime(vratiVrijednostString(jsonObject, "korIme"));
            k.setIme(vratiVrijednostString(jsonObject, "ime"));
            k.setPrezime(vratiVrijednostString(jsonObject, "prezime"));
            k.setEmail_adresa(vratiVrijednostString(jsonObject, "emailAdresa"));

            listaKorisnika.add(k);
        }

        return listaKorisnika;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public JsonObject getOdgovorAtribut() {
        return odgovorAtribut;
    }

    /**
     *
     * @param odgovorAtribut
     */
    public void setOdgovorAtribut(JsonObject odgovorAtribut) {
        this.odgovorAtribut = odgovorAtribut;
    }

    /**
     *
     * @return
     */
    public JsonArray getOdgovorNiz() {
        return odgovorNiz;
    }

    /**
     *
     * @param odgovorNiz
     */
    public void setOdgovorNiz(JsonArray odgovorNiz) {
        this.odgovorNiz = odgovorNiz;
    }
}
