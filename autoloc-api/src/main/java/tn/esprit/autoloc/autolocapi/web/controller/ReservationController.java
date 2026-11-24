package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.service.IReservationService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Réservations")
public class ReservationController extends CrudController<Reservation> {
    public ReservationController(IReservationService service) { super(service); }
}
