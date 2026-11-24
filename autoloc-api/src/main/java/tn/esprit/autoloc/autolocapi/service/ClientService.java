package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.repository.IClientRepository;

@Service
public class ClientService extends AbstractCrudService<Client> implements IClientService {
    public ClientService(IClientRepository repository) { super(repository, Client.class, "Client"); }
}
