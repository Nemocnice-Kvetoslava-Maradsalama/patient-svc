package cz.nevesnican.nkm.patient.service.security;

import cz.nevesnican.nkm.patient.external.client.PersonnelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Value("${application.require-user-auth:true}")
    private boolean requireUserAuth;
    @Value("${application.require-doctor-auth:true}")
    private boolean requireDoctorAuth;

    private final PersonnelClient personnelClient;

    public boolean validateDoctor(String token) {
        return !requireDoctorAuth || personnelClient.validateDoctor(token);
    }

    public boolean validateUser(String token) {
        return !requireUserAuth || personnelClient.validateToken(token);
    }

    @Autowired
    public AuthorizationService(PersonnelClient personnelClient) {
        this.personnelClient = personnelClient;
    }
}
