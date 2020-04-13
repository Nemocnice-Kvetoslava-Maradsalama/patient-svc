package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.InteractionDAO;
import cz.nevesnican.nkm.patient.dao.PatientDAO;
import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InteractionRepositoryService {
    private final InteractionDAO interactionDAO;
    private final PatientDAO patientDAO;

    @Transactional
    public List<Interaction> getPatientInteractions(Long patientId) {
        Patient p = patientDAO.getReference(patientId);

        if (p == null) {
            throw new EntityNotFoundException(patientId);
        }

        return interactionDAO.getPatientInteractions(p);
    }

    @Transactional
    public Interaction getInteraction(Long id) {
        Interaction i = interactionDAO.getById(id);

        if (i == null) {
            throw new EntityNotFoundException(id);
        }

        return i;
    }

    @Transactional
    public Long addInteraction(Interaction i) {
        interactionDAO.add(i);
        return i.getId();
    }

    @Transactional
    public void updateInteraction(Interaction i) {
        interactionDAO.update(i);
    }

    @Transactional
    public void deleteInteraction(Long id) {
        Interaction i = interactionDAO.getReference(id);

        if (i == null) {
            throw new EntityNotFoundException(id);
        }

        interactionDAO.remove(i);
    }

    @Transactional
    public Long getDoctorInteractionCount(Long doctorId) {
        return interactionDAO.getDoctorInteractionCount(doctorId);
    }

    @Transactional
    public Long getDiseaseDiagnoseCount(Long diseaseId) {
        return interactionDAO.getDiseaseDiagnoseCount(diseaseId);
    }

    @Autowired
    public InteractionRepositoryService(InteractionDAO interactionDAO, PatientDAO patientDAO) {
        this.interactionDAO = interactionDAO;
        this.patientDAO = patientDAO;
    }
}
