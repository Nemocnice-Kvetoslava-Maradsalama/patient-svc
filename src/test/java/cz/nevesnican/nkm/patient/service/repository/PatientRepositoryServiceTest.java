package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cz.nevesnican.nkm.patient.service.repository.TestUtil.createTestPatient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PatientRepositoryServiceTest {
    @Autowired
    private PatientRepositoryService service;

    @Test
    void testItemCount() {
        long initialCount = service.getPatientCount();
        int initialListCount = service.getPatients().size();
        assertEquals(initialCount, initialListCount);

        Patient p = createTestPatient();
        Long id = service.createPatient(p);

        assertEquals(initialCount+1, service.getPatientCount());
        assertEquals(service.getPatientCount(), service.getPatients().size());

        service.deletePatient(id);

        assertEquals(initialCount, service.getPatientCount());
        assertEquals(service.getPatientCount(), service.getPatients().size());
    }

    @Test
    void testCreateAndGetListOfPatients() {
        int initialSize = service.getPatients().size();

        Patient p1 = createTestPatient();
        Patient p2 = createTestPatient();

        Long id1 = service.createPatient(p1);
        Long id2 = service.createPatient(p2);

        assertEquals(initialSize + 2, service.getPatients().size());

        Patient _p1 = service.getPatient(id1);
        assertEquals(_p1.getLastName(), p1.getLastName());

        Patient _p2 = service.getPatient(id2);
        assertEquals(_p2.getLastName(), p2.getLastName());
    }

    @Test
    void testGetCountPatients() {
        int initialCount = (int) service.getPatientCount();

        Patient p1 = createTestPatient();
        Patient p2 = createTestPatient();
        Patient p3 = createTestPatient();

        service.createPatient(p1);
        service.createPatient(p2);
        service.createPatient(p3);

        List<Patient> firstPatient = service.getPatients(1, initialCount);
        assertEquals(1, firstPatient.size());
        assertEquals(firstPatient.get(0).getLastName(), p1.getLastName());

        List<Patient> secondAndThirdPatient = service.getPatients(2, 1+initialCount);
        assertEquals(2, secondAndThirdPatient.size());
        assertEquals(secondAndThirdPatient.get(0).getLastName(), p2.getLastName());
        assertEquals(secondAndThirdPatient.get(1).getLastName(), p3.getLastName());
    }

    @Test
    void testDeletePatient() {
        int initialCount = (int) service.getPatientCount();

        Patient p1 = createTestPatient();
        Long id = service.createPatient(p1);

        assertEquals(p1.getLastName(), service.getPatient(id).getLastName());
        assertEquals(initialCount+1, service.getPatientCount());

        service.deletePatient(id);
        assertEquals(initialCount, service.getPatientCount());
        assertThrows(EntityNotFoundException.class, () -> service.getPatient(id));
    }

    @Test
    void testUpdatePatient() {
        Patient p = createTestPatient();
        Long id = service.createPatient(p);
        assertEquals(p.getLastName(), service.getPatient(id).getLastName());

        Patient _p = createTestPatient(p.getFirstName(), "test update");
        _p.setId(id);

        service.updatePatient(_p);
        assertEquals("test update", service.getPatient(id).getLastName());
    }
}