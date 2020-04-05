package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class InteractionDAO extends BaseDAO<Interaction, Long> {

    public List<Interaction> getPatientInteractions(Patient p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Interaction> q = cb.createQuery(type);
        Root<Interaction> r = q.from(type);

        q.select(r).where(cb.equal(r.get("patient"), p));

        return em.createQuery(q).getResultList();
    }

    @Override
    protected String getDefaultOrdering() {
        return "id ASC";
    }

    public InteractionDAO() {
        super(Interaction.class);
    }
}
