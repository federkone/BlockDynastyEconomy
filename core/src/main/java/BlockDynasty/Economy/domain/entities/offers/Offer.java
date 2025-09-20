package BlockDynasty.Economy.domain.entities.offers;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Offer implements IOffer {
    private Player vendedor;
    private Player comprador;
    private BigDecimal cantidad;  //puede ser de tipo balance, compuesto por currency y cantidad.
    private Currency tipoCantidad;
    private BigDecimal monto;
    private Currency tipoMonto;

    public Offer(Player vendedor, Player comprador, BigDecimal cantidad, BigDecimal monto,Currency tipoCantidad,Currency tipoMonto) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.cantidad = cantidad;
        this.tipoCantidad = tipoCantidad;
        this.monto = monto;
        this.tipoMonto = tipoMonto;
    }

    public Player getVendedor() {
        return this.vendedor;
    }

    public Player getComprador() {
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
    public int hashCode() {
        return Objects.hash(this.vendedor.getUuid().toString(), this.comprador.getUuid().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(vendedor.getUuid().toString(), offer.vendedor.getUuid().toString()) &&
                Objects.equals(comprador.getUuid().toString(), offer.comprador.getUuid().toString());
    }

}
