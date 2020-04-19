package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.InteractionDAO;
import cz.nevesnican.nkm.patient.dao.PatientDAO;
import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import cz.nevesnican.nkm.patient.exception.NotAuthorizedException;
import cz.nevesnican.nkm.patient.external.client.DiseaseClient;
import cz.nevesnican.nkm.patient.external.client.DrugClient;
import cz.nevesnican.nkm.patient.external.client.PersonnelClient;
import cz.nevesnican.nkm.patient.external.model.Disease;
import cz.nevesnican.nkm.patient.external.model.Symptom;
import cz.nevesnican.nkm.patient.external.model.SymptomsDTO;
import cz.nevesnican.nkm.patient.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InteractionRepositoryService {
    private final DiseaseClient diseaseClient;
    private final DrugClient drugClient;
    private final InteractionDAO interactionDAO;
    private final PatientDAO patientDAO;

    private void diagnose(Interaction i) {
        // todo improve
        SymptomsDTO symptoms = new SymptomsDTO();
        symptoms.setSymptoms(new ArrayList<>());
        for (Long symptomId : i.getSymptoms()) {
            Symptom s = new Symptom();
            s.setId(symptomId);
            symptoms.getSymptoms().add(s);
        }

        List<Disease> diagnosis = diseaseClient.diagnose(symptoms);

        if (!diagnosis.isEmpty()) {
            i.getDiagnosis().clear();
            for (Disease d : diagnosis) {
                i.getDiagnosis().add(d.getId());
            }
        }
    }

    private void prescribeDrugs(Interaction i) {
        // todo
        //i.getPrescriptions().clear();
    }

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
        diagnose(i);
        prescribeDrugs(i);

        interactionDAO.add(i);
        return i.getId();
    }

    @Transactional
    public void updateInteraction(Interaction i) {
        diagnose(i);
        prescribeDrugs(i);

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
    public InteractionRepositoryService(DiseaseClient diseaseClient,
                                        DrugClient drugClient,
                                        InteractionDAO interactionDAO,
                                        PatientDAO patientDAO) {
        this.drugClient = drugClient;
        this.diseaseClient = diseaseClient;
        this.interactionDAO = interactionDAO;
        this.patientDAO = patientDAO;
    }
}
