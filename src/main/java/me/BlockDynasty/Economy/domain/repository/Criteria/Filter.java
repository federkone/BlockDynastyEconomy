package me.BlockDynasty.Economy.domain.repository.Criteria;

import java.util.Objects;

public class Filter {
    private String field;
    private Object value;

    public Filter(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    // Sobrescribimos equals y hashCode para que LinkedHashSet pueda comparar objetos basados en "field"
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(field, filter.field);  // Compara solo el campo "field"
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);  // Usa solo el campo "field" para el hash
    }
}
