package revolut.transfer;

import dagger.Component;
import revolut.transfer.modules.CommonModule;

import javax.inject.Singleton;

@Component(modules = CommonModule.class)
@Singleton
public interface ApplicationComponent {
    ApplicationBootstrapInitializer applicationBootstrapInitializer();
}
