package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.exception.InvalidRequestException;
import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    private final static Logger LOG = Logger.getLogger(InteractionController.class.getName());

    private final InteractionRepositoryService interactionService;

    @ApiOperation("Returns an interaction record")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Interaction getInteraction(@PathVariable @ApiParam(value = "interaction id", required = true, example = "1") Long id) {
        LOG.info("serving getInteraction");

        if (id == null) {
            throw new InvalidRequestException();
        }

        return interactionService.getInteraction(id);
    }

    @ApiOperation("Creates an interaction record")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long addInteraction(@RequestBody @ApiParam(value = "interaction", required = true) Interaction i) {
        LOG.info("serving addInteraction");

        if (i == null || i.getId() != null) {
            throw new InvalidRequestException();
        }

        return interactionService.addInteraction(i);
    }

    @ApiOperation("Edits an interaction")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void editInteraction(@RequestBody @ApiParam(value = "interaction", required = true) Interaction i) {
        LOG.info("serving editInteraction");

        if (i == null || i.getId() == null) {
            throw new InvalidRequestException();
        }

        interactionService.updateInteraction(i);
    }

    @ApiOperation("Deletes an interaction")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteInteraction(@PathVariable @ApiParam(value = "interaction id", required = true, example = "1") Long id) {
        LOG.info("serving deleteInteraction");

        if (id == null) {
            throw new InvalidRequestException();
        }

        interactionService.deleteInteraction(id);
    }

    @ApiOperation("Returns a number of interactions done by a specified doctor")
    @RequestMapping(value="/doctorInteractionCount", method = RequestMethod.GET)
    public Long getDoctorInteractionCount(@RequestParam @ApiParam(value = "doctor id", required = true, example = "1") Long doctorId) {
        LOG.info("serving getDoctorInteractionCount");

        if (doctorId == null) {
            throw new InvalidRequestException();
        }

        return interactionService.getDoctorInteractionCount(doctorId);
    }

    @ApiOperation("Returns a number of times a specified disease has been diagnosed")
    @RequestMapping(value="/diseaseDiagnoseCount", method = RequestMethod.GET)
    public Long getDiagnoseCount(@RequestParam @ApiParam(value = "disease id", required = true, example = "1") Long diseaseId) {
        LOG.info("serving getDiagnoseCount");
        if (diseaseId == null) {
            throw new InvalidRequestException();
        }

        return interactionService.getDiseaseDiagnoseCount(diseaseId);
    }

    @Autowired
    public InteractionController(InteractionRepositoryService interactionService) {
        this.interactionService = interactionService;
    }
}
