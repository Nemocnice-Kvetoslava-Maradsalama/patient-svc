package cz.nevesnican.nkm.patient.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

@Primary
@FeignClient(value="personnel-svc", fallback = PersonnelClient.PersonnelClientFallback.class)
public interface PersonnelClient {

    @RequestMapping(value = "/auth/validate-token", method = RequestMethod.GET)
    boolean validateToken(@RequestHeader(value="Authorization") String token);

    @RequestMapping(value = "/auth/validate-doctor", method = RequestMethod.GET)
    boolean validateDoctor(@RequestHeader(value="Authorization") String token);

    @Component
    class PersonnelClientFallback implements PersonnelClient {
        private final static Logger LOG = Logger.getLogger(PersonnelClient.class.getName());


        @Override
        public boolean validateDoctor(String token) {
            LOG.severe("personnel-svc is unavailable, using fallback validateDoctor");
            return false;
        }

        @Override
        public boolean validateToken(String token) {
            LOG.severe("personnel-svc is unavailable, using fallback validateToken");
            return false;
        }
    }
}
