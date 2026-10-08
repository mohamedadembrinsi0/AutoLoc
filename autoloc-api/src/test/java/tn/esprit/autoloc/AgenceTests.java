package tn.esprit.autoloc;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*; import tn.esprit.autoloc.repository.AgenceRepositoryMock; import java.math.BigDecimal;
@SpringBootTest public class AgenceTests {
 @Autowired private AgenceRepositoryMock agenceRepository;
 @Test void addAgence(){
  Agence a=new Agence(); a.setAdresse("1 Rue Hedi"); a.setNom("Agence ariana"); a.setTelephone("71585874"); a.setVille("Tunis");
  Vehicule v1=new Vehicule(); v1.setImmatriculation("785414TU96"); v1.setMarque("Isuzu"); v1.setModele("DMax"); v1.setCategorie(CategorieVehicule.SUV); v1.setStatut(StatutVehicule.MAINTENANCE); v1.setTarifJournalier(new BigDecimal("100"));
  Vehicule v2=new Vehicule(); v2.setImmatriculation("785414TU95"); v2.setMarque("Toyota"); v2.setModele("Yaris"); v2.setCategorie(CategorieVehicule.UTILITAIRE); v2.setStatut(StatutVehicule.DISPONIBLE); v2.setTarifJournalier(new BigDecimal("80"));
  a.addVehicule(v1); a.addVehicule(v2); agenceRepository.save(a);
 }
}