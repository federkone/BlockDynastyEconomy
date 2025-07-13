package me.BlockDynasty.Economy.domain.entities.offers;

import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public interface IOffer {

     UUID getVendedor();
     UUID getComprador();
     BigDecimal getCantidad();
     Currency getTipoCantidad();
     BigDecimal getMonto();
     Currency getTipoMonto();
     int hashCode();
}
