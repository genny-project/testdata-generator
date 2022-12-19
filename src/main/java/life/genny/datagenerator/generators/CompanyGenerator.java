package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;

import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class CompanyGenerator extends CustomFakeDataGenerator {

    @Override
    public BaseEntity generate(BaseEntity entity) {
        return entity;
    }

    @Override
    Object runGenerator(String attributeCode, String regex, String... args) {
        return null;
    }
    
}
