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


/*
*
package net.blockdynasty.providers.services;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/** ServiceProvider is a utility class for registering, retrieving, and unregistering service implementations.
 * It supports multiple implementations for the same service interface/class and allows selection based on predicates.
 * <h3>Rules:</h3>
 * <ul>
 *   <li>Multiple implementations can be registered for the same service interface/class.</li>
 *   <li>When retrieving a service, the first registered implementation is returned by default.</li>
 *   <li>A predicate can be provided to select a specific implementation when retrieving a service.</li>
 *   <li>Services are returned as dynamic proxies to prevent direct access to the original implementation.</li>
 *   <li>Unregistering a service removes only the exact instance provided.</li>
 * </ul>
 *
 * @version 1.0
 */
/*public class ServiceProviderOld {
    private static final Map<Class<?>, Queue<Object>> SERVICES = new ConcurrentHashMap<>();
    public static <T> void register(Class<T> clazz, T service) {
        synchronized (SERVICES) {
            SERVICES.computeIfAbsent(clazz, k -> new LinkedList<>()).add(service);
        }
    }

    public static <T> T get(Class<T> clazz) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services == null || services.isEmpty()) {
                return null;
            }
            Object service= services.peek();
            if (service == null) {
                return null;
            }

            return createProxy(clazz, service);
        }
    }

    public static <T> T get(Class<T> clazz, Predicate<T> selector) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services == null || services.isEmpty()) {
                return null;
            }

            for (Object service : services) {
                @SuppressWarnings("unchecked")
                T typedService = (T) service;
                if (selector.test(typedService)) {
                    return createProxy(clazz, service);
                }
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createProxy(Class<T> clazz, Object service) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> method.invoke(service, args)
        );
    }

    public static <T> void unregister(Class<?> clazz, T service) {
        synchronized (SERVICES) {
            Queue<Object> services = SERVICES.get(clazz);
            if (services != null && !services.isEmpty()) {
                services.removeIf(s -> s == service);
                if (services.isEmpty()) {
                    SERVICES.remove(clazz);
                }
            }
        }
    }
} */

//test

/*
import ServiceProviderTest.Mocks.MockService;
import ServiceProviderTest.Mocks.MockServiceSecondary;
import net.blockdynasty.providers.services.ServiceProviderOld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceProviderTestOld {
    private MockService mockService2 = new MockService("Service1");
    private  MockService mockService3 = new MockService("Service2");
    private  MockService mockService4 = new MockService("Service3");

    private MockServiceSecondary mockServiceSecondary = new MockServiceSecondary("Secondary") {};

    @BeforeEach
    public void setup() {
        ServiceProviderOld.register(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProviderOld.register(MockService.class, mockService2);
        ServiceProviderOld.register(MockService.class, mockService3);
        ServiceProviderOld.register(MockService.class, mockService4);
    }

    @AfterEach
    public void tearDown() {
        ServiceProviderOld.unregister(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProviderOld.unregister(MockService.class, mockService2);
        ServiceProviderOld.unregister(MockService.class, mockService3);
        ServiceProviderOld.unregister(MockService.class, mockService4);
    }
    @Test
    public void testRegisteredServices() {
        assertNotNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service1")));
        assertNotNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
        assertNotNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service3")));
    }

    @Test
    public void testGetFirstServiceRegistered() {
        MockService service = ServiceProviderOld.get(MockService.class);
        assertNotNull(service);
        assertEquals("Service1", service.getId());
    }

    @Test
    public void testGetServiceWithPredicate() {
        assertNotNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
    }

    @Test
    public void testRemoveServiceWithReference() {
        ServiceProviderOld.unregister(MockService.class, mockService3);
        assertNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
    }

    @Test
    public void testRemoveServiceWithoutReference() {
        MockService fakeService = new MockService("Service1");

        MockService proxyService = ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service1"));

        ServiceProviderOld.unregister(MockService.class, fakeService);
        ServiceProviderOld.unregister(MockService.class, proxyService);
        assertNotNull(ServiceProviderOld.get(MockService.class, mockService -> mockService.getId().equals("Service1")));
    }
}*/
