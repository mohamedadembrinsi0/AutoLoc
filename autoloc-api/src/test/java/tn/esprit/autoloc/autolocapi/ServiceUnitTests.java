package tn.esprit.autoloc.autolocapi;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.calcul.StandardStrategy;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.domain.*;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.io.RapportGenerator;
import tn.esprit.autoloc.autolocapi.io.RapportGeneratorFactory;
import tn.esprit.autoloc.autolocapi.repository.*;
import tn.esprit.autoloc.autolocapi.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceUnitTests {

    @Test
    void genericCrudServiceCreatesReadsUpdatesAndDeletesClient() {
        IClientRepository repository = mock(IClientRepository.class);
        ClientService service = new ClientService(repository);
        Client client = validClient();
        when(repository.save(any(Client.class))).thenAnswer(invocation -> {
            Client saved = invocation.getArgument(0);
            saved.setIdClient(11L);
            return saved;
        });

        Client created = service.create(client);
        assertThat(created.getIdClient()).isEqualTo(11L);
        assertThat(service.check(created, false)).isTrue();
        when(repository.findById(11L)).thenReturn(Optional.of(created));
        assertThat(service.findById(11L)).isSameAs(created);
        when(repository.findAll()).thenReturn(List.of(created));
        assertThat(service.findAll()).containsExactly(created);

        Client changes = validClient();
        changes.setNom("Nom modifié");
        assertThat(service.update(11L, changes).getNom()).isEqualTo("Nom modifié");

        when(repository.existsById(11L)).thenReturn(true);
        service.deleteById(11L);
        verify(repository).deleteById(11L);
        verify(repository).flush();
    }

    @Test
    void genericCrudOperationsWorkForEachCrudService() {
        IAgenceRepository agenceRepository = mock(IAgenceRepository.class);
        assertCrudContract(new AgenceServiceImpl(agenceRepository, mock(IEmployeRepository.class),
                mock(IVehiculeService.class), mock(RapportGeneratorFactory.class)), agenceRepository,
                validAgence(), entity -> entity.setIdAgence(1L));
        IAgenceRepository basicAgenceRepository = mock(IAgenceRepository.class);
        assertCrudContract(new BasicAgenceServiceImpl(basicAgenceRepository, mock(IEmployeRepository.class),
                mock(IVehiculeService.class), mock(RapportGeneratorFactory.class)), basicAgenceRepository,
                validAgence(), entity -> entity.setIdAgence(1L));

        IClientRepository clientRepository = mock(IClientRepository.class);
        assertCrudContract(new ClientService(clientRepository), clientRepository, validClient(),
                entity -> entity.setIdClient(1L));
        IContratRepository contratRepository = mock(IContratRepository.class);
        assertCrudContract(new ContratService(contratRepository), contratRepository, validContrat(),
                entity -> entity.setIdContrat(1L));
        IEmployeRepository employeRepository = mock(IEmployeRepository.class);
        assertCrudContract(new EmployeService(employeRepository), employeRepository, validEmploye(),
                entity -> entity.setIdEmploye(1L));
        IEquipementRepository equipementRepository = mock(IEquipementRepository.class);
        assertCrudContract(new EquipementService(equipementRepository), equipementRepository, validEquipement(),
                entity -> entity.setIdEquipement(1L));
        IMaintenanceRepository maintenanceRepository = mock(IMaintenanceRepository.class);
        assertCrudContract(new MaintenanceService(maintenanceRepository), maintenanceRepository, validMaintenance(),
                entity -> entity.setIdMaintenance(1L));
        IReservationRepository reservationRepository = mock(IReservationRepository.class);
        assertCrudContract(new ReservationService(reservationRepository), reservationRepository, validReservation(),
                entity -> entity.setIdReservation(1L));
        IVehiculeRepository vehiculeRepository = mock(IVehiculeRepository.class);
        assertCrudContract(new VehiculeService(vehiculeRepository, mock(IAgenceRepository.class),
                mock(IEquipementRepository.class), List.of(new StandardStrategy())), vehiculeRepository,
                validVehicule(), entity -> entity.setIdVehicule(1L));
    }

    @Test
    void agenceServiceHandlesEmployeeBatchAndVehiculeLookup() {
        IAgenceRepository agenceRepository = mock(IAgenceRepository.class);
        IEmployeRepository employeRepository = mock(IEmployeRepository.class);
        AgenceServiceImpl service = new AgenceServiceImpl(agenceRepository, employeRepository,
                mock(IVehiculeService.class), mock(RapportGeneratorFactory.class));
        Agence agence = validAgence();
        agence.setIdAgence(5L);
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(agence));
        when(agenceRepository.hasVehicules(5L)).thenReturn(true);
        when(agenceRepository.save(any(Agence.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.hasVehicules(5L)).isTrue();
        Employe employe = validEmploye();
        Agence result = service.addEmployes(5L, validAgence(), List.of(employe));
        assertThat(result.getEmployes()).contains(employe);
        assertThat(result.getEffectif()).isEqualTo(1);
        verify(employeRepository).save(employe);
        assertThatThrownBy(() -> service.addEmployes(5L, validAgence(), List.of()))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void agenceSimulationUsesVehicleServiceAndReportGenerator() {
        IAgenceRepository agenceRepository = mock(IAgenceRepository.class);
        IVehiculeService vehiculeService = mock(IVehiculeService.class);
        RapportGeneratorFactory factory = mock(RapportGeneratorFactory.class);
        RapportGenerator generator = mock(RapportGenerator.class);
        AgenceServiceImpl service = new AgenceServiceImpl(agenceRepository, mock(IEmployeRepository.class),
                vehiculeService, factory);
        Agence agence = validAgence();
        Vehicule vehicule = validVehicule();
        vehicule.setIdVehicule(19L);
        agence.addVehicule(vehicule);
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(agence));
        when(vehiculeService.calculTarif(19L, 2, VehiculeOffre.STANDARD)).thenReturn(25.0);
        when(factory.create()).thenReturn(generator);
        when(generator.generer(agence, 2, VehiculeOffre.STANDARD, new BigDecimal("25.0"))).thenReturn("rapport");

        assertThat(service.simuler(5L, 2, VehiculeOffre.STANDARD)).isEqualTo("rapport");
        assertThatThrownBy(() -> service.simuler(5L, 0, VehiculeOffre.STANDARD))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void vehiculeServiceCalculatesTarifAndManagesAssignments() {
        IVehiculeRepository vehiculeRepository = mock(IVehiculeRepository.class);
        IAgenceRepository agenceRepository = mock(IAgenceRepository.class);
        IEquipementRepository equipementRepository = mock(IEquipementRepository.class);
        VehiculeService service = new VehiculeService(vehiculeRepository, agenceRepository, equipementRepository,
                List.of(new StandardStrategy()));
        Vehicule vehicule = validVehicule();
        vehicule.setIdVehicule(7L);
        Agence agence = validAgence();
        Equipement equipement = validEquipement();
        when(vehiculeRepository.findById(7L)).thenReturn(Optional.of(vehicule));
        when(vehiculeRepository.save(any(Vehicule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(agenceRepository.findById(3L)).thenReturn(Optional.of(agence));
        when(equipementRepository.findById(4L)).thenReturn(Optional.of(equipement));

        assertThat(service.calculTarif(7L, 2, VehiculeOffre.STANDARD)).isEqualTo(250.0);
        assertThat(service.affecterAgence(7L, 3L).getAgence()).isSameAs(agence);
        assertThat(service.affecterEquipement(7L, 4L).getEquipements()).contains(equipement);
        assertThat(service.desaffecterAgence(7L).getAgence()).isNull();
        assertThatThrownBy(() -> service.calculTarif(7L, 0, VehiculeOffre.STANDARD))
                .isInstanceOf(BadRequestException.class);
        when(vehiculeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.desaffecterAgence(99L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void paiementServiceIsReadOnlyAndProvidesLookup() {
        IPaiementRepository repository = mock(IPaiementRepository.class);
        PaiementService service = new PaiementService(repository);
        Paiement paiement = new Paiement();
        when(repository.findById(2L)).thenReturn(Optional.of(paiement));
        when(repository.findAll()).thenReturn(List.of(paiement));

        assertThat(service.check(paiement, true)).isTrue();
        assertThat(service.findById(2L)).isSameAs(paiement);
        assertThat(service.findAll()).containsExactly(paiement);
        assertThatThrownBy(() -> service.create(paiement)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> service.update(2L, paiement)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> service.deleteById(2L)).isInstanceOf(UnsupportedOperationException.class);
        when(repository.findById(404L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(404L)).isInstanceOf(ResourceNotFoundException.class);
    }

    private static Client validClient() {
        Client client = new Client();
        client.setNom("Client"); client.setPrenom("Test"); client.setEmail("client@example.com");
        client.setTelephone("71112233"); client.setNumPermis("PERMIS-1"); client.setDateInscription(LocalDate.now());
        return client;
    }

    private static <T> void assertCrudContract(IGenericService<T, Long> service,
                                                JpaRepository<T, Long> repository,
                                                T entity, Consumer<T> assignId) {
        when(repository.save(any())).thenAnswer(invocation -> {
            T saved = invocation.getArgument(0);
            assignId.accept(saved);
            return saved;
        });
        T created = service.create(entity);
        assertThat(service.check(created, false)).isTrue();
        when(repository.findById(1L)).thenReturn(Optional.of(created));
        assertThat(service.findById(1L)).isSameAs(created);
        when(repository.findAll()).thenReturn(List.of(created));
        assertThat(service.findAll()).containsExactly(created);
        assertThat(service.update(1L, entity)).isSameAs(created);
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteById(1L);
        verify(repository).deleteById(1L);
        verify(repository).flush();
    }

    private static Agence validAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence"); agence.setVille("Tunis"); agence.setAdresse("1 rue Test"); agence.setTelephone("71112233");
        return agence;
    }

    private static Employe validEmploye() {
        Employe employe = new Employe(); employe.setNom("Employe"); employe.setPrenom("Test"); employe.setRole(RoleEmploye.AGENT);
        return employe;
    }

    private static Equipement validEquipement() {
        Equipement equipement = new Equipement(); equipement.setLibelle("GPS"); return equipement;
    }

    private static Vehicule validVehicule() {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123-TUN-1"); vehicule.setMarque("Toyota"); vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE); vehicule.setTarifJournalier(new BigDecimal("125"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        return vehicule;
    }

    private static Reservation validReservation() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 1, 1)); reservation.setDateFin(LocalDate.of(2026, 1, 2));
        reservation.setStatut(StatutReservation.EN_ATTENTE); reservation.setVehicule(validVehicule());
        reservation.setClient(validClient()); return reservation;
    }

    private static Contrat validContrat() {
        Contrat contrat = new Contrat(); contrat.setDateSignature(LocalDate.of(2026, 1, 1));
        contrat.setMontantTotal(BigDecimal.TEN); contrat.setReservation(validReservation()); return contrat;
    }

    private static Maintenance validMaintenance() {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.of(2026, 1, 1)); maintenance.setDateFin(LocalDate.of(2026, 1, 2));
        maintenance.setDescription("Entretien"); maintenance.setVehicule(validVehicule()); return maintenance;
    }
}
