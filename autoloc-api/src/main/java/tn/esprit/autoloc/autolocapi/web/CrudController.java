package tn.esprit.autoloc.autolocapi.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.service.IGenericService;

import java.util.List;

public abstract class CrudController<T> {
    private final IGenericService<T, Long> service;
    protected CrudController(IGenericService<T, Long> service) { this.service = service; }

    @GetMapping
    public List<T> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public T findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        if (!service.check(entity, true)) throw new BadRequestException("Données invalides");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public T update(@PathVariable Long id, @Valid @RequestBody T entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
