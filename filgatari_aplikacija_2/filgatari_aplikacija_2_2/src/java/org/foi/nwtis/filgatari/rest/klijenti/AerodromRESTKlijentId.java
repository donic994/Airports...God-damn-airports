package org.foi.nwtis.filgatari.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author filip
 */
public class AerodromRESTKlijentId {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/filgatari_aplikacija_1/webresources/";

    public AerodromRESTKlijentId(String id) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String id) {
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnickoIme header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T deleteJson(Class<T> responseType, String korisnickoIme, String lozinka) throws ClientErrorException {
        return webTarget.request().header("korisnickoIme", korisnickoIme).header("lozinka", lozinka).delete(responseType);
    }

    /**
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)
     */
    public <T> T postJson(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    /**
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)@param korisnickoIme header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T putJson(Object requestEntity, Class<T> responseType, String korisnickoIme, String lozinka) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnickoIme", korisnickoIme).header("lozinka", lozinka).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnickoIme header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T getJson(Class<T> responseType, String korisnickoIme, String lozinka) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnickoIme", korisnickoIme).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
