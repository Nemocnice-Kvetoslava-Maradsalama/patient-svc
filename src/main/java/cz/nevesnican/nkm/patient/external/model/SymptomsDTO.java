package cz.nevesnican.nkm.patient.external.model;

import java.util.ArrayList;
import java.util.List;

public class SymptomsDTO {
    private List<Symptom> symptoms = new ArrayList<>();

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }
}
