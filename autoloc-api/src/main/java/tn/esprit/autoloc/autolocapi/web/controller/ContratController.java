package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.service.IContratService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contrats")
@Tag(name = "Contrats")
public class ContratController extends CrudController<Contrat> {
    public ContratController(IContratService service) { super(service); }
}
