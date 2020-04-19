package cz.nevesnican.nkm.patient.service.security;

import cz.nevesnican.nkm.patient.external.client.PersonnelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final PersonnelClient personnelClient;

    public boolean validateDoctor(String token) {
        return personnelClient.validateDoctor(token);
    }

    public boolean validateUser(String token) {
        return personnelClient.validateToken(token);
    }

    @Autowired
    public AuthorizationService(PersonnelClient personnelClient) {
        this.personnelClient = personnelClient;
    }
}
