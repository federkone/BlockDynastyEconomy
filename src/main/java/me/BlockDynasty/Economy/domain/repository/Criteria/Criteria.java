package me.BlockDynasty.Economy.domain.repository.Criteria;

import java.util.LinkedHashSet;
import java.util.Set;

public class Criteria {
    private Set<Filter> filters;
    private String order;
    private Integer limit;
    private Integer offset;

    private Criteria() {
        this.filters = new LinkedHashSet<>();
    }

    public static Criteria create() {
        return new Criteria();
    }

    //public void and(Criteria criteria2) {
    //    this.filters.addAll(criteria2.getFilters());
    //}

    public Criteria filter(String field, Object value) {
        filters.add(new Filter(field, value));
        return this;
    }

    public Criteria order(String field, boolean ascending) {
        this.order = field + (ascending ? " ASC" : " DESC");
        return this;
    }

    public Criteria limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Criteria offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Set<Filter> getFilters() {
        return this.filters;
    }

    public String getOrder() {
        return order;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

}


