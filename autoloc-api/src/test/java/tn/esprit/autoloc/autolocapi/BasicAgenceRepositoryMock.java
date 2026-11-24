package tn.esprit.autoloc.autolocapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.autolocapi.domain.Agence;

@Repository
interface BasicAgenceRepositoryMock extends CrudRepository<Agence, Long> { }
