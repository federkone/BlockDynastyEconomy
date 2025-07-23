package BlockDynasty.Economy.domain.result;

import BlockDynasty.Economy.domain.entities.account.Account;

public class TransferResult {
    private final Account from;
    private final Account to;

    public TransferResult(Account from, Account to) {
        this.from = from;
        this.to   = to;
    }
    public Account getFrom() { return from; }
    public Account getTo()   { return to;   }
}
