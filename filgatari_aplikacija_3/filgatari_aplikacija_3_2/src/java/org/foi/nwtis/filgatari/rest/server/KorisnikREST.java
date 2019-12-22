/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.rest.server;

import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.ws.klijenti.AerodromWSKlijent;
import org.foi.nwtis.filgatari.ws.klijenti.Korisnik;

/**
 * REST Web Service
 *
 * @author filip
 */
@Path("korisnik")
public class KorisnikREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisnikREST
     */
    public KorisnikREST() {
    }

    /**
     * Šalje komandu za preuzimanje svih korisnika.
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);

        boolean uspjesno = odgovor.contains("OK");
        String poruka = "";
        if (uspjesno) {
            List<Korisnik> nizKorisnici = AerodromWSKlijent.dajSveKorisnike(korisnik, lozinka);

            poruka = "{\"odgovor\":" + convertToJSON(nizKorisnici) + ",\"status\": \"OK\"}";
        } else {
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod ispisa popisa svih korisnika}";
        }
        return poruka;
    }

    /**
     * Šalje komandu za preuzimanje svih korisnika.
     *
     * @param id
     * @param korisnik
     * @param lozinka
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getJsonId(@PathParam("id") Integer id, @HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);

        boolean uspjesno = odgovor.contains("OK");
        String poruka = "";
        if (uspjesno) {
            List<Korisnik> nizKorisnici = AerodromWSKlijent.dajSveKorisnike(korisnik, lozinka);
            for(Korisnik k: nizKorisnici){
                if(k.getId() == id){
                      poruka = "{\"odgovor\":" + convertToJSON(k) + ",\"status\": \"OK\"}";
                      break;
                }                
              poruka = "{\"status\": \"ERR\", \"poruka\": Nema korisnika sa id = "+id+"}";
            }
        } else {
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod ispisa popisa svih korisnika}";
        }
        return poruka;
    }

    /**
     * Šalje komandu za dodavanje korisnika.
     *
     * @param unos
     * @param korisnik
     * @param lozinka
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String dodajKorisnika(org.foi.nwtis.filgatari.web.podaci.Korisnik unos, @HeaderParam("lozinka") String lozinka, @HeaderParam("korisnik") String korisnik) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);

        boolean uspjesno = odgovor.contains("OK");
        String poruka = "";
        if (uspjesno) {
            if (AerodromWSKlijent.dodajKorisnika(unos, korisnik, lozinka)) {
                poruka = "{\"odgovor\": OK[],\"status\": \"OK\"}";
            }
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod dodavanja korisnika}";
        } else {
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod autorizacije}";
        }
        return poruka;
    }

    /**
     * Šalje komandu za preuzimanje korisnika.
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/auth")
    public String getJsonId(@PathParam("id") String id, @HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);

        boolean uspjesno = odgovor.contains("OK");
        String poruka = "";
        if (uspjesno) {
            poruka = "{\"odgovor\": [], \"status\": \"OK\"}";
        } else {
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod autorizacije korisnika}";
        }
        return poruka;
    }
    
    /**
     * Šalje komandu za dodavanje korisnika.
     *
     * @param unos
     * @param korisnik
     * @param lozinka
     * @return
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String azurirajKorisnika(org.foi.nwtis.filgatari.web.podaci.Korisnik unos, @HeaderParam("lozinka") String lozinka, @HeaderParam("korisnik") String korisnik) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);

        boolean uspjesno = odgovor.contains("OK");
        String poruka = "";
        if (uspjesno) {
            if (AerodromWSKlijent.azurirajKorisnika(unos, korisnik, lozinka)) {
                poruka = "{\"odgovor\": OK[],\"status\": \"OK\"}";
            }
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod dodavanja korisnika}";
        } else {
            poruka = "{\"status\": \"ERR\", \"poruka\": Greska kod autorizacije}";
        }
        return poruka;
    }


    public String convertToJSON(Object object) {
        Gson gson = new Gson();
        String odgovor = gson.toJson(object);
        return odgovor;
    }
}
