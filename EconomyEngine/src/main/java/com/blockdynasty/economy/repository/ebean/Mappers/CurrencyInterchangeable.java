/*package com.blockdynasty.economy.repository.ebean.Mappers;

import com.blockdynasty.economy.repository.ebean.Models.CurrencyDb;
import jakarta.persistence.*;

@Entity
@Table(name = "currency_interchangeable")
public class CurrencyInterchangeable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_uuid")
    private CurrencyDb currency;

    @ManyToOne
    @JoinColumn(name = "interchangeable_uuid")
    private CurrencyDb interchangeable;

}
*/