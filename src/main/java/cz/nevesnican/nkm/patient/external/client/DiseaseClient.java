package cz.nevesnican.nkm.patient.external.client;

import cz.nevesnican.nkm.patient.external.model.Disease;
import cz.nevesnican.nkm.patient.external.model.Symptom;
import cz.nevesnican.nkm.patient.external.model.SymptomsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Primary
@FeignClient(value="disease-svc", fallback = DiseaseClient.DiseaseClientFallback.class)
public interface DiseaseClient {

    @RequestMapping(value = "/disease/diagnose/", method = RequestMethod.POST)
    List<Disease> diagnose(@RequestBody SymptomsDTO symptoms, @RequestHeader(value="Authorization") String token);

    @Component
    class DiseaseClientFallback implements DiseaseClient {
        private final static Logger LOG = Logger.getLogger(DiseaseClient.class.getName());

        @Override
        public List<Disease> diagnose(SymptomsDTO symptoms, String token) {
            LOG.severe("disease-svc is unavailable, cannot process diagnose request");
            return Collections.emptyList();
        }
    }
}
