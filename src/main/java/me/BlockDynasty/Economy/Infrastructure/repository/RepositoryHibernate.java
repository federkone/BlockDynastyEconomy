package me.BlockDynasty.Economy.Infrastructure.repository;

public class RepositoryHibernate  {
    /*private SessionFactory sessionFactory;

    public RepositoryHibernate(ConnectionHibernate connectionHibernate) {
        this.sessionFactory = connectionHibernate.getSession();

    }

    public void insertData(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(currency);
            transaction.commit();
        }
    }

    public void deleteData(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(currency);
            transaction.commit();
        }
    }

    public void updateData(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(currency);
            transaction.commit();
        }
    }

    public void insertData(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
        }
    }

    public void deleteData(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(account);
            transaction.commit();
        }
    }

    public void updateData(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(account);
            transaction.commit();
        }
    }

    @Override
    public void loadCurrencies() { //no usage
        try (Session session = sessionFactory.openSession()) {
            List<Currency> currencies = session.createQuery("from Currency", Currency.class).list();
            //plugin.getCurrencyManager().add(currencies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        String hql = new CriteriaHQLConverter("currencies").convert(criteria);
        Session session = sessionFactory.openSession();
            Query<Currency> query = session.createQuery(hql, Currency.class);
            for (Filter filter : criteria.getFilters()) {
                query.setParameter(filter.getField(), filter.getValue());
            }
            if (criteria.getLimit() != null) {
                query.setMaxResults(criteria.getLimit());
            }
            if (criteria.getOffset() != null) {
                query.setFirstResult(criteria.getOffset());
            }
            return query.getResultList();
    }

    @Override
    public void saveCurrency(Currency currency) {
        insertData(currency);
    }

    @Override
    public void deleteCurrency(Currency currency) {
        deleteData(currency);
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        String hql = new CriteriaHQLConverter("accounts").convert(criteria);
        Session session = sessionFactory.openSession();
            Query<Account> query = session.createQuery(hql, Account.class);
            for (Filter filter : criteria.getFilters()) {
                query.setParameter(filter.getField(), filter.getValue());
            }
            if (criteria.getLimit() != null) {
                query.setMaxResults(criteria.getLimit());
            }
            if (criteria.getOffset() != null) {
                query.setFirstResult(criteria.getOffset());
            }
            return query.getResultList();
    }

    @Override
    public void createAccount(Account account) {
        insertData(account);
    }

    @Override
    public void saveAccount(Account account) {
        updateData(account);
    }

    @Override
    public void transfer(Account userFrom, Account userTo) {

    }



    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return null;
    }

    @Override
    public boolean isTopSupported() {
        return false;
    }

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {

    }*/
}