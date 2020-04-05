package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.PatientDAO;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PatientRepositoryService {
    private final PatientDAO dao;

    public List<Patient> getPatients() {
        return dao.getAll();
    }

    public List<Patient> getPatients(int limit, int offset) {
        return dao.getCountItems(limit, offset);
    }

    public Patient getPatient(Long id) {
        Patient p = dao.getById(id);

        if (p == null) {
            throw new EntityNotFoundException(id);
        }

        return p;
    }

    @Transactional
    public Long createPatient(Patient p) {
        dao.add(p);
        return p.getId();
    }

    @Transactional
    public void updatePatient(Patient p) {
        dao.update(p);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient p = dao.getReference(id);

        if (p == null) {
            throw new EntityNotFoundException(id);
        }

        dao.remove(p);
    }

    @Autowired
    public PatientRepositoryService(PatientDAO dao) {
        this.dao = dao;
    }
}
