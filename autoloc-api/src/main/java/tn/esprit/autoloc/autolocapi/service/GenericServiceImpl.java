package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;

import java.util.List;

public abstract class GenericServiceImpl<T, I> implements IGenericService<T, I> {
    private final JpaRepository<T, I> repository;

    protected GenericServiceImpl(JpaRepository<T, I> repository) {
        this.repository = repository;
    }

    protected JpaRepository<T, I> repository() { return repository; }
    protected abstract String getEntityName();
    protected abstract I getId(T entity);
    protected abstract void setFields(T target, T source);

    @Override
    public boolean check(T entity, boolean isNew) {
        return entity != null && (isNew ? getId(entity) == null : getId(entity) != null);
    }

    @Override
    @Transactional
    public T create(T entity) {
        if (!check(entity, true)) throw new BadRequestException("Données invalides pour " + getEntityName());
        return repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(I id) {
        return loadEntity(id);
    }

    protected T loadEntity(I id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(getEntityName(), String.valueOf(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() { return repository.findAll(); }

    @Override
    @Transactional
    public T update(I id, T entity) {
        return updateEntity(id, entity);
    }

    protected T updateEntity(I id, T entity) {
        T existing = loadEntity(id);
        if (entity == null) throw new BadRequestException("Données invalides pour " + getEntityName());
        setFields(existing, entity);
        if (!check(existing, false)) throw new BadRequestException("Données invalides pour " + getEntityName());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(I id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException(getEntityName(), String.valueOf(id));
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (EntityNotFoundException | org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BadRequestException("Suppression impossible pour " + getEntityName() + " " + id);
        }
    }
}
