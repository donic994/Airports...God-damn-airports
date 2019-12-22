package org.foi.nwtis.filgatari.web.dretve;

/**
 * Klasa koja izvršava potrebne akcije za komande poslužitelja.
 *
 * @author filip
 */
public class AkcijePosluzitelj {

    
    /**
     * Pauzira zaprimanje komandi posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String pauza() {
        if (ServerSustava.serverKomandePokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_PAUZA_ERR;
        }

        ServerSustava.serverKomandePokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_PAUZA_OK;
    }

    /**
     * Pokrece zaprimanje komandi posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String kreni() {
        if (ServerSustava.serverKomandePokrenut == true) {
            return OdgovoriKomandi.POSLUZITELJ_KRENI_ERR;
        }

        ServerSustava.serverKomandePokrenut = true;
        return OdgovoriKomandi.POSLUZITELJ_KRENI_OK;
    }

    /**
     * Pauzira dretvu za pokretanje aerodroma.
     *
     * @return odgovor propisane strukture
     */
    public static String pasivno() {
        if (ServerSustava.serverAvioniAktivan == false) {
            return OdgovoriKomandi.POSLUZITELJ_PASIVNO_ERR;
        }

        ServerSustava.serverAvioniAktivan = false;
        return OdgovoriKomandi.POSLUZITELJ_PASIVNO_OK;
    }

    /**
     * Ponovno pokrece dretvu za pokretanje aerodroma.
     *
     * @return odgovor propisane strukture
     */
    public static String aktivno() {
        if (ServerSustava.serverAvioniAktivan == true) {
            return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_ERR;
        }

        ServerSustava.serverAvioniAktivan = true;
        return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_OK;
    }

    /**
     * Potpuno zaustavlja rad posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String stani() {
        if (ServerSustava.serverPotpunoPokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_STANI_ERR;
        }

        ServerSustava.serverPotpunoPokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_STANI_OK;
    }

    /**
     * Vraca trenutno stanje posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String stanje() {
        String odgovor = "";
        if (ServerSustava.serverKomandePokrenut && ServerSustava.serverAvioniAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK;
        } else if (ServerSustava.serverKomandePokrenut && !ServerSustava.serverAvioniAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK2;
        } else if (!ServerSustava.serverKomandePokrenut && ServerSustava.serverAvioniAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK3;
        } else if (!ServerSustava.serverKomandePokrenut && !ServerSustava.serverAvioniAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK4;
        }

        return odgovor;
    }
}
