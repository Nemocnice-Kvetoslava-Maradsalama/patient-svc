package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import cz.nevesnican.nkm.patient.external.client.DiseaseClient;
import cz.nevesnican.nkm.patient.external.model.Disease;
import cz.nevesnican.nkm.patient.external.model.Symptom;
import cz.nevesnican.nkm.patient.external.model.SymptomsDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.List;

import static cz.nevesnican.nkm.patient.service.repository.TestUtil.createTestInteraction;
import static cz.nevesnican.nkm.patient.service.repository.TestUtil.createTestPatient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InteractionRepositoryServiceTest {
    @Autowired
    private InteractionRepositoryService service;
    @Autowired
    private PatientRepositoryService patientService;

    private static Long testPatient1ID;
    private static Long testPatient2ID;

    private Patient testPatient1;
    private Patient testPatient2;

    //@Configuration
    //@EnableAutoConfiguration
    /*static class ContextConfiguration {
        @Bean
        public DiseaseClient getDc() {
            return new DiseaseClient() {
                @Override
                public List<Disease> diagnose(SymptomsDTO symptoms) {
                    return Collections.emptyList();
                }

                @Override
                public Disease getDisease(Long id) {
                    return new Disease();
                }

                @Override
                public Symptom getSymptom(Long id) {
                    return new Symptom();
                }
            };
        }
    }*/

    @BeforeEach
    void setUp() {
        synchronized (InteractionRepositoryServiceTest.class) {
            if (testPatient1ID == null) {
                Patient p1 = createTestPatient();
                testPatient1ID = patientService.createPatient(p1);
            }

            if (testPatient2ID == null) {
                Patient p2 = createTestPatient();
                testPatient2ID = patientService.createPatient(p2);
            }
        }

        testPatient1 = patientService.getPatient(testPatient1ID);
        testPatient2 = patientService.getPatient(testPatient2ID);
    }

    @Test
    void testCreateAndGetInteraction() {
        int initialCount = service.getPatientInteractions(testPatient1.getId()).size();

        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i, "");
        Interaction _i = service.getInteraction(id);

        assertEquals(i.getNote(), _i.getNote());
        assertEquals(initialCount+1, service.getPatientInteractions(testPatient1.getId()).size());
    }

    @Test
    void testGetPatientsInteractions() {
        List<Interaction> p1Interactions = service.getPatientInteractions(testPatient1.getId());
        List<Interaction> p2Interactions = service.getPatientInteractions(testPatient2.getId());
        int initialCountP1 = p1Interactions.size();
        int initialCountP2 = p2Interactions.size();

        Interaction i1 = createTestInteraction(testPatient1);
        Interaction i2 = createTestInteraction(testPatient1);
        Interaction i3 = createTestInteraction(testPatient1);
        Interaction i4 = createTestInteraction(testPatient2);
        Interaction i5 = createTestInteraction(testPatient2);

        service.addInteraction(i1, "");
        service.addInteraction(i2, "");
        service.addInteraction(i3, "");
        service.addInteraction(i4, "");
        service.addInteraction(i5, "");

        p1Interactions = service.getPatientInteractions(testPatient1.getId());
        p2Interactions = service.getPatientInteractions(testPatient2.getId());

        assertEquals(initialCountP1+3, p1Interactions.size());
        assertEquals(initialCountP2+2, p2Interactions.size());
    }

    @Test
    void testDeleteInteraction() {
        int initialCount = service.getPatientInteractions(testPatient1.getId()).size();

        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i, "");

        assertEquals(i.getNote(), service.getInteraction(id).getNote());
        assertEquals(initialCount+1, service.getPatientInteractions(testPatient1.getId()).size());

        service.deleteInteraction(id);
        assertEquals(initialCount, service.getPatientInteractions(testPatient1.getId()).size());
        assertThrows(EntityNotFoundException.class, () -> service.getInteraction(id));
    }

    @Test
    void testUpdateInteraction() {
        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i, "");
        assertEquals(i.getNote(), service.getInteraction(id).getNote());

        Interaction _i = createTestInteraction(testPatient1);
        _i.setNote("test update");
        _i.setId(id);

        service.updateInteraction(_i, "");
        assertEquals("test update", service.getInteraction(id).getNote());
    }

    @Test
    void testDoctorInteractionCount() {
        Long testDoctor1 = 5L;
        Long testDoctor2 = 6L;
        Long initialCount1 = service.getDoctorInteractionCount(testDoctor1);
        Long initialCount2 = service.getDoctorInteractionCount(testDoctor2);

        Interaction i1 = createTestInteraction(testPatient1);
        Interaction i2 = createTestInteraction(testPatient1);
        Interaction i3 = createTestInteraction(testPatient2);
        i1.setDoctor(testDoctor1);
        i2.setDoctor(testDoctor1);
        i3.setDoctor(testDoctor2);
        Long id1 = service.addInteraction(i1, "");
        Long id2 = service.addInteraction(i2, "");
        Long id3 = service.addInteraction(i3, "");

        assertEquals(initialCount1+2, service.getDoctorInteractionCount(testDoctor1));
        assertEquals(initialCount2+1, service.getDoctorInteractionCount(testDoctor2));

        service.deleteInteraction(id1);

        assertEquals(initialCount1+1, service.getDoctorInteractionCount(testDoctor1));
        assertEquals(initialCount2+1, service.getDoctorInteractionCount(testDoctor2));
    }

    @Test
    void testDiseaseDiagnoseCount() {
        Long testDisease1 = 5L;
        Long testDisease2 = 6L;
        Long initialCount1 = service.getDiseaseDiagnoseCount(testDisease1);
        Long initialCount2 = service.getDiseaseDiagnoseCount(testDisease2);

        Interaction i1 = createTestInteraction(testPatient1);
        Interaction i2 = createTestInteraction(testPatient1);
        Interaction i3 = createTestInteraction(testPatient2);
        Interaction i4 = createTestInteraction(testPatient2);
        i1.getDiagnosis().add(testDisease1);
        i2.getDiagnosis().add(testDisease2);
        i3.getDiagnosis().add(testDisease2);
        i4.getDiagnosis().add(testDisease1);
        i4.getDiagnosis().add(testDisease2);
        Long id1 = service.addInteraction(i1, "");
        Long id2 = service.addInteraction(i2, "");
        Long id3 = service.addInteraction(i3, "");
        Long id4 = service.addInteraction(i4, "");

        assertEquals(initialCount1+2, service.getDiseaseDiagnoseCount(testDisease1));
        assertEquals(initialCount2+3, service.getDiseaseDiagnoseCount(testDisease2));

        service.deleteInteraction(id1);
        service.deleteInteraction(id4);

        assertEquals(initialCount1, service.getDiseaseDiagnoseCount(testDisease1));
        assertEquals(initialCount2+2, service.getDiseaseDiagnoseCount(testDisease2));
    }
}
