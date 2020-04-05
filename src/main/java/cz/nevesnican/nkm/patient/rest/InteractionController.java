package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.exception.InvalidRequestException;
import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    private final InteractionRepositoryService interactionService;

    @ApiOperation("Returns an interaction record")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Interaction getInteraction(@PathVariable @ApiParam(value = "interaction id", required = true, example = "1") Long id) {
        if (id == null) {
            throw new InvalidRequestException();
        }

        return interactionService.getInteraction(id);
    }

    @ApiOperation("Creates an interaction record")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long addInteraction(@RequestBody @ApiParam(value = "interaction", required = true) Interaction i) {
        if (i == null || i.getId() != null) {
            throw new InvalidRequestException();
        }

        return interactionService.addInteraction(i);
    }

    @ApiOperation("Edits an interaction")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void editInteraction(@RequestBody @ApiParam(value = "interaction", required = true) Interaction i) {
        if (i == null || i.getId() == null) {
            throw new InvalidRequestException();
        }

        interactionService.updateInteraction(i);
    }

    @ApiOperation("Deletes an interaction")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteInteraction(@PathVariable @ApiParam(value = "interaction id", required = true, example = "1") Long id) {
        if (id == null) {
            throw new InvalidRequestException();
        }

        interactionService.deleteInteraction(id);
    }

    @Autowired
    public InteractionController(InteractionRepositoryService interactionService) {
        this.interactionService = interactionService;
    }
}
