package tn.esprit.autoloc.autolocapi.io;

import org.springframework.stereotype.Component;

@Component
public class RapportGeneratorFactory {
    private final RapportGeneratorDecorator decorator;
    public RapportGeneratorFactory(RapportGeneratorDecorator decorator) { this.decorator = decorator; }
    public RapportGenerator create() { return decorator; }
}
