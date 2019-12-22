/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.ws.klijenti;

import javax.ejb.Stateless;
import javax.jws.WebService;
import org.foi.nwtis.dkermek.ws.serveri.AerodromiWS;
import org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service;

/**
 *
 * @author filip
 */
@WebService(serviceName = "AerodromiWS", portName = "AerodromiWSPort", endpointInterface = "org.foi.nwtis.dkermek.ws.serveri.AerodromiWS", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", wsdlLocation = "WEB-INF/wsdl/nwtis.foi.hr_8080/NWTiS_2019/AerodromiWS.wsdl")
@Stateless
public class AerodromiWSDL {

    public static boolean ucitajUgradeneAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.ucitajUgradeneAerodromeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static boolean ucitajUgradeneAvioneGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.ucitajUgradeneAvioneGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static boolean dodajAvionGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Avion avionNovi) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajAvionGrupi(korisnickoIme, korisnickaLozinka, avionNovi);
    }

    public static java.lang.Boolean dodajAerodromGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Aerodrom serodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajAerodromGrupi(korisnickoIme, korisnickaLozinka, serodrom);
    }

    public static java.lang.Boolean dodajNoviAerodromGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom, java.lang.String nazivAerodrom, java.lang.String drzavaAerodrom, java.lang.String latAerodrom, java.lang.String lonAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajNoviAerodromGrupi(korisnickoIme, korisnickaLozinka, idAerodrom, nazivAerodrom, drzavaAerodrom, latAerodrom, lonAerodrom);
    }

    public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Avion> dajSveAvioneAerodromaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.dajSveAvioneAerodromaGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static java.lang.Boolean obrisiSveAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.obrisiSveAerodromeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static org.foi.nwtis.dkermek.ws.serveri.AerodromStatus dajStatusAerodromaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.dajStatusAerodromaGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static boolean postaviAvioneGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<org.foi.nwtis.dkermek.ws.serveri.Avion> avioniNovi) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.postaviAvioneGrupe(korisnickoIme, korisnickaLozinka, avioniNovi);
    }

    public static java.lang.Boolean autenticirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.autenticirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Aerodrom> dajSveAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.dajSveAerodromeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean aktivirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean blokirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static boolean aktivirajAerodromGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.aktivirajAerodromGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static boolean blokirajAerodromGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.blokirajAerodromGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static boolean obrisiAerodromGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();

        return port.obrisiAerodromGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static boolean aktivirajOdabraneAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.String> odabraniAerodromi) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.aktivirajOdabraneAerodromeGrupe(korisnickoIme, korisnickaLozinka, odabraniAerodromi);
    }

    public static boolean blokirajOdabraneAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.String> odabranaAerodromi) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.blokirajOdabraneAerodromeGrupe(korisnickoIme, korisnickaLozinka, odabranaAerodromi);
    }

    public static boolean obrisiOdabraneAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.String> odabraniAerodromi) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.obrisiOdabraneAerodromeGrupe(korisnickoIme, korisnickaLozinka, odabraniAerodromi);
    }

    public static int dajBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.dajBrojPoruka(korisnickoIme, korisnickaLozinka);
    }

    public static int dajTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        return port.dajTrajanjeCiklusa(korisnickoIme, korisnickaLozinka);
    }

    public static void promjeniBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int brojPoruka) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        port.promjeniBrojPoruka(korisnickoIme, korisnickaLozinka, brojPoruka);
    }

    public static void promjeniTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int trajanjeCiklusa) {
        AerodromiWS_Service service = new AerodromiWS_Service();
        AerodromiWS port = service.getAerodromiWSPort();
        port.promjeniTrajanjeCiklusa(korisnickoIme, korisnickaLozinka, trajanjeCiklusa);
    }

}
