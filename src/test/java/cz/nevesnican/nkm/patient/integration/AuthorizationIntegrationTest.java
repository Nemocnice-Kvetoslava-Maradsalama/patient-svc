package cz.nevesnican.nkm.patient.integration;

import cz.nevesnican.nkm.patient.service.security.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Field;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ServiceMockConfiguration.class)
@AutoConfigureWireMock(port = 0)
public class AuthorizationIntegrationTest {
    @Autowired
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Class<?> authServiceClass = AuthorizationService.class;
        Field requireUserAuth = authServiceClass.getDeclaredField("requireUserAuth");
        Field requireDoctorAuth = authServiceClass.getDeclaredField("requireDoctorAuth");

        requireUserAuth.setAccessible(true);
        requireDoctorAuth.setAccessible(true);

        requireUserAuth.setBoolean(authorizationService, true);
        requireDoctorAuth.setBoolean(authorizationService, true);
    }

    @Test
    void testValidateValidUser() {
        stubFor(
                get(
                        urlPathEqualTo("/auth/validate-token"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("true")
                        )
        );

        assertTrue(authorizationService.validateUser("token"));
    }

    @Test
    void testValidateInvalidUser() {
        stubFor(
                get(
                        urlPathEqualTo("/auth/validate-token"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("false")
                        )
        );

        assertFalse(authorizationService.validateUser("token"));
    }

    @Test
    void testValidateValidDoctor() {
        stubFor(
                get(
                        urlPathEqualTo("/auth/validate-doctor"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("true")
                        )
        );

        assertTrue(authorizationService.validateDoctor("token"));
    }

    @Test
    void testValidateInvalidDoctor() {
        stubFor(
                get(
                        urlPathEqualTo("/auth/validate-doctor"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("false")
                        )
        );

        assertFalse(authorizationService.validateDoctor("token"));
    }
}
