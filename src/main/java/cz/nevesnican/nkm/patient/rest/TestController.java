package cz.nevesnican.nkm.patient.rest;

import cz.nevesnican.nkm.patient.entity.TestEntity;
import cz.nevesnican.nkm.patient.service.repository.TestEntityRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final TestEntityRepositoryService tt;

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String test1() {
        tt.addTestEntity();
        return "ok";
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public List<TestEntity> test2() {
        List<TestEntity> x = tt.getTestEntities();
        return x;
    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public TestEntity test3() {
        List<TestEntity> x = tt.getTestEntities();
        return x.get(0);
    }

    @Autowired
    public TestController(TestEntityRepositoryService tt) {
        this.tt = tt;
    }
}
