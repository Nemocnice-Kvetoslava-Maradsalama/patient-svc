package cz.nevesnican.nkm.patient.external.client;

import cz.nevesnican.nkm.patient.external.model.DoctorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

@FeignClient(value="drug-svc", fallback = DrugClient.DrugClientFallback.class)
public interface DrugClient {


    @Component
    class DrugClientFallback implements DrugClient {
        private final static Logger LOG = Logger.getLogger(DrugClient.class.getName());


    }
}
