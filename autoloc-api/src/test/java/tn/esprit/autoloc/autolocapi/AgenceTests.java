package tn.esprit.autoloc.autolocapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.service.IAgenceService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Tests d'un ancien atelier, remplacés par ServiceUnitTests")
class AgenceTests {
    @Autowired private IAgenceService agenceService;
    @Autowired private BasicAgenceRepositoryMock basicAgenceRepository;
    @Autowired private IAgenceRepository agenceRepository;

    @Test
    void createFindUpdateAndDeleteAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence de test");
        agence.setVille("Tunis");
        agence.setAdresse("1 rue des Tests");
        agence.setTelephone("71112233");

        Agence created = agenceService.create(agence);
        assertThat(created.getIdAgence()).isNotNull();
        assertThat(agenceService.findById(created.getIdAgence()).getNom()).isEqualTo("Agence de test");
        assertThat(agenceService.findAll()).extracting(Agence::getIdAgence).contains(created.getIdAgence());

        Agence changes = new Agence();
        changes.setNom("Agence modifiée");
        changes.setVille("Sfax");
        changes.setAdresse("2 rue des Tests");
        changes.setTelephone("74445566");
        assertThat(agenceService.update(created.getIdAgence(), changes).getVille()).isEqualTo("Sfax");
        assertThat(agenceService.hasVehicules(created.getIdAgence())).isFalse();

        agenceService.deleteById(created.getIdAgence());
        assertThat(agenceRepository.existsById(created.getIdAgence())).isFalse();
    }

    @Test
    void rejectsInvalidAgenceAndMissingIds() {
        Agence invalide = new Agence();
        invalide.setNom(" ");
        assertThat(agenceService.check(invalide, true)).isFalse();
        assertThat(agenceService.findAll()).isNotNull();
    }

    @Test
    void createsAgenceThroughSpringMockRepository() {
        Agence agence = new Agence();
        agence.setNom("Agence mock"); agence.setVille("Tunis");
        agence.setAdresse("2 rue Test"); agence.setTelephone("71110000");
        Agence saved = basicAgenceRepository.save(agence);
        assertThat(saved.getIdAgence()).isNotNull();
    }
}
