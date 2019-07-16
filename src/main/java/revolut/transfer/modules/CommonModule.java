package revolut.transfer.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.BankDetailsRepository;
import revolut.transfer.domain.service.BankAccountFactory;
import revolut.transfer.domain.service.StubBankAccountFactoryImpl;
import revolut.transfer.integration.adapters.JsonTransformer;
import revolut.transfer.integration.repositories.BankDetailsRepositoryImpl;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;
import revolut.transfer.integration.repositories.StubAccountRepositoryImpl;
import spark.ResponseTransformer;

import javax.inject.Named;
import java.util.ResourceBundle;

@Module()
public class CommonModule {
    public final ResourceBundle resourceBundle = ResourceBundle.getBundle("local");

    @Provides
    public AccountHolderRepository accountHolderRepository(StubAccountHolderRepositoryImpl impl) {
        return impl;
    }

    @Provides
    public AccountRepository accountRepository(StubAccountRepositoryImpl impl) {
        return impl;
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    public ResponseTransformer responseTransformer(JsonTransformer jsonTransformer) {
        return jsonTransformer;
    }

    @Provides
    public BankAccountFactory bankAccountService(StubBankAccountFactoryImpl impl) {
        return impl;
    }

    @Provides
    public BankDetailsRepository bankDetailsRepository(BankDetailsRepositoryImpl impl) {
        return impl;
    }

    @Provides @Named("application.port")
    public String applicationport() {
        return resourceBundle.getString("application.port");
    }

    @Provides @Named("database.url")
    public String databaseUrl() {
        return resourceBundle.getString("database.url");
    }

    @Provides @Named("database.user")
    public String databaseUser() {
        return resourceBundle.getString("database.user");
    }

    @Provides @Named("database.password")
    public String databasePassword() {
        return resourceBundle.getString("database.password");
    }

}
