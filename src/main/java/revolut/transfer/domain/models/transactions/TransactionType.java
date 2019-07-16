package revolut.transfer.domain.models.transactions;

public enum  TransactionType {
    DEPOSIT(false),
    INCOMING_PAYMENT(false),
    OUTBOUND_PAYMENT(true),
    WITHDRAW(true);

    private boolean representNegativeOperation;

    TransactionType(boolean representNegativeOperation) {
        this.representNegativeOperation = representNegativeOperation;
    }

    public boolean isNegativeOperation() {
        return representNegativeOperation;
    }
}
