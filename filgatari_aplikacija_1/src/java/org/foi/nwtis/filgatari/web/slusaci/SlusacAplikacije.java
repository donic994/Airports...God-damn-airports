/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.filgatari.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.filgatari.konfiguracije.Konfiguracija;
import org.foi.nwtis.filgatari.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.filgatari.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.filgatari.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.filgatari.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.filgatari.web.dretve.PreuzimanjeAviona;
import org.foi.nwtis.filgatari.web.dretve.ServerSustava;

/**
 *
 * @author filip
 */
public class SlusacAplikacije implements ServletContextListener {

    public static ServletContext servletContext;
    PreuzimanjeAviona dretvaPreuzimanjeAviona;
    ServerSustava dretvaServerSustava;

    /**
     * preuzima ServletContext i ucitava konfiguraciju
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteka = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + java.io.File.separator;
        String puniNazivDatoteke = putanja + datoteka;

        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(puniNazivDatoteke);
            sce.getServletContext().setAttribute("Konfig", konf);
            System.out.println("Konfiguracija uspjesno ucitana");
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        BP_Konfiguracija bpk;
        try {
            bpk = new BP_Konfiguracija(puniNazivDatoteke);
            sc.setAttribute("BP_Konfig", bpk);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        servletContext = sc;

        dretvaPreuzimanjeAviona = new PreuzimanjeAviona(sc);
        dretvaPreuzimanjeAviona.start();

        dretvaServerSustava = new ServerSustava(sc);
        dretvaServerSustava.start();
    }

    /**
     * Briše atribute iz konteksta i prekida dretve obrade.
     *
     * @param sce događaj konteksta servleta
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dretvaPreuzimanjeAviona != null) {
            dretvaPreuzimanjeAviona.interrupt();
        }
        if (dretvaServerSustava != null) {
            dretvaServerSustava.interrupt();
        }
        ServletContext sc = sce.getServletContext();
        sc.removeAttribute("BP_Konfig");
        sc.removeAttribute("Konfig");
    }
}
