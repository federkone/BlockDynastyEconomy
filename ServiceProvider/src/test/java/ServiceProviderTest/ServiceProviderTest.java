/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package ServiceProviderTest;

import ServiceProviderTest.Mocks.MockService;
import ServiceProviderTest.Mocks.MockServiceSecondary;
import ServiceProviderTest.Mocks.SecondServiceSupplier;
import ServiceProviderTest.Mocks.ServiceSupplier;
import net.blockdynasty.providers.services.ServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceProviderTest {
    private Supplier<MockService> mockService1 = new ServiceSupplier("Service1");
    private Supplier<MockService> mockService2 = new ServiceSupplier("Service2");
    private Supplier<MockService> mockService3 = new ServiceSupplier("Service3");
    private Supplier<MockServiceSecondary> mockServiceSecondary = new SecondServiceSupplier("Secondary");

    @BeforeEach
    public void setup() {
        ServiceProvider.register(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProvider.register(MockService.class, mockService1);
        ServiceProvider.register(MockService.class, mockService2);
        ServiceProvider.register(MockService.class, mockService3);
    }

    @AfterEach
    public void tearDown() {
        ServiceProvider.unregister(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProvider.unregister(MockService.class, mockService1);
        ServiceProvider.unregister(MockService.class, mockService2);
        ServiceProvider.unregister(MockService.class, mockService3);
    }

    @Test
    public void testRegisteredServices() {
        assertTrue(ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service1")).isPresent());
        assertTrue(ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service2")).isPresent());
        assertTrue(ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service3")).isPresent());
    }

    @Test
    public void testGetFirstServiceRegistered() {
        Optional<MockService> service = ServiceProvider.get(MockService.class);
        assertTrue(service.isPresent());
        assertEquals("Service1", service.get().getId());
    }

    @Test
    public void testGetServiceWithPredicate() {
        assertFalse(ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service2")).isEmpty());
    }

    @Test
    public void testRemoveServiceWithReference() {
        ServiceProvider.unregister(MockService.class, mockService2);
        Optional<MockService> service = ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service2"));
        assertTrue(service.isEmpty());
    }

    @Test
    public void testRemoveServiceWithoutReference() {
        Supplier<MockService> fakeService = new ServiceSupplier("Service1");
        ServiceProvider.register(MockService.class, fakeService);

        Optional<MockService> service = ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service1"));
        Supplier<MockService> serviceSupplier = service::get;

        ServiceProvider.unregister(MockService.class, serviceSupplier);
        ServiceProvider.unregister(MockService.class, fakeService);
        assertTrue(ServiceProvider.get(MockService.class,
                mockService -> mockService.getId().equals("Service1")).isPresent());
    }

    @Test
    public void getWithIdTest(){
        assertTrue(ServiceProvider.getWithId(MockService.class, "Service1").isPresent());
        assertTrue(ServiceProvider.getWithId(MockService.class, "Service2").isPresent());
        assertTrue(ServiceProvider.getWithId(MockService.class, "Service3").isPresent());
    }
}
