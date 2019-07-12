package revolut.transfer;

import revolut.transfer.integration.adapters.AccountAdapter;
import revolut.transfer.integration.adapters.AccountHolderAdapter;
import revolut.transfer.integration.adapters.QuoteAdapter;
import revolut.transfer.integration.adapters.TransferAdapter;

public class ApplicationBootstrapInitializer {
    private final TransferAdapter transferAdapter;
    private final AccountAdapter accountAdapter;
    private final QuoteAdapter quoteAdapter;
    private final AccountHolderAdapter accountHolderAdapter;

    public ApplicationBootstrapInitializer() {
        transferAdapter = new TransferAdapter();
        accountAdapter = new AccountAdapter();
        quoteAdapter = new QuoteAdapter();
        accountHolderAdapter = new AccountHolderAdapter();
    }

    public void initialize() {
        transferAdapter.initialize();
        accountAdapter.initialize();
        quoteAdapter.initialize();
        accountHolderAdapter.initialize();
    }
}
