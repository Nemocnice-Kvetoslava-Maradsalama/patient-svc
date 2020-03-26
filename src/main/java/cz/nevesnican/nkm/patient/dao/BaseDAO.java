package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.NKMPatientEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseDAO<T extends NKMPatientEntity, I> implements DAO<T, I> {
    private final static Logger LOG = Logger.getLogger(BaseDAO.class.getName());

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    @Override
    public T getById(I id) {
        return id == null ? null : em.find(type, id);
    }

    @Override
    @Transactional
    public void add(T item) {
        if (item == null) {
            LOG.warning("Attempt was made to persist a null item");
            throw new UnsupportedOperationException();
        }

        em.persist(item);
    }

    @Override
    @Transactional
    public void update(T item) {
        if (item == null) {
            LOG.warning("Attempt was made to update a null item");
            throw new UnsupportedOperationException();
        }

        em.merge(item);
    }

    @Override
    @Transactional
    public void remove(T item) {
        if (item == null) {
            LOG.warning("Attempt was made to delete a null item");
            throw new UnsupportedOperationException();
        }

        em.remove(item);
    }

    @Override
    public List<T> getAll() {
        return em.createQuery("SELECT t FROM " + type.getSimpleName() + " t", type).getResultList();
    }

    public BaseDAO(Class<T> type) {
        this.type = type;
    }
}
