package tn.esprit.autoloc.autolocapi.io;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import java.math.BigDecimal;

@Component
public class RapportGeneratorDecorator implements RapportGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RapportGeneratorDecorator.class);
    private final BasicRapportGenerator delegate;
    public RapportGeneratorDecorator(BasicRapportGenerator delegate) { this.delegate = delegate; }
    @Override public String generer(Agence agence, int jours, VehiculeOffre offre, BigDecimal total) {
        LOGGER.info("=== {} | {} ===", agence.getNom(), agence.getTelephone());
        return delegate.generer(agence, jours, offre, total);
    }
}
