package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.entity.TestEntity;
import cz.nevesnican.nkm.patient.service.repository.TestEntityRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final TestEntityRepositoryService tt;

    @RequestMapping("/test1")
    public String test1() {
        tt.addTestEntity();
        return "ok";
    }

    @RequestMapping("/test2")
    public String test2() {
        List<TestEntity> x = tt.getTestEntities();
        return Long.toString(x.size());
    }

    @Autowired
    public TestController(TestEntityRepositoryService tt) {
        this.tt = tt;
    }
}
