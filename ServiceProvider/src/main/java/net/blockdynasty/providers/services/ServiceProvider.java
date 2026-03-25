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

package net.blockdynasty.providers.services;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** ServiceProvider is a utility class for registering, retrieving, and unregistering service implementations.
 * It supports multiple implementations for the same service interface/class and allows selection based on predicates.
 * <h3>Rules:</h3>
 * <ul>
 *   <li>Multiple implementations can be registered for the same service interface/class.</li>
 *   <li>When retrieving a service, the first registered implementation is returned by default.</li>
 *   <li>A predicate can be provided to select a specific implementation when retrieving a service.</li>
 *   <li>Services are returned by Supplier implementation to prevent direct access to the original Service.</li>
 *   <li>Unregistering a service removes only the exact instance of Supplier provided.</li>
 * </ul>
 *
 * @version 1.0
 */
public class ServiceProvider {
    private static final Map<Class<?>, Queue<Supplier<?>>> SERVICES = new ConcurrentHashMap<>();

    /** Registers a service implementation for the specified interface/class.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.Class"
     * @param supplier service Supplier of implementation of the service
     * @param <T> type of the service
     */
    public static <T extends Service<?>> void register(Class<T> clazz, Supplier<T> supplier) {
        SERVICES.computeIfAbsent(clazz, k -> new ConcurrentLinkedQueue<>()).add(supplier);
    }

    /** Retrieves a service implementation for the specified interface/class.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.Class"
     * @param <T> type of the service
     * @return implementation of the service or null if not found
     * NOTE: If multiple implementations are registered, the first one registered is returned.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Service<?>> Optional<T> get(Class<T> clazz) {
        Queue<Supplier<?>> services = SERVICES.get(clazz);
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((T) services.peek().get());
    }

    /** Retrieves a service implementation for the specified interface/class that matches the given ID.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.Class"
     * @param id identifier to select the desired service implementation
     * @param <T> type of the service
     * @param <I> type of the service ID
     * @return implementation of the service or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Service<I>,I> Optional<T> getWithId(Class<T> clazz, I id) {
        Queue<Supplier<?>> services = SERVICES.get(clazz);
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }
        return services.stream()
                .map(s -> (T) s.get())
                .filter(service -> service.getId().equals(id))
                .findFirst();
    }

    /** Retrieves a service implementation for the specified interface/class that matches the given selector.
     *
     * @param clazz class/interface type of the service "ex: EconomyService.Class"
     * @param selector predicate to select the desired service implementation
     * @param <T> type of the service
     * @return implementation of the service or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Service<?>> Optional<T> get(Class<T> clazz, Predicate<T> selector) {
        Queue<Supplier<?>> services = SERVICES.get(clazz);
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }
        return services.stream()
                .map(s -> (T) s.get())
                .filter(selector)
                .findFirst();
    }

    /** Unregisters a service implementation for the specified interface/class.
     * Only removes the exact instance provided as the service.
     * @param clazz class/interface type of the service "ex: EconomyService.Class"
     * @param supplier original of the service to be unregistered
     */
    public static <T extends Service<?>> void unregister(Class<T> clazz, Supplier<T> supplier) {
        Queue<Supplier<?>> services = SERVICES.get(clazz);
        if (services != null && !services.isEmpty()) {
            services.removeIf(s -> s == supplier);
            if (services.isEmpty()) {
                SERVICES.remove(clazz);
            }
        }
    }
}
