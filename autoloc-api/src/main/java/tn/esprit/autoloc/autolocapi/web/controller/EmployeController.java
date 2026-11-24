package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.service.IEmployeService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/employes")
@Tag(name = "Employés")
public class EmployeController extends CrudController<Employe> {
    public EmployeController(IEmployeService service) { super(service); }
}
