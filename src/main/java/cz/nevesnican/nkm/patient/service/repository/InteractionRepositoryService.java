package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.InteractionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractionRepositoryService {
    private final InteractionDAO dao;

    @Autowired
    public InteractionRepositoryService(InteractionDAO dao) {
        this.dao = dao;
    }
}
