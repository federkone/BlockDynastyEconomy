package BlockDynasty.repository.ConnectionHandler.Hibernate;

import org.hibernate.SessionFactory;

public interface Connection {
    SessionFactory getSession();
    void close();
}
