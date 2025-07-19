package repositoryTest;

import me.BlockDynasty.Economy.Infrastructure.repositoryV2.RepositorySql;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;

public class FactoryrRepo {

    public static IRepository getDb(){
        return new RepositorySql(new MockConnectionHibernateH2());
    }
}
