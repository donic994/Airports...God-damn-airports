package org.foi.nwtis.filgatari.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author filip
 */
public class AerodromiRESTKlijentIdAviona {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/filgatari_aplikacija_1/webresources/";

    public AerodromiRESTKlijentIdAviona(String id) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}/avion", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String id) {
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}/avion", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnickoIme header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T getJsonAviona(Class<T> responseType, String korisnickoIme, String lozinka) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnickoIme", korisnickoIme).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
