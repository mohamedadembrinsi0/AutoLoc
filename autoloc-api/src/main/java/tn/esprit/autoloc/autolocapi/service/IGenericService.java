package tn.esprit.autoloc.autolocapi.service;

import java.util.List;

public interface IGenericService<T, I> {
    boolean check(T entity, boolean isNew);
    T create(T entity);
    T findById(I id);
    List<T> findAll();
    T update(I id, T entity);
    void deleteById(I id);
}
