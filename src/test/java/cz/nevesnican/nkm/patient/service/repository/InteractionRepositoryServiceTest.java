package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cz.nevesnican.nkm.patient.service.repository.TestUtil.createTestInteraction;
import static cz.nevesnican.nkm.patient.service.repository.TestUtil.createTestPatient;
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {
        if (patientService.getPatientCount() == 0) {
            Patient p1 = createTestPatient();
            Patient p2 = createTestPatient();
            testPatient1ID = patientService.createPatient(p1);
            testPatient2ID = patientService.createPatient(p2);
        }

        testPatient1 = patientService.getPatient(testPatient1ID);
        testPatient2 = patientService.getPatient(testPatient2ID);
    }

    @Test
    void testCreateAndGetInteraction() {
        int initialCount = service.getPatientInteractions(testPatient1.getId()).size();

        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i);
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

        service.addInteraction(i1);
        service.addInteraction(i2);
        service.addInteraction(i3);
        service.addInteraction(i4);
        service.addInteraction(i5);

        p1Interactions = service.getPatientInteractions(testPatient1.getId());
        p2Interactions = service.getPatientInteractions(testPatient2.getId());

        assertEquals(initialCountP1+3, p1Interactions.size());
        assertEquals(initialCountP2+2, p2Interactions.size());
    }

    @Test
    void testDeleteInteraction() {
        int initialCount = service.getPatientInteractions(testPatient1.getId()).size();

        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i);

        assertEquals(i.getNote(), service.getInteraction(id).getNote());
        assertEquals(initialCount+1, service.getPatientInteractions(testPatient1.getId()).size());

        service.deleteInteraction(id);
        assertEquals(initialCount, service.getPatientInteractions(testPatient1.getId()).size());
        assertThrows(EntityNotFoundException.class, () -> service.getInteraction(id));
    }

    @Test
    void testUpdateInteraction() {
        Interaction i = createTestInteraction(testPatient1);
        Long id = service.addInteraction(i);
        assertEquals(i.getNote(), service.getInteraction(id).getNote());

        Interaction _i = createTestInteraction(testPatient1);
        _i.setNote("test update");
        _i.setId(id);

        service.updateInteraction(_i);
        assertEquals("test update", service.getInteraction(id).getNote());
    }
}
