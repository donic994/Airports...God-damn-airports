/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.web.zrna;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.filgatari.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.filgatari.pomocno.PomocnaKlasa;
import org.foi.nwtis.filgatari.pomocno.Stranicenje;
import org.foi.nwtis.filgatari.web.podaci.Korisnik;

/**
 *
 * @author filip
 */
@Named(value = "pogled1")
@ManagedBean
@SessionScoped
public class Pogled1 implements Serializable{
    
    private String poruka;    
    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private Stranicenje stranicenje;

    /**
     * instanca Pogled1
     */
    public Pogled1() {
    }
    
    /**
     * Dohvaća popis korisnika.
     */
    @PostConstruct
    public void init() {
        preuzmiSveKorisnike();
        int brojZapisaPoStranici = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenje.pogled1"));
        stranicenje = new Stranicenje(listaKorisnika, brojZapisaPoStranici);       
    }
    
    /**
     * Preuzima listu korisnika iz baze podataka.
     */
    public void preuzmiSveKorisnike(){      
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            listaKorisnika = bpo.korisniciSelectSviKorisnici();
        } catch (SQLException | ClassNotFoundException ex) {
            poruka = "Greska prilikom dohvacanja korisnika: " + ex.getMessage();
        }     
    }
    
    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public Stranicenje getStranicenje() {
        return stranicenje;
    }

    /**
     *
     * @param stranicenje
     */
    public void setStranicenje(Stranicenje stranicenje) {
        this.stranicenje = stranicenje;
    }    
}
