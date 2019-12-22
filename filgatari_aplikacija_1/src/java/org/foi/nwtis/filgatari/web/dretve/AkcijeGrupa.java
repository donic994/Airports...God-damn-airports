package org.foi.nwtis.filgatari.web.dretve;

import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;
import static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika.AKTIVAN;
import static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika.BLOKIRAN;
import static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika.DEREGISTRIRAN;
import static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika.REGISTRIRAN;
import org.foi.nwtis.filgatari.pomocno.KorisnikPodaci;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.ws.klijenti.AerodromiWSDL;


/**
 * Klasa koja izvršava potrebne akcije za komande grupe.
 * @author filip
 */
public class AkcijeGrupa {

    /**
     * Registrira grupu putem servisa.
     * @return propisani odgovor.
     */
    public static String dodaj() {
        //samo ako je deregistrirana može se registrirati
        //moze se registrirati samo ako je deregistrirana
        if (dajStatusGrupe() != StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_DODAJ_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = AerodromiWSDL.registrirajGrupu(k.getKorisnickoIme(), k.getLozinka());        
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_DODAJ_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    /**
     * Deregistrira grupu putem servisa.
     * @return propisani odgovor.
     */
    public static String prekid() {
        //ako grupa još ne postoji greška, nije registrirana, samo ako je u statusu deregistrirana
        //u svim statusima osim deregistriran se moze deregistrirati
        if (dajStatusGrupe() == StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_PREKID_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = AerodromiWSDL.deregistrirajGrupu(k.getKorisnickoIme(), k.getLozinka());
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_PREKID_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    /**
     * Aktivira grupu putem servisa.
     * @return propisani odgovor.
     */
    public static String kreni() {
        StatusKorisnika status = dajStatusGrupe();
        
        //ako grupa još ne postoji greška
        if (status == DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR2;
        }
        //ako je grupa već aktivna greška
        else if (status == StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR;
        }         
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = AerodromiWSDL.aktivirajGrupu(k.getKorisnickoIme(), k.getLozinka());
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_KRENI_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    /**
     * Blokira grupu putem servisa.
     * @return propisani odgovor.
     */
    public static String pauza() {
        StatusKorisnika status = dajStatusGrupe();
        
        //ako grupa još ne postoji greška
        if (status == StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR2;
        } 
        //ako grupa još nije aktivna
        else if (status != StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR;
        }        
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = AerodromiWSDL.blokirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_PAUZA_OK;
    }

    /**
     * Vraca stanje grupe putem servisa.
     * @return propisani odgovor.
     */
    public static String stanje() {
        StatusKorisnika status = dajStatusGrupe();

        String odgovor = "";
        if (null != status) {
            switch (status) {
                case AKTIVAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK;
                    break;
                case BLOKIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK2;
                    break;
                case REGISTRIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK3;
                    break;
                case DEREGISTRIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_ERR;
                    break;
                default:
                    break;
            }
        }

        return odgovor;
    }

    /**
     * Pomocna metoda za dohvacanje statusa grupe putem servisa.
     * @return 
     */
    private static StatusKorisnika dajStatusGrupe() {
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        StatusKorisnika status = AerodromiWSDL.dajStatusGrupe(k.getKorisnickoIme(), k.getLozinka());

        return status;
    }
}
