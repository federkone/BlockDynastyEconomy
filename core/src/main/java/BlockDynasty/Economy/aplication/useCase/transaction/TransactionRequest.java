package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.domain.events.Context;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {
    public enum TransactionType {
        WITHDRAW,
        DEPOSIT,
        SET,
        EXCHANGE,
        TRADE,
        TRANSFER,
        PAY
    }

    private final TransactionType transactionType;
    private final String fromName;
    private final UUID fromUUID;
    private final String toName;
    private final UUID toUUID;
    private final String currencyFrom;
    private final String currencyTo;
    private final BigDecimal amountFrom;
    private final BigDecimal amountTo;
    private final Context context;

    private TransactionRequest(Builder builder) {
        this.transactionType = builder.transactionType;
        this.fromName = builder.fromName;
        this.fromUUID = builder.fromUUID;
        this.toName = builder.toName;
        this.toUUID = builder.toUUID;
        this.currencyFrom = builder.currencyFrom;
        this.currencyTo = builder.currencyTo;
        this.amountFrom = builder.amountFrom;
        this.amountTo = builder.amountTo;
        this.context = builder.context;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public String getFromName() {
        return this.fromName;
    }

    public UUID getFromUUID() {
        return this.fromUUID;
    }

    public String getToName() {
        return this.toName;
    }

    public UUID getToUUID() {
        return this.toUUID;
    }

    public String getCurrencyFrom() {
        return this.currencyFrom;
    }

    public String getCurrencyTo() {
        return this.currencyTo;
    }

    public BigDecimal getAmountFrom() {
        return this.amountFrom;
    }

    public BigDecimal getAmountTo() {
        return this.amountTo;
    }

    public Context getContext() {
        return this.context;
    }

    public static class Builder {
        private TransactionType transactionType;
        private String fromName;
        private UUID fromUUID;
        private String toName;
        private UUID toUUID;
        private String currencyFrom;
        private String currencyTo;
        private BigDecimal amountFrom;
        private BigDecimal amountTo;
        private Context context;

        public Builder type(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder fromName(String fromName) {
            this.fromName = fromName;
            return this;
        }

        public Builder fromUUID(UUID fromUUID) {
            this.fromUUID = fromUUID;
            return this;
        }

        public Builder toName(String toName) {
            this.toName = toName;
            return this;
        }

        public Builder toUUID(UUID toUUID) {
            this.toUUID = toUUID;
            return this;
        }

        public Builder currencyFrom(String currencyFrom) {
            this.currencyFrom = currencyFrom;
            return this;
        }

        public Builder currencyTo(String currencyTo) {
            this.currencyTo = currencyTo;
            return this;
        }

        public Builder amountFrom(BigDecimal amountFrom) {
            this.amountFrom = amountFrom;
            return this;
        }

        public Builder amountTo(BigDecimal amountTo) {
            this.amountTo = amountTo;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public TransactionRequest build() {
            //validaciones necesarias para los tipos de transacciones
            if (transactionType == null) {
                throw new IllegalStateException("transactionType must be set");
            }
            return new TransactionRequest(this);
        }
    }
}
