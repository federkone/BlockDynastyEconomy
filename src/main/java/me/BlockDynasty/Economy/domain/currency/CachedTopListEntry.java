package me.BlockDynasty.Economy.domain.currency;

import java.math.BigDecimal;

public class CachedTopListEntry {

    private String name;
    private BigDecimal amount;

    public CachedTopListEntry(String name, BigDecimal amount) {
        this.name = name; //PLAYER NAME
        this.amount = amount;  //monto?
        //tambien incorporar la currency
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
