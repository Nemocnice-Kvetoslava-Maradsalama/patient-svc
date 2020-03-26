package cz.nevesnican.nkm.patient.dao;

import cz.nevesnican.nkm.patient.entity.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TestDAO extends BaseDAO<TestEntity, Long> {
    public TestDAO() {
        super(TestEntity.class);
    }
}
