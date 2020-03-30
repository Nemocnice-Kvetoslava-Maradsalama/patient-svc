package cz.nevesnican.nkm.patient.dto;

import cz.nevesnican.nkm.patient.entity.Interaction;

import java.io.Serializable;
import java.util.List;

public class InteractionListDTO implements Serializable {

    private final Interaction interaction;

    public Long getId() {
        return interaction.getId();
    }

    public String getNote() {
        return interaction.getNote();
    }

    public List<Long> getDiagnosis() {
        return interaction.getDiagnosis();
    }

    public List<Long> getSymptoms() {
        return interaction.getSymptoms();
    }

    public List<Long> getPrescriptions() {
        return interaction.getPrescriptions();
    }

    public Long getDoctor() {
        return interaction.getDoctor();
    }

    public InteractionListDTO(Interaction interaction) {
        this.interaction = interaction;
    }
}
