package cz.nevesnican.nkm.patient.external.client;

import cz.nevesnican.nkm.patient.external.model.DoctorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@FeignClient(value="personnel-svc", fallback = PersonnelClient.PersonnelClientFallback.class)
public interface PersonnelClient {

    @RequestMapping(value = "/auth/validate-token", method = RequestMethod.POST)
    boolean validateToken(@RequestHeader(value="Authorization") String token);

    @RequestMapping(value = "/auth/validate-doctor", method = RequestMethod.POST)
    boolean validateDoctor(@RequestHeader(value="Authorization") String token);

    @RequestMapping(value = "/doctor/{id}", method = RequestMethod.GET)
    DoctorDTO getDoctor(@PathVariable Long id);

    @Component
    class PersonnelClientFallback implements PersonnelClient {
        private final static Logger LOG = Logger.getLogger(PersonnelClient.class.getName());

        @Override
        public DoctorDTO getDoctor(Long id) {
            LOG.severe("personnel-svc is unavailable, using fallback getDoctor");

            DoctorDTO rtn = new DoctorDTO();
            rtn.setFirstname("Doctor");
            rtn.setLastname(id.toString());
            return rtn;
        }

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
