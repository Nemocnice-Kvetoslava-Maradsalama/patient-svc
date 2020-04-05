package cz.nevesnican.nkm.patient.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Interaction implements NKMPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    private String note;

    @ElementCollection
    private List<Long> diagnosis = new ArrayList<>();

    @ElementCollection
    private List<Long> symptoms = new ArrayList<>();

    @ElementCollection
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaction that = (Interaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }

        return Objects.hash(id);
    }
}
