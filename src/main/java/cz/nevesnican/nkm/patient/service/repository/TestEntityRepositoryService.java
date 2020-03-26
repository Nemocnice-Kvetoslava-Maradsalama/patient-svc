package cz.nevesnican.nkm.patient.service.repository;

import cz.nevesnican.nkm.patient.dao.TestDAO;
import cz.nevesnican.nkm.patient.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEntityRepositoryService {
    private final TestDAO dao;

    public void addTestEntity() {
        TestEntity t = new TestEntity();
        t.setTest("xxx");
        dao.add(t);
    }

    public List<TestEntity> getTestEntities() {
        return dao.getAll();
    }

    @Autowired
    public TestEntityRepositoryService(TestDAO dao) {
        this.dao = dao;
    }
}
