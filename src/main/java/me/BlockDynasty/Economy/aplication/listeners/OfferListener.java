package me.BlockDynasty.Economy.aplication.listeners;

import me.BlockDynasty.Economy.domain.entities.offers.Offer;

public interface OfferListener {
    void onOfferExpired(Offer offer);
}