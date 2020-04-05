package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.NKMPatientEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public void add(T item) {
        if (item == null) {
            LOG.warning("Attempt was made to persist a null item");
            throw new UnsupportedOperationException();
        }

        em.persist(item);
    }

    @Override
    public void update(T item) {
        if (item == null) {
            LOG.warning("Attempt was made to update a null item");
            throw new UnsupportedOperationException();
        }

        em.merge(item);
    }

    @Override
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

    @Override
    public List<T> getCountItems(int limit, int offset) {
        Query q = em.createQuery("SELECT t FROM " + type.getSimpleName() + " t", type);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long itemCount() {
        return (long) em.createQuery("SELECT COUNT(t.id) FROM " + type.getSimpleName() + " t").getSingleResult();
    }

    @Override
    public T getReference(I id) {
        return id == null ? null : em.getReference(type, id);
    }

    public BaseDAO(Class<T> type) {
        this.type = type;
    }
}
