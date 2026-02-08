package ServiceProviderTest.Mocks;

import java.util.function.Supplier;

public class ServiceSupplier implements Supplier<MockService> {
    private String id;

    public ServiceSupplier(String id) {
        this.id = id;
    }

    @Override
    public MockService get() {
        return new MockService(id);
    }
}
