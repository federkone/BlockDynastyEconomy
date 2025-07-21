package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class BalanceId implements Serializable {
    private Long walletId;
    private Long currencyId;
}
