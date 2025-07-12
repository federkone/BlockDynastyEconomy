package me.BlockDynasty.Economy.domain.entities.offers;

import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Offer {
    private UUID vendedor;
    private UUID comprador;
    private BigDecimal cantidad;  //puede ser de tipo balance, compuesto por currency y cantidad.
    private Currency tipoCantidad;
    private BigDecimal monto;
    private Currency tipoMonto;

    public Offer(UUID vendedor, UUID comprador, BigDecimal cantidad, BigDecimal monto,Currency tipoCantidad,Currency tipoMonto) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.cantidad = cantidad;
        this.tipoCantidad = tipoCantidad;
        this.monto = monto;
        this.tipoMonto = tipoMonto;
    }

    public UUID getVendedor() {
        return this.vendedor;
    }

    public UUID getComprador() {
        return this.comprador;
    }

    //cantidad de moneda ofertada
    public BigDecimal getCantidad() {
        return this.cantidad;
    }

    //tipo de moneda ofertada
    public Currency getTipoCantidad() {
        return this.tipoCantidad;
    }

    //monto a pagar
    public BigDecimal getMonto() {
        return this.monto;
    }
    //tipo de moneda monto a pagar
    public Currency getTipoMonto() {
        return this.tipoMonto;
    }

    @Override
    public int hashCode() { //para usar en un map
        return Objects.hash(this.vendedor, this.vendedor);
    }
}
