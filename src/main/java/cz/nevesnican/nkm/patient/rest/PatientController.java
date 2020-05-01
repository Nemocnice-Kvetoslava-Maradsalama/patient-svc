package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.dto.InteractionListDTO;
import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.InvalidRequestException;
import cz.nevesnican.nkm.patient.exception.NotAuthorizedException;
import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import cz.nevesnican.nkm.patient.service.repository.PatientRepositoryService;
import cz.nevesnican.nkm.patient.service.security.AuthorizationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private final static Logger LOG = Logger.getLogger(PatientController.class.getName());

    private final AuthorizationService authorizationService;
    private final PatientRepositoryService patientService;
    private final InteractionRepositoryService interactionService;

    @ApiOperation("Returns a list of patients. If either one of limit or offset parameters is null, a list of all patients is returned.")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Patient> getPatients(@RequestParam(required = false) @ApiParam(value = "How many result to include (empty for all results)", example = "10") Integer limit,
                                     @RequestParam(required = false) @ApiParam(value = "How many results to skip (empty for all results)", example = "0") Integer offset,
                                     @RequestHeader(value="Authorization") String token) {
        LOG.info("serving getPatients");

        if (limit != null && offset != null) {
            return patientService.getPatients(limit, offset);
        }

        validateToken(token);

        return patientService.getPatients();
    }

    @ApiOperation("Returns a patient")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Patient getPatient(@PathVariable @ApiParam(value = "patient id", required = true, example = "1") Long id, @RequestHeader(value="Authorization") String token) {
        LOG.info("serving getPatient");

        if (id == null) {
            throw new InvalidRequestException();
        }

        validateToken(token);

        return patientService.getPatient(id);
    }

    @ApiOperation("Creates a patient")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Long> createPatient(@RequestBody @ApiParam(value = "patient", required = true) Patient p, @RequestHeader(value="Authorization") String token) {
        LOG.info("serving createPatient");

        if (p == null || p.getId() != null) {
            throw new InvalidRequestException();
        }

        validateToken(token);

        return new ResponseEntity<>(patientService.createPatient(p), HttpStatus.CREATED);
    }

    @ApiOperation("Edits a patient")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<Void> editPatient(@RequestBody @ApiParam(value = "patient", required = true) Patient p, @RequestHeader(value="Authorization") String token) {
        LOG.info("serving editPatient");

        if (p == null || p.getId() == null) {
            throw new InvalidRequestException();
        }

        validateToken(token);

        patientService.updatePatient(p);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @ApiOperation("Deletes a patient")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePatient(@PathVariable @ApiParam(value = "patient id", required = true, example = "1") Long id, @RequestHeader(value="Authorization") String token) {
        LOG.info("serving deletePatient");

        if (id == null) {
            throw new InvalidRequestException();
        }

        validateToken(token);

        patientService.deletePatient(id);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @ApiOperation("Returns a list of patient interactions")
    @RequestMapping(value="{id}/interactions", method = RequestMethod.GET)
    public List<InteractionListDTO> getPatientInteractions(@PathVariable @ApiParam(value = "patient id", required = true, example = "1") Long id, @RequestHeader(value="Authorization") String token) {
        LOG.info("serving getPatientInteractions");

        if (id == null) {
            throw new InvalidRequestException();
        }

        validateToken(token);

        List<Interaction> interactions = interactionService.getPatientInteractions(id);
        List<InteractionListDTO> interactionsDTO = new ArrayList<>(interactions.size());

        interactions.forEach(i -> interactionsDTO.add(new InteractionListDTO(i)));

        return interactionsDTO;
    }

    private void validateToken(String token) {
        if (!authorizationService.validateUser(token)) {
            throw new NotAuthorizedException();
        }
    }

    @Autowired
    public PatientController(AuthorizationService authorizationService,
                             PatientRepositoryService patientService,
                             InteractionRepositoryService interactionService) {
        this.authorizationService = authorizationService;
        this.patientService = patientService;
        this.interactionService = interactionService;
    }
}
