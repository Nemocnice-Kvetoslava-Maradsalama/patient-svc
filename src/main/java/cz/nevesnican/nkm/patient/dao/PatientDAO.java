package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDAO extends BaseDAO<Patient, Long> {
    public PatientDAO() {
        super(Patient.class);
    }
}
