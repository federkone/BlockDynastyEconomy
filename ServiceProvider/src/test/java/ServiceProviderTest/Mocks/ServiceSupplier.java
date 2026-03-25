package ServiceProviderTest.Mocks;

import java.util.function.Supplier;

public class ServiceSupplier implements Supplier<MockService> {
    private String id;
    private volatile MockService singletonInstance;
    //private useCase1, useCase2.
    //private boolean reloading = false; // en true y seteamos singletoINstance en null

    public ServiceSupplier(String id) {
        this.id = id;
    }

    public void updateDependencies(){// Paso 1 completado: los datos nuevos ya están en el Supplier
        //this use case = use case


        //incluso puedo invalidar automaticamente aqui
        //invalidateService()
    }

    private void invalidateService() { // Paso 2 completado: la próxima llamada a get() disparará el 'new'
        singletonInstance = null;
    }

    //se podria decir que primero creo el caso de uso nuevo e inmediatamente actualizo aqui
    //podriamos tener una variable interna que indique el estado, puede ser un estado "reloading"
    //que sea cambiada durante el reinicio del sistema

    @Override
    public MockService get() {
        if (singletonInstance == null) {
            synchronized (this) {
                if (singletonInstance == null) {
                    singletonInstance = new MockService(id); // El constructor usa las variables que actualizamos en el paso 1
                }
            }
        }
        return singletonInstance;
    }

    /*
    * // Leemos a una variable local para optimizar el acceso volatile
        MockService service = singletonInstance;

        if (service == null) {
            synchronized (this) { // Evita que dos hilos creen el New al mismo tiempo
                service = singletonInstance;
                if (service == null) {
                    service = new MockService(id);
                    singletonInstance = service;
                }
            }
        }
        return service;*/
}
