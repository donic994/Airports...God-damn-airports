package org.foi.nwtis.filgatari.web.podaci;

import org.foi.nwtis.rest.podaci.Lokacija;

public class Aerodrom {
    private String icao;
    private String naziv;
    private String drzava;
    private Lokacija lokacija;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     */
    public Aerodrom() {
    }

    /**
     *
     * @param icao
     * @param naziv
     * @param drzava
     * @param lokacija
     */
    public Aerodrom(String icao, String naziv, String drzava, Lokacija lokacija) {
        this.icao = icao;
        this.naziv = naziv;
        this.drzava = drzava;
        this.lokacija = lokacija;
    }

    /**
     *
     * @return
     */
    public Lokacija getLokacija() {
        return lokacija;
    }

    /**
     *
     * @param lokacija
     */
    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    /**
     *
     * @return
     */
    public String getIcao() {
        return icao;
    }

    /**
     *
     * @param icao
     */
    public void setIcao(String icao) {
        this.icao = icao;
    }

    /**
     *
     * @return
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     *
     * @param naziv
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     *
     * @return
     */
    public String getDrzava() {
        return drzava;
    }

    /**
     *
     * @param drzava
     */
    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }
    
}