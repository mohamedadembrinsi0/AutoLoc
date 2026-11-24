package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.service.IClientService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients")
public class ClientController extends CrudController<Client> {
    public ClientController(IClientService service) { super(service); }
}
