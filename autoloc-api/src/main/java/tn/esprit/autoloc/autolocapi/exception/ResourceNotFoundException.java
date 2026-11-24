package tn.esprit.autoloc.autolocapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " introuvable avec l'identifiant " + id);
    }
}
