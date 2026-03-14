package ServiceProviderTest.Mocks;

import net.blockdynasty.providers.services.Service;

import java.util.function.Supplier;

public class MockServiceSecondary implements Service<String> {
    private String id;

    public MockServiceSecondary(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
