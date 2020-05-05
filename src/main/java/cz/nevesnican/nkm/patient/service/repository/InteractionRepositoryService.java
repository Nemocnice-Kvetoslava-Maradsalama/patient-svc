package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.InteractionDAO;
import cz.nevesnican.nkm.patient.dao.PatientDAO;
import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import cz.nevesnican.nkm.patient.exception.NotAuthorizedException;
import cz.nevesnican.nkm.patient.external.client.DiseaseClient;
import cz.nevesnican.nkm.patient.external.client.DrugClient;
import cz.nevesnican.nkm.patient.external.model.Disease;
import cz.nevesnican.nkm.patient.external.model.Symptom;
import cz.nevesnican.nkm.patient.external.model.SymptomsDTO;
import cz.nevesnican.nkm.patient.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class InteractionRepositoryService {
    private final AuthorizationService authorizationService;
    private final DiseaseClient diseaseClient;
    private final DrugClient drugClient;
    private final InteractionDAO interactionDAO;
    private final PatientDAO patientDAO;

    private void diagnose(Interaction i, String token) {
        if (i.getDiagnosis().isEmpty()) {
            SymptomsDTO symptoms = new SymptomsDTO();
            List<Symptom> symptomList = symptoms.getSymptoms();

            for (Long symptomId : i.getSymptoms()) {
                Symptom s = new Symptom(symptomId);
                symptomList.add(s);
            }

            List<Disease> diagnosis = diseaseClient.diagnose(symptoms, token);
            List<Long> diseaseList = i.getDiagnosis();

            for (Disease d : diagnosis) {
                diseaseList.add(d.getId());
            }
        }
    }

    private void prescribeDrugs(Interaction i, String token) {
        if (i.getPrescriptions().isEmpty() && !i.getDiagnosis().isEmpty()) {
            StringJoiner diseaseList = new StringJoiner(",");

            for (Long diseaseId : i.getDiagnosis()) {
                diseaseList.add(diseaseId.toString());
            }

            Map<String, List<Long>> prescriptions = drugClient.suggestPrescription(diseaseList.toString(), token);
            List<Long> interactionPrescriptions = i.getPrescriptions();

            prescriptions.values().forEach(dp -> dp.forEach(d -> {
                if (!interactionPrescriptions.contains(d)) {
                    interactionPrescriptions.add(d);
                }
            }));
        }
    }

    private void handleInteraction(Interaction i, String token) {
        if (!authorizationService.validateDoctor(token)) {
            throw new NotAuthorizedException();
        }

        diagnose(i, token);
        prescribeDrugs(i, token);
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
    public Long addInteraction(Interaction i, String token) {
        handleInteraction(i, token);
        interactionDAO.add(i);
        return i.getId();
    }

    @Transactional
    public void updateInteraction(Interaction i, String token) {
        handleInteraction(i, token);
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
    public InteractionRepositoryService(AuthorizationService authorizationService,
                                        DiseaseClient diseaseClient,
                                        DrugClient drugClient,
                                        InteractionDAO interactionDAO,
                                        PatientDAO patientDAO) {
        this.authorizationService = authorizationService;
        this.drugClient = drugClient;
        this.diseaseClient = diseaseClient;
        this.interactionDAO = interactionDAO;
        this.patientDAO = patientDAO;
    }
}
