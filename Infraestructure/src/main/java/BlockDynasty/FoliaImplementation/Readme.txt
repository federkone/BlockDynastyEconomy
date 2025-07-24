Para soporte de folia se utilizará el mismo proyecto paper/bukkit
con la diferencia de que se va a utilizar un SCHEDULER diferente para operaciones asincrónicas

Agregado de un elemento llamado ContextualTask que contiene lo necesario para trabajar en las versiones de scheduler: spigot y Folia
ContextualTask + SchedulerFactory se tiene lo esencial para trabajar con las versiones existentes y futuras relacionadas a la concurrencia de tareas asincrónicas de los servidores optimizados

Es retrocompatible Folia con Canvas.