package cz.nevesnican.nkm.patient.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Interaction implements NKMPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    private String note;

    private List<Long> diagnosis = new ArrayList<>();

    private List<Long> symptoms = new ArrayList<>();

    private List<Long> prescriptions = new ArrayList<>();

    @NotNull
    private Long doctor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Long> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Long> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Long> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Long> symptoms) {
        this.symptoms = symptoms;
    }

    public List<Long> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Long> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public Long getDoctor() {
        return doctor;
    }

    public void setDoctor(Long doctor) {
        this.doctor = doctor;
    }
}
