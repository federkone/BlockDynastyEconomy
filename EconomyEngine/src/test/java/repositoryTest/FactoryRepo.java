package repositoryTest;


import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import repository.Repository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;

public class FactoryRepo {

    public static IRepository getDb(){
        return new Repository(new MockConnectionHibernateH2());
    }
}
