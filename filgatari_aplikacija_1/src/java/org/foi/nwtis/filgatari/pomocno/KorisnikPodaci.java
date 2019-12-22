/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.pomocno;

/**
 * Klasa za pohranu korisničkog imena i lozinke u objekt.
 * @author filip
 */
public class KorisnikPodaci {

    private String korisnickoIme;
    private String lozinka;

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     */
    public KorisnikPodaci(String korisnickoIme, String lozinka) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    /**
     *
     * @return
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     *
     * @return
     */
    public String getLozinka() {
        return lozinka;
    }
}