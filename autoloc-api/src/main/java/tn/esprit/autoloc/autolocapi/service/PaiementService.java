package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IPaiementRepository;

import java.util.List;

@Service
public class PaiementService implements IPaiementService {
    private final IPaiementRepository repository;
    public PaiementService(IPaiementRepository repository) { this.repository = repository; }
    @Override public boolean check(Paiement entity, boolean isNew) { return entity != null; }
    @Override public Paiement create(Paiement entity) { throw new UnsupportedOperationException("Le paiement est en lecture seule"); }
    @Override public Paiement update(Long id, Paiement entity) { throw new UnsupportedOperationException("Le paiement est en lecture seule"); }
    @Override public void deleteById(Long id) { throw new UnsupportedOperationException("Le paiement est en lecture seule"); }
    @Override public Paiement findById(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paiement", String.valueOf(id))); }
    @Override public List<Paiement> findAll() { return repository.findAll(); }
}
