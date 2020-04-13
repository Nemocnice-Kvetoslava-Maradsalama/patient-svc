package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class InteractionDAO extends BaseDAO<Interaction, Long> {

    public List<Interaction> getPatientInteractions(Patient p) {
        /*CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Interaction> q = cb.createQuery(type);
        Root<Interaction> r = q.from(type);

        q.select(r).where(cb.equal(r.get("patient"), p)).orderBy(cb.asc(r.get("id")));

        return em.createQuery(q).getResultList();*/
        Query q = em.createQuery("SELECT i FROM Interaction i WHERE i.patient=:patient");
        q.setParameter("patient", p);
        return (List<Interaction>) q.getResultList();
    }

    public Long getDoctorInteractionCount(Long doctorId) {
        Query q = em.createQuery("SELECT COUNT(i) FROM Interaction i WHERE i.doctor=:doctorId", Long.class);
        q.setParameter("doctorId", doctorId);
        return (Long) q.getSingleResult();
    }

    public Long getDiseaseDiagnoseCount(Long diseaseId) {
        List<Interaction> interactions = em.createQuery("SELECT i FROM Interaction i").getResultList();
        Long count = 0L;

        for (Interaction i : interactions) {
            if (i.getDiagnosis().contains(diseaseId)) {
                count++;
            }
        }

        return count;
    }

    @Override
    protected String getDefaultOrdering() {
        return "id ASC";
    }

    public InteractionDAO() {
        super(Interaction.class);
    }
}
