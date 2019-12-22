/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.ws.klijenti;

/**
 *
 * @author filip
 */
public class AerodromWSKlijent {

    

    public static boolean dodajKorisnika(org.foi.nwtis.filgatari.web.podaci.Korisnik arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajKorisnika(arg0, arg1, arg2);
    }

    public static boolean azurirajKorisnika(org.foi.nwtis.filgatari.web.podaci.Korisnik arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.azurirajKorisnika(arg0, arg1, arg2);
    }

    public static java.util.List<org.foi.nwtis.filgatari.ws.klijenti.Korisnik> dajSveKorisnike(java.lang.String arg0, java.lang.String arg1) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajSveKorisnike(arg0, arg1);
    }   
    
}
