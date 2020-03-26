package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.NKMPatientEntity;

import java.util.List;

public interface DAO<T extends NKMPatientEntity, I> {
    T getById(I id);

    void add(T item);

    void update(T item);

    void remove(T item);

    List<T> getAll();
}
