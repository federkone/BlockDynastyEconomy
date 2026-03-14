package ServiceProviderTest.Mocks;

import java.util.function.Supplier;

public class SecondServiceSupplier implements Supplier<MockServiceSecondary> {
    private String id;

    public SecondServiceSupplier(String id) {
        this.id = id;
    }

    @Override
    public MockServiceSecondary get() {
        return new MockServiceSecondary(id);
    }
}
