package revolut.transfer.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.integration.adapters.JsonTransformer;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;
import revolut.transfer.integration.repositories.StubAccountRepositoryImpl;
import spark.ResponseTransformer;

@Module
public class CommonModule {

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
}
