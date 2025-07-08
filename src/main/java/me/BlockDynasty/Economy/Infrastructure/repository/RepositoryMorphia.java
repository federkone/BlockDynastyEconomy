package me.BlockDynasty.Economy.Infrastructure.repository;

//import dev.morphia.query.filters.Filters;


/*public class RepositoryMorphia implements IRepository{
    private static Datastore datastore;

    public RepositoryMorphia(ConnectionMorphia connectionMorphia) {
        datastore = ConnectionMorphia.getDatastore();
    }
    @Override
    public void loadCurrencies() {
        List<CurrencyMongoDb> currencies = datastore.find(CurrencyMongoDb.class).iterator().toList();
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        return createQuery(CurrencyMongoDb.class, criteria).stream()
                .map(CurrencyMongoDb::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void saveCurrency(Currency currency) {
        CurrencyMongoDb currencyMongoDb = new CurrencyMongoDb(currency);
        datastore.save(currencyMongoDb);
    }

    @Override
    public void deleteCurrency(Currency currency) {
        CurrencyMongoDb currencyMongoDb = new CurrencyMongoDb(currency);
        datastore.delete(currencyMongoDb);
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        return createQuery(AccountMongoDb.class, criteria).stream()
                .map(AccountMongoDb::toEntity)
                .collect(Collectors.toList());
    }

    public <T> List<T> createQuery(Class<T> entityClass, Criteria criteria) throws TransactionException {
        List<T> results = new ArrayList<>();
        try {
            // Crea una consulta base para la entidad
            Query<T> query = datastore.find(entityClass);

            // Aplica filtros dinámicos si los hay
            if (criteria != null && criteria.getFilters() != null) {
                for (Filter customFilter : criteria.getFilters()) {
                    query.filter((dev.morphia.query.filters.Filter) Filters.eq(customFilter.getField(), customFilter.getValue()));
                }
            }

        // Configura las opciones de consulta
            FindOptions findOptions = new FindOptions();

            // Aplica el límite si está definido
            if (criteria != null && criteria.getLimit() != null) {
                findOptions.limit(criteria.getLimit());
            }

            // Aplica la paginación (opcional, usando skip si está definido en Criteria)
            if (criteria != null && criteria.getOffset() != null) {
                findOptions.skip(criteria.getOffset());
            }

            // Aplica el ordenamiento si está definido en Criteria
            if (criteria != null && criteria.getOrder() != null) {
                findOptions.sort(Sort.ascending(criteria.getOrder())); // Orden ascendente
            }

            // Ejecuta la consulta con las opciones configuradas
            results = query.iterator(findOptions).toList();
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
        return results;
    }

    @Override
    public void createAccount(Account account) {
        AccountMongoDb accountMongoDb = new AccountMongoDb(account);
        datastore.save(accountMongoDb);
    }

    @Override
    public void saveAccount(Account account) {
        AccountMongoDb accountMongoDb = new AccountMongoDb(account);
        datastore.save(accountMongoDb);
    }

    @Override
    public void transfer(Account userFrom, Account userTo) {

    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return List.of();
    }

    @Override
    public boolean isTopSupported() {

        return false;
    }

    @Override
    public String getName() {
        return "mongoDb";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {

    }
}*/
