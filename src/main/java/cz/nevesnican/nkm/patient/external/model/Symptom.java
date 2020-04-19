package cz.nevesnican.nkm.patient.external.model;

import java.util.List;

public class Symptom {

    private Long id;
    private String name;
    private String description;
    private List<Disease> diseases;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Disease> getDiseaseSymptoms() {
        return diseases;
    }

    public void setDiseaseSymptoms(List<Disease> diseaseSymptoms) {
        this.diseases = diseaseSymptoms;
    }
}
