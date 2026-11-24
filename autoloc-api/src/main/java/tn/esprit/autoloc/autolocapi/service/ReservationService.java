package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.repository.IReservationRepository;

@Service
public class ReservationService extends AbstractCrudService<Reservation> implements IReservationService {
    public ReservationService(IReservationRepository repository) { super(repository, Reservation.class, "RÃ©servation"); }
}
