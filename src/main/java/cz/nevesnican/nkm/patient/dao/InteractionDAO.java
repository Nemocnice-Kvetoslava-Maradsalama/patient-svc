package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.Interaction;
import org.springframework.stereotype.Repository;

@Repository
public class InteractionDAO extends BaseDAO<Interaction, Long> {
    public InteractionDAO() {
        super(Interaction.class);
    }
}
