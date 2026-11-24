package tn.esprit.autoloc.autolocapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.domain.*;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.repository.*;
import tn.esprit.autoloc.autolocapi.service.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Disabled("Tests d'anciens ateliers, remplacés par ServiceUnitTests")
class WorkshopFeatureTests {
    @Autowired private MockMvc mockMvc;
    @Autowired private IAgenceService agenceService;
    @Autowired private IVehiculeService vehiculeService;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IEmployeRepository employeRepository;

    @Test
    void offerStrategiesCalculateExpectedTotals() {
        Agence agence = agence("Calcul");
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TC" + (System.nanoTime() % 100000000000000000L));
        vehicule.setMarque("Toyota"); vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("1000.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);

        assertThat(vehiculeService.calculTarif(vehicule.getIdVehicule(), 3, VehiculeOffre.STANDARD)).isEqualTo(3000.0);
        assertThat(vehiculeService.calculTarif(vehicule.getIdVehicule(), 3, VehiculeOffre.WEEKEND)).isEqualTo(2400.0);
        assertThat(vehiculeService.calculTarif(vehicule.getIdVehicule(), 3, VehiculeOffre.LONGUE_DUREE)).isEqualTo(2137.5);
        assertThat(agenceService.simuler(agence.getIdAgence(), 3, VehiculeOffre.STANDARD)).contains("Total : 3000.00");
    }

    @Test
    void employeeBatchRollsBackOnInvalidEmployee() {
        Agence agence = agence("Rollback");
        Agence changes = agence("Updated");
        Employe valid = employee("Valid");
        Employe invalid = employee(" ");
        List<Employe> employes = List.of(valid, invalid);
        Long agenceId = agence.getIdAgence();

        assertThatThrownBy(() -> agenceService.addEmployes(agenceId, changes, employes))
                .isInstanceOf(BadRequestException.class);
        assertThat(employeRepository.findAll()).isEmpty();
        assertThat(agenceRepository.findById(agence.getIdAgence()).orElseThrow().getNom()).isEqualTo("Rollback");
    }

    @Test
    void agencyRestRoutesReturnExpectedStatuses() throws Exception {
        mockMvc.perform(get("/agences")).andExpect(status().isOk());
        mockMvc.perform(get("/agences/999999")).andExpect(status().isNotFound());
        mockMvc.perform(post("/agences").contentType("application/json")
                .content("{\"nom\":\"API Test\",\"ville\":\"Tunis\",\"adresse\":\"1 rue API\",\"telephone\":\"71234567\"}"))
                .andExpect(status().isCreated());
    }

    private Agence agence(String name) {
        Agence agence = new Agence(); agence.setNom(name); agence.setVille("Tunis");
        agence.setAdresse("1 rue du Test"); agence.setTelephone("71112233");
        return agenceService.create(agence);
    }

    private Employe employee(String name) {
        Employe employe = new Employe(); employe.setNom(name); employe.setPrenom("Test");
        employe.setRole(RoleEmploye.AGENT); return employe;
    }
}
