package BlockDynasty.Economy.domain.entities.currency;

import java.math.BigDecimal;
import java.util.UUID;

public interface ICurrency {

     void setUuid(UUID uuid);
     void setSingular(String singular);
     void setPlural(String plural);
     UUID getUuid();
     String getSingular();
     String getPlural();
     BigDecimal getDefaultBalance() ;
     String format(BigDecimal amount);
     boolean isValidAmount(BigDecimal amount);
     boolean isDefaultCurrency();
     void setStartBalance(BigDecimal startBalance);
     void setDefaultBalance(BigDecimal defaultBalance);
     void setDefaultCurrency(boolean defaultCurrency);
     boolean isTransferable();
     void setTransferable(boolean transferable);
     boolean isDecimalSupported();
     void setDecimalSupported(boolean decimalSupported);
     String getColor();
     void setColor(String color);
     String getSymbol();
     void setSymbol(String symbol);
     double getExchangeRate();
     void setExchangeRate(double exchangeRate);
     boolean equals(Object o) ;
     int hashCode();
}
