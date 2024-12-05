package me.BlockDynasty.Economy.domain.Offers;

import me.BlockDynasty.Economy.domain.currency.Currency;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Offer {
    private UUID vendedor;
    private UUID comprador;
    private double cantidad;
    private Currency tipoCantidad;
    private double monto;
    private Currency tipoMonto;
    private BukkitRunnable expirationTask;

    public Offer() {
        this.vendedor=null;
        this.comprador=null;
        this.cantidad=0;
        this.tipoCantidad=null;
        this.monto=0;
        this.tipoMonto=null;
    }
    public Offer(UUID vendedor, UUID comprador, double cantidad, double monto,Currency tipoCantidad,Currency tipoMonto) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.cantidad = cantidad;
        this.tipoCantidad = tipoCantidad;
        this.monto = monto;
        this.tipoMonto = tipoMonto;
    }

    public void setVendedor(UUID vendedor) {
        this.vendedor=vendedor;
    }
    public void setComprador(UUID comprador) {
        this.comprador=comprador;
    }

    public void setCantidad(double cantidad) {
        this.cantidad=cantidad;
    }
    public void setTipoCantidad(Currency tipoCantidad) {
        this.tipoCantidad=tipoCantidad;
    }
    public void setMonto(double monto) {
        this.monto=monto;
    }
    public void setTipoMonto(Currency tipoMonto) {
        this.tipoMonto=tipoMonto;
    }

    public UUID getVendedor() {
        return this.vendedor;
    }

    public UUID getComprador() {
        return this.comprador;
    }

    //cantidad de moneda ofertada
    public double getCantidad() {
        return this.cantidad;
    }

    //tipo de moneda ofertada
    public Currency getTipoCantidad() {
        return this.tipoCantidad;
    }

    //monto a pagar
    public double getMonto() {
        return this.monto;
    }
    //tipo de moneda monto a pagar
    public Currency getTipoMonto() {
        return this.tipoMonto;
    }

    public void setExpirationTask(BukkitRunnable expirationTask) {
        this.expirationTask=expirationTask;
    }
    public void cancelExpirationTask() {
        if(this.expirationTask != null) {
            this.expirationTask.cancel();
        }
    }

}
