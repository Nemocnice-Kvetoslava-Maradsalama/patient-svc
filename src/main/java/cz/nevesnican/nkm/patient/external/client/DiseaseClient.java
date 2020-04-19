package cz.nevesnican.nkm.patient.external.client;

import cz.nevesnican.nkm.patient.external.model.Disease;
import cz.nevesnican.nkm.patient.external.model.Symptom;
import cz.nevesnican.nkm.patient.external.model.SymptomsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@FeignClient(value="disease-svc", fallback = DiseaseClient.DiseaseClientFallback.class)
public interface DiseaseClient {

    @RequestMapping(value = "/disease/diagnose/", method = RequestMethod.POST)
    List<Disease> diagnose(@RequestBody SymptomsDTO symptoms);

    @RequestMapping(value = "/disease/{id}", method = RequestMethod.GET)
    Disease getDisease(@PathVariable Long id);

    @RequestMapping(value = "/symptom/{id}", method = RequestMethod.GET)
    Symptom getSymptom(@PathVariable Long id);

    @Component
    class DiseaseClientFallback implements DiseaseClient {
        private final static Logger LOG = Logger.getLogger(DiseaseClient.class.getName());

        @Override
        public Symptom getSymptom(Long id) {
            LOG.severe("personnel-svc is unavailable, using fallback getSymptom");

            Symptom rtn = new Symptom();
            rtn.setId(id);
            rtn.setName("Symptom [" + id.toString() + "]");
            return rtn;
        }

        @Override
        public Disease getDisease(Long id) {
            LOG.severe("personnel-svc is unavailable, using fallback getDisease");

            Disease rtn = new Disease();
            rtn.setId(id);
            rtn.setName("Disease [" + id.toString() + "]");
            return rtn;
        }

        @Override
        public List<Disease> diagnose(SymptomsDTO symptoms) {
            LOG.severe("disease-svc is unavailable, cannot process diagnose request");
            return Collections.emptyList();
        }
    }
}
