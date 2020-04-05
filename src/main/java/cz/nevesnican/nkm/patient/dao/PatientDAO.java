package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDAO extends BaseDAO<Patient, Long> {

    @Override
    protected String getDefaultOrdering() {
        return "id ASC";
    }

    public PatientDAO() {
        super(Patient.class);
    }
}
