package repositoryTest;

import BlockDynasty.repository.RepositorySql;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;

public class FactoryrRepo {

    public static IRepository getDb(){
        return new RepositorySql(new MockConnectionHibernateH2());
    }
}
