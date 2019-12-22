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
public class WSKlijent {

    public static MeteoPodaci dajVazeceMeteoPodatke(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajVazeceMeteoPodatke(arg0, arg1, arg2);
    }

    public static java.util.List<org.foi.nwtis.filgatari.ws.klijenti.AvionLeti> dajAvioneSAerodromaZaPeriodTimestamp(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajAvioneSAerodromaZaPeriodTimestamp(arg0, arg1, arg2, arg3, arg4);
    }

    private static java.util.List<org.foi.nwtis.filgatari.ws.klijenti.AvionLeti> dajAvioneZaPeriodTimestamp(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajAvioneZaPeriodTimestamp(arg0, arg1, arg2, arg3, arg4);
    }

    public static java.util.List<org.foi.nwtis.filgatari.ws.klijenti.AvionLeti> dajAvioneZaPeriodTimestamp_1(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajAvioneZaPeriodTimestamp(arg0, arg1, arg2, arg3, arg4);
    }

    public static double dajUdaljenostAerodroma(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) {
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service service = new org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS_Service();
        org.foi.nwtis.filgatari.ws.klijenti.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajUdaljenostAerodroma(arg0, arg1, arg2, arg3);
    }
    
    
    
}
