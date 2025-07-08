package me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Translators;

import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Filter;

public class CriteriaHQLConverter {
    private final String tableName;

    public CriteriaHQLConverter(String tableName) {
        this.tableName = tableName;
    }

    public String convert(Criteria criteria) {
        // Iniciar la consulta HQL
        StringBuilder query = new StringBuilder("FROM ").append(tableName).append(" p WHERE 1=1");

        for (Filter filter : criteria.getFilters()) {
            query.append(" AND p.").append(filter.getField()).append(" = :").append(filter.getField());
        }

        // Agregar orden
        if (criteria.getOrder() != null) {
            query.append(" ORDER BY p.").append(criteria.getOrder());
        }

        return query.toString();
    }
}