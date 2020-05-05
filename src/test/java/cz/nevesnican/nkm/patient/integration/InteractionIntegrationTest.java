package cz.nevesnican.nkm.patient.integration;

import com.google.common.io.Resources;
import cz.nevesnican.nkm.patient.entity.Interaction;
import cz.nevesnican.nkm.patient.entity.Patient;
import cz.nevesnican.nkm.patient.service.repository.InteractionRepositoryService;
import cz.nevesnican.nkm.patient.service.repository.PatientRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ServiceMockConfiguration.class)
@AutoConfigureWireMock(port = 0)
public class InteractionIntegrationTest {
    @Autowired
    private InteractionRepositoryService interactionService;
    @Autowired
    private PatientRepositoryService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        assertTrue(patientService.getPatientCount() > 0);
        testPatient = patientService.getPatients(1, 0).get(0);
    }

    @Test
    @Transactional
    void suggestPrescriptionTest() throws IOException {
        List<Long> diagnosis = Arrays.asList(1L, 2L);
        Interaction interaction = new Interaction();
        interaction.setDoctor(1L);
        interaction.setPatient(testPatient);
        interaction.setDiagnosis(diagnosis);

        String expectedResponse = Resources.toString(Resources.getResource("suggest_prescription_response.json"), StandardCharsets.UTF_8);

        stubFor(
                get(
                        urlPathMatching("/drug/suggestPrescription/(\\d+,)*\\d"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(expectedResponse)
                        )
        );

        Long interactionId = interactionService.addInteraction(interaction, "");
        interaction = interactionService.getInteraction(interactionId);

        assertEquals(diagnosis.size(), interaction.getDiagnosis().size());
        for (Long diseaseId : diagnosis) {
            assertTrue(interaction.getDiagnosis().contains(diseaseId));
        }

        List<Long> expectedDrugs = Arrays.asList(2L, 3L, 4L, 5L);
        assertEquals(expectedDrugs.size(), interaction.getPrescriptions().size());
        for (Long drugId : expectedDrugs) {
            assertTrue(interaction.getPrescriptions().contains(drugId));
        }
    }

    @Test
    @Transactional
    void diagnoseTest() throws IOException {
        List<Long> symptoms = Arrays.asList(5L, 6L);
        Interaction interaction = new Interaction();
        interaction.setDoctor(1L);
        interaction.setPatient(testPatient);
        interaction.setPrescriptions(Arrays.asList(0L));
        interaction.setSymptoms(symptoms);

        String expectedResponse = Resources.toString(Resources.getResource("diagnose_response.json"), StandardCharsets.UTF_8);

        stubFor(
                post(
                        urlPathEqualTo("/disease/diagnose/"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(expectedResponse)
                        )
        );

        Long interactionId = interactionService.addInteraction(interaction, "");
        interaction = interactionService.getInteraction(interactionId);

        assertEquals(symptoms.size(), interaction.getSymptoms().size());
        for (Long symptomId : symptoms) {
            assertTrue(interaction.getSymptoms().contains(symptomId));
        }

        List<Long> expectedDiseases = Arrays.asList(1L, 2L);
        assertEquals(expectedDiseases.size(), interaction.getDiagnosis().size());
        for (Long diseaseId : expectedDiseases) {
            assertTrue(interaction.getDiagnosis().contains(diseaseId));
        }
    }

    @Test
    @Transactional
    void completeAddInteractionTest() throws IOException {
        List<Long> symptoms = Arrays.asList(5L, 6L);
        Interaction interaction = new Interaction();
        interaction.setDoctor(1L);
        interaction.setPatient(testPatient);
        interaction.setSymptoms(symptoms);

        String expectedDiagnoseResponse = Resources.toString(Resources.getResource("diagnose_response.json"), StandardCharsets.UTF_8);
        String expectedSuggestPrescriptionResponse = Resources.toString(Resources.getResource("suggest_prescription_response.json"), StandardCharsets.UTF_8);

        stubFor(
                post(
                        urlPathEqualTo("/disease/diagnose/"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(expectedDiagnoseResponse)
                        )
        );
        stubFor(
                get(
                        urlPathMatching("/drug/suggestPrescription/(\\d+,)*\\d"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(expectedSuggestPrescriptionResponse)
                        )
        );

        Long interactionId = interactionService.addInteraction(interaction, "");
        interaction = interactionService.getInteraction(interactionId);

        assertEquals(symptoms.size(), interaction.getSymptoms().size());
        for (Long symptomId : symptoms) {
            assertTrue(interaction.getSymptoms().contains(symptomId));
        }

        List<Long> expectedDiseases = Arrays.asList(1L, 2L);
        assertEquals(expectedDiseases.size(), interaction.getDiagnosis().size());
        for (Long diseaseId : expectedDiseases) {
            assertTrue(interaction.getDiagnosis().contains(diseaseId));
        }

        List<Long> expectedDrugs = Arrays.asList(2L, 3L, 4L, 5L);
        assertEquals(expectedDrugs.size(), interaction.getPrescriptions().size());
        for (Long drugId : expectedDrugs) {
            assertTrue(interaction.getPrescriptions().contains(drugId));
        }
    }
}
