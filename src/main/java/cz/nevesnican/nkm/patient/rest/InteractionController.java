package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    private final InteractionRepositoryService interactionService;



    @Autowired
    public InteractionController(InteractionRepositoryService interactionService) {
        this.interactionService = interactionService;
    }
}
