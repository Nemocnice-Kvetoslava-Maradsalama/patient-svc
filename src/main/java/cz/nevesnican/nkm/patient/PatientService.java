package cz.nevesnican.nkm.patient;

import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import cz.nevesnican.nkm.patient.service.repository.PatientRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
public class PatientService {
	private final PatientRepositoryService patientService;
	private final InteractionRepositoryService interactionService;

	@PostConstruct
	private void createTestData() {
		if (patientService.getPatientCount() == 0L) {
			Patient p1 = new Patient();
			p1.setFirstName("Test");
			p1.setLastName("Patient 1");
			p1.setPersonalNumber("000001/0100");
			p1.setAddress("Salámová ulice 123");
			Long idp1 = patientService.createPatient(p1);
			p1 = patientService.getPatient(idp1);

			Patient p2 = new Patient();
			p2.setFirstName("Test");
			p2.setLastName("Patient 2");
			p2.setPersonalNumber("000002/0100");
			p2.setAddress("Zmrzlinová ulice 123");
			p2.setNote("note");
			Long idp2 = patientService.createPatient(p2);
			p2 = patientService.getPatient(idp2);

			Interaction i1 = new Interaction();
			i1.setDoctor(1L);
			i1.setNote("test interaction");
			i1.setPatient(p1);
			i1.setSymptoms(Arrays.asList(1L, 2L));
			i1.setPrescriptions(Collections.singletonList("1"));
			i1.setDiagnosis(Collections.singletonList(1L));
			interactionService.addInteraction(i1, "");
		}
	}

	@Autowired
	public PatientService(PatientRepositoryService patientService, InteractionRepositoryService interactionService) {
		this.patientService = patientService;
		this.interactionService = interactionService;
	}

	public static void main(String[] args) {
		SpringApplication.run(PatientService.class, args);
	}
}
