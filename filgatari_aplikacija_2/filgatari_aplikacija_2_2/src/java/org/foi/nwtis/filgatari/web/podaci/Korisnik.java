package org.foi.nwtis.filgatari.web.podaci;

import java.util.Date;

/**
 * Klasa za rad s korisnicima iz baze podataka.
 *
 * @author filip
 */
public class Korisnik {

    private int id;
    private String kor_ime;
    private String ime;
    private String prezime;
    private String lozinka;
    private String email_adresa;
    private Date datum_pohrane;
    private int vrsta;

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getKor_ime() {
        return kor_ime;
    }

    /**
     *
     * @param kor_ime
     */
    public void setKor_ime(String kor_ime) {
        this.kor_ime = kor_ime;
    }

    /**
     *
     * @return
     */
    public String getIme() {
        return ime;
    }

    /**
     *
     * @param ime
     */
    public void setIme(String ime) {
        this.ime = ime;
    }

    /**
     *
     * @return
     */
    public String getPrezime() {
        return prezime;
    }

    /**
     *
     * @param prezime
     */
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    /**
     *
     * @return
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     *
     * @param lozinka
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     *
     * @return
     */
    public String getEmail_adresa() {
        return email_adresa;
    }

    /**
     *
     * @param email_adresa
     */
    public void setEmail_adresa(String email_adresa) {
        this.email_adresa = email_adresa;
    }

    /**
     *
     * @return
     */
    public Date getDatum_pohrane() {
        return datum_pohrane;
    }

    /**
     *
     * @param datum_promjene
     */
    public void setDatum_pohrane(Date datum_pohrane) {
        this.datum_pohrane = datum_pohrane;
    }

}
