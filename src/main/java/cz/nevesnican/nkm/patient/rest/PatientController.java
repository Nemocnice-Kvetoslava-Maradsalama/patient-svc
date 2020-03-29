package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.exception.InvalidRequestException;
import cz.nevesnican.nkm.patient.service.repository.PatientRepositoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientRepositoryService patientService;

    @ApiOperation("Returns a list of patients. If either one of limit or offset parameters is null, a list of all patients is returned.")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Patient> getPatients(@RequestParam(required = false) @ApiParam(value = "How many result to include (empty for all results)", example = "10") Integer limit,
                                     @RequestParam(required = false) @ApiParam(value = "How many results to skip (empty for all results)", example = "0") Integer offset) {
        if (limit != null && offset != null) {
            return patientService.getPatients(limit, offset);
        }

        return patientService.getPatients();
    }

    @ApiOperation("Returns a patient")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Patient getPatient(@PathVariable @ApiParam(value = "patient id", required = true, example = "1") Long id) {
        if (id == null) {
            throw new InvalidRequestException();
        }

        return patientService.getPatient(id);
    }

    @ApiOperation("Creates a patient")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long createPatient(@RequestBody @ApiParam(value = "patient", required = true) Patient p) {
        if (p == null || p.getId() != null) {
            throw new InvalidRequestException();
        }

        return patientService.createPatient(p);
    }

    @ApiOperation("Edits a patient")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void editPatient(@RequestBody @ApiParam(value = "patient", required = true) Patient p) {
        if (p == null || p.getId() == null) {
            throw new InvalidRequestException();
        }

        patientService.updatePatient(p);
    }

    @ApiOperation("Deletes a patient")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void editPatient(@PathVariable @ApiParam(value = "patient id", required = true, example = "1") Long id) {
        if (id == null) {
            throw new InvalidRequestException();
        }

        patientService.deletePatient(id);
    }

    @Autowired
    public PatientController(PatientRepositoryService patientService) {
        this.patientService = patientService;
    }
}
