package me.BlockDynasty.Economy.domain.repository.Criteria.Translators;

import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;

public interface ICriteriaConverter {
    String convert(Criteria criteria);
}