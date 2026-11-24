package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.service.IPaiementService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@Tag(name = "Paiements (lecture seule)")
public class PaiementController {
    private final IPaiementService service;
    public PaiementController(IPaiementService service) { this.service = service; }
    @GetMapping public List<Paiement> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public Paiement findById(@PathVariable Long id) { return service.findById(id); }
}
