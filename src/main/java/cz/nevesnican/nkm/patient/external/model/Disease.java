package cz.nevesnican.nkm.patient.external.model;

import java.util.List;

public class Disease {

    private Long id;
    private String name;
    private String icd10;
    private String description;
    private List<Symptom> symptoms;
    private List<Long> cures;

    public List<Long> getCures() {
        return cures;
    }

    public void setCures(List<Long> cures) {
        this.cures = cures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcd10() {
        return icd10;
    }

    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public Disease() {
    }

    public Disease(Long id) {
        this.id = id;
    }
}