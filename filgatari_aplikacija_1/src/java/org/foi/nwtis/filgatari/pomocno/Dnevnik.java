package org.foi.nwtis.filgatari.pomocno;

import java.util.Date;


/**
 * Klasa za rad s zapisima iz dnevnika rada.
 * @author filip
 */
public class Dnevnik {

    private String korisnik;
    private String url;
    private String ipAdresa = "";
    private int trajanje;
    private int status; 
    private Date spremljeno;

    private long pocetakRada;
    private long krajRada;

    /**
     *
     */
    public Dnevnik() {
        this.pocetakRada = System.currentTimeMillis();
        this.status = 0;
    }
    
    /**
     * Operacija u kojoj je dnevnik koristen je uspjesna.
     */
    public void postaviUspjesanStatus(){
        this.status = 1;
    }
    
    /**
     * Postavlja konacne vrijednosti zapisa za dnevnik.
     * @param korisnik
     * @param url 
     */
    public void zavrsiDnevnik(String korisnik, String url){
        this.ipAdresa = PomocnaKlasa.dajTrenutnuIPAdresu();        
        this.korisnik = korisnik;
        this.url = url;
        this.krajRada = System.currentTimeMillis();
        this.trajanje = (int) (this.krajRada - this.pocetakRada);
    }
    
    /**
     * Postavlja konacne vrijednosti zapisa za dnevnik i sprema u bazu podataka.
     * @param korisnik
     * @param url 
     */
    public void zavrsiISpremiDnevnik(String korisnik, String url){
        this.zavrsiDnevnik(korisnik, url);
        PomocnaKlasa.zapisiUDnevnik(this);
    }

    /**
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     *
     * @param korisnik
     */
    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public String getIpAdresa() {
        return ipAdresa;
    }

    /**
     *
     * @param ipAdresa
     */
    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    /**
     *
     * @return
     */
    public int getTrajanje() {
        return trajanje;
    }

    /**
     *
     * @param trajanje
     */
    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }
     
    /**
     *
     * @return
     */
    public Date getSpremljeno() {
        return spremljeno;
    }

    /**
     *
     * @param spremljeno
     */
    public void setSpremljeno(Date spremljeno) {
        this.spremljeno = spremljeno;
    }
    
}
