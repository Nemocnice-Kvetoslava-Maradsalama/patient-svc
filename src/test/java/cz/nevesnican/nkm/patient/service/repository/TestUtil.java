package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtil {
    private final static Random random = ThreadLocalRandom.current();

    private static String randomString() {
        return Integer.toString(random.nextInt(500000)+10000);
    }

    public static Patient createTestPatient(String firstName, String lastName) {
        Patient p = new Patient();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        return p;
    }

    public static Patient createTestPatient() {
        String firstName = randomString();
        String lastName = randomString();
        return createTestPatient(firstName, lastName);
    }

    public static Interaction createTestInteraction(Patient p) {
        Interaction i = new Interaction();
        i.setPatient(p);
        i.setNote(randomString());
        i.setDoctor(1L);
        i.setPrescriptions(Collections.singletonList(0L));
        i.setDiagnosis(Collections.singletonList(0L));
        return i;
    }
}
