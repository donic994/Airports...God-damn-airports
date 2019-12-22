package org.foi.nwtis.filgatari.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.dkermek.ws.serveri.AerodromStatus;
import org.foi.nwtis.dkermek.ws.serveri.AerodromiWS;
import org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service;
import org.foi.nwtis.dkermek.ws.serveri.Avion;
import org.foi.nwtis.filgatari.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.filgatari.pomocno.Dnevnik;
import org.foi.nwtis.filgatari.pomocno.KorisnikPodaci;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.web.podaci.Aerodrom;
import org.foi.nwtis.filgatari.ws.klijenti.AerodromiWSDL;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

@Path("aerodromi")
public class AIRP2REST {

    private static ServletContext sc = null;

    @Context
    private UriInfo context;

    @Context
    HttpServletRequest request;

    public AIRP2REST() {
    }

    /**
     * povezuje se na bazu podataka vraća popis svih aerodroma, a za svaki
     * aerodrom icao, naziv, državu i geo lokaciju u application/json formatu.
     *
     * @return string sa porukom u kojoj javlja uspješnost procedure
     * @throws SQLException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        List<Aerodrom> sviAerodromi = new ArrayList<>();

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviAerodromi = bpo.aerodromiSelectSviMyairports();
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }

        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        JsonArray aerodromiJsonDio = jsonOdgovor.postaviSveAerodromeJsonDio(sviAerodromi);
        return jsonOdgovor.vratiKompletanJsonOdgovor(aerodromiJsonDio);
    }

    /**
     * Metoda za dodavanje novog aerodroma u bazu podataka.
     *
     * @param icao aerodroma u application/json formatu
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String icao, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";

        Aerodrom aerodrom;
        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            Gson gson = new Gson();
            aerodrom = gson.fromJson(icao, Aerodrom.class);

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (bpo.aerodromSelectIcaoPostoji(aerodrom.getIcao())) {
                uspjesno = false;
                poruka = "Aerodrom s icao " + aerodrom.getIcao() + " vec postoji!";
            } else {
                aerodrom = bpo.aerodromAirportsSelectByIcao(aerodrom.getIcao());
                Lokacija lokacija = PomocnaKlasa.dohvatiGPSLokaciju(aerodrom.getNaziv());
                aerodrom.setLokacija(lokacija);
                bpo.aerodromInsert(aerodrom);
                //sinkronizirajAerodrome();
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }

        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }

    /**
     * Metoda za dohvat podataka pojedinog aerodroma.
     *
     * @param id identifikator aerodroma po kojem se dohvaća
     * @param korisnickoIme
     * @param lozinka
     * @return vraća podatke o aerodromu s zadanim id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getJsonId(@PathParam("id") String icao, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        String idAerodroma = icao;
        Aerodrom a = new Aerodrom();

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.aerodromSelectIcaoPostoji(idAerodroma)) {
                uspjesno = false;
                poruka = "Aerodrom s icao " + icao + " ne postoji!";
            } else {
                a = bpo.aerodromSelectByIcao(idAerodroma);
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }

        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            JsonArray aerodromJsonDio = jsonOdgovor.postaviAerodromJsonDio(a);
            return jsonOdgovor.vratiKompletanJsonOdgovor(aerodromJsonDio);
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
    }

    /**
     * Metoda za ažuriranje novog aerodroma u bazi podataka.
     *
     * @param icao identifikator aerodroma po kojem se ažurira
     * @param podaci podaci aerodroma u application/json formatu
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String putJson(@PathParam("id") String icao, String podaci, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = " ";
        String icaoAerodroma = icao;

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.aerodromSelectIcaoPostoji(icaoAerodroma)) {
                uspjesno = false;
                poruka = "Aerodrom s icao " + icao + " ne postoji!";
            } else {
                Gson gson = new Gson();
                Aerodrom aerodrom = gson.fromJson(podaci, Aerodrom.class);
                Lokacija lokacija = PomocnaKlasa.dohvatiGPSLokaciju(aerodrom.getNaziv());
                aerodrom.setIcao(icaoAerodroma);
                aerodrom.setLokacija(lokacija);
                bpo.aerodromiUpdate(aerodrom);
                //sinkronizirajAerodrome();
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }

        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }

    /**
     * Metoda za DELETE na bazi osnovne adrese nije dozvoljena.
     *
     * @return grešku HTTP statusa 405: Nedozvoljena operacija
     */
    @DELETE
    public Response deleteJson() {
        return dajOdgovorZaNedozvoljenPristup();
    }

    /**
     * Metoda za brisanje pojedinog aerodroma.
     *
     * @param id identifikator aerodroma po kojem se dohvaća
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String deleteJson(@PathParam("id") String id, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        String icaoAerodroma = id;

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);

            if (aerodromImaAvione(icaoAerodroma)) {
                uspjesno = false;
                poruka = "Aerodrom ima avione!";
            } else {
                BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
                if (!bpo.aerodromSelectIcaoPostoji(icaoAerodroma)) {
                    uspjesno = false;
                    poruka = "Aerodrom s icao " + id + " ne postoji!";
                } else {
                    bpo.aerodromDelete(icaoAerodroma);
                    //obrisiAerodromNaServisu(icaoAerodroma);
                    dnevnik.postaviUspjesanStatus();
                }
                bpo.zatvoriVezu();
            }
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }

        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }
    
     /**
     * Metoda za dohvat aviona pojedinog aerodroma.
     * @param id identifikator aerodroma.
     * @param korisnickoIme
     * @param lozinka
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/avion")
    public String getJsonAvioni(@PathParam("id") String id, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        boolean uspjesno = true;
        String poruka = "";
        String icaoAerodroma = id;
        
        List<AvionLeti> sviAvioni = new ArrayList<>();
        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            //KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();      
            //sviAvioni = AIRP2REST.dajSveAvioneAerodromaGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), icaoAerodroma);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
                if (!bpo.aerodromSelectIcaoPostoji(icaoAerodroma)) {
                    uspjesno = false;
                    poruka = "Aerodrom s icao " + id + " ne postoji!";
                } else {
                    sviAvioni = bpo.avioniAerodroma(icaoAerodroma);
                    dnevnik.postaviUspjesanStatus();
                }
                bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());
        
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            JsonArray avioniJsonDio = jsonOdgovor.postaviSveAvioneJsonDio(sviAvioni);
            return jsonOdgovor.vratiKompletanJsonOdgovor(avioniJsonDio);
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
    }
    
    
    
    
        public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Avion> dajSveAvioneAerodromaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.dajSveAvioneAerodromaGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    /**
     * Briše aerodrom na web servisu
     *
     * @param aerodromIcao
     */
    private void obrisiAerodromNaServisu(String aerodromIcao) throws Exception {
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = AerodromiWSDL.obrisiAerodromGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), aerodromIcao);

        if (rezultat == false) {
            throw new Exception("Neuspješno brisanje sa servisa");
        }
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
    public static void setSc(ServletContext sc) {
        AIRP2REST.sc = sc;
    }

    /**
     * Vraća url adresu zahtjeva.
     *
     * @return url adresa zahtjeva
     */
    private String vratiTrenutnuAdresuZahtjeva() {
        String adresa = context.getRequestUri().toString() + "/" + request.getMethod();
        return adresa;
    }

    /**
     * Autentificira korisnika prema bazi podataka
     *
     * @param korisnickoIme
     * @param lozinka
     * @throws Exception
     */
    private void autentificirajKorisnika(String korisnickoIme, String lozinka) throws Exception {
        if (PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false) {
            throw new Exception("Neovlasten pristup! Pogresno korisnicko ime i/ili lozinka.");
        }
    }

    /**
     * Vraća HTTP Status 405 - Method Not Allowed odgovor. Zapisuje poziv
     * operacije u dnevnik.
     *
     * @return
     */
    private Response dajOdgovorZaNedozvoljenPristup() {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.zavrsiISpremiDnevnik("gost", vratiTrenutnuAdresuZahtjeva());

        Response.ResponseBuilder rb = Response.status(Response.Status.METHOD_NOT_ALLOWED);
        Response response = rb.build();
        return response;
    }

    /**
     * Provjerava da li aerodrom ima avione.
     *
     * @param aerodromID
     * @return true ako ima, inace false
     */
    private boolean aerodromImaAvione(String aerodromID) {
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        List<Avion> avioni = AerodromiWSDL.dajSveAvioneAerodromaGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), aerodromID);

        if (avioni.isEmpty()) {
            return false;
        }
        return true;

    }

    /**
     * Sinkronizira aerodrome u bazi podataka sa onima na web servisu. 1.
     * Briše sve aerodrome grupe na web servisu 2. Dohvaća sve aerodrome
     * iz baze podataka 3. Svaki aerodrom iz baze podataka dodaje na web
     * servis
     */
    private void sinkronizirajAerodrome() {
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        AerodromiWSDL.obrisiSveAerodromeGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka());

        List<Aerodrom> sviAerodromi = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviAerodromi = bpo.aerodromiSelectSviMyairports();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AIRP2REST.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Aerodrom a : sviAerodromi) {
            org.foi.nwtis.dkermek.ws.serveri.Aerodrom aerodromWS = preslikajAerodrom(a);
            AerodromiWSDL.dodajAerodromGrupi(korisnik.getKorisnickoIme(), korisnik.getLozinka(), aerodromWS);
        }
    }

    /**
     * Prepisuje vrijednosti iz objekta kakav se dohvaća iz baze podataka u
     * objekt koji se koristi na web servisu.
     *
     * @param a aerodrom dohvaćen iz baze podataka
     * @return aerodrom za web servis
     */
    private org.foi.nwtis.dkermek.ws.serveri.Aerodrom preslikajAerodrom(Aerodrom a) {
        org.foi.nwtis.dkermek.ws.serveri.Aerodrom aerodrom = new org.foi.nwtis.dkermek.ws.serveri.Aerodrom();
        aerodrom.setIcao(a.getIcao());
        aerodrom.setNaziv(a.getNaziv());
        aerodrom.setDrzava(a.getDrzava());

        org.foi.nwtis.dkermek.ws.serveri.Lokacija lokacija = new org.foi.nwtis.dkermek.ws.serveri.Lokacija();
        lokacija.setLatitude(a.getLokacija().getLatitude());
        lokacija.setLongitude(a.getLokacija().getLongitude());
        aerodrom.setLokacija(lokacija);

        AerodromStatus status = prepoznajStatus(a.getStatus());
        aerodrom.setStatus(status);

        return aerodrom;
    }

    /**
     * Pretvara status u obliku stringa iz baze podataka u status iz enumeracije
     * koji se koriste na servisu.
     *
     * @param statusString status u obliku stringa
     * @return status iz enumeracije
     */
    private AerodromStatus prepoznajStatus(String statusString) {
        AerodromStatus statusEnum = AerodromStatus.PASIVAN;
        switch (statusString) {
            case "PASIVAN":
                statusEnum = AerodromStatus.PASIVAN;
                break;
            case "AKTIVAN":
                statusEnum = AerodromStatus.AKTIVAN;
                break;
            case "BLOKIRAN":
                statusEnum = AerodromStatus.BLOKIRAN;
                break;
            case "NEPOSTOJI":
                statusEnum = AerodromStatus.NEPOSTOJI;
                break;
            default:
                break;
        }

        return statusEnum;
    }
}
