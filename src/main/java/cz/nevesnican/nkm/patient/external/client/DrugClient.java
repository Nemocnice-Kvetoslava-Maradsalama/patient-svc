package cz.nevesnican.nkm.patient.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

@Primary
@FeignClient(value="drug-svc", fallback = DrugClient.DrugClientFallback.class)
public interface DrugClient {

    @RequestMapping(value = "/suggestPrescription/{diseases}", method = RequestMethod.GET)
    Map<String, Long> suggestPrescription(@PathVariable String diseases, @RequestHeader(value="Authorization") String token);

    @Component
    class DrugClientFallback implements DrugClient {
        private final static Logger LOG = Logger.getLogger(DrugClient.class.getName());

        @Override
        public Map<String, Long> suggestPrescription(String diseases, String token) {
            LOG.severe("drug-svc is unavailable, cannot process suggestPrescription request");
            return Collections.emptyMap();
        }
    }
}
