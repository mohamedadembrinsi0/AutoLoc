package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.service.IMaintenanceService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/maintenances")
@Tag(name = "Maintenances")
public class MaintenanceController extends CrudController<Maintenance> {
    public MaintenanceController(IMaintenanceService service) { super(service); }
}
