package ServiceProviderTest.Mocks;

import net.blockdynasty.providers.services.Service;

import java.util.function.Supplier;

public class MockService implements Service<String> {
    private String id;

    public MockService(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
