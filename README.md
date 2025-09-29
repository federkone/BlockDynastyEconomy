Proyecto basado en arquitectura limpia,por capas "Onion", representada con la capa de: dominio,aplicacion, e infraestructura

Pruebas unitarias y de integracion,TDD, ejecutadas con JUnit

Basado en la Metodologia "Rational Unified Process" dirigido por Casos de Usos

Tecnologias fundamentales: Base de datos transaccionales orquestada por un ORM

Implementacion de la arquitectura con multiples Patrones de diseños para ejecutar la implementación de manera ordenada y legible.


Patron - modelo Cache-Aside (Look-aside cache) con validación transaccional final en la DB.

    -Cache-aside (lazy-loading cache)
        Se lee primero de caché, pero la DB es la fuente de la verdad.

    -Write-through fallback
        Escribes en la DB y luego sincronizas la caché si todo salió bien.

lista de compatibilidad: 1.8 -> 1.12 .... 1.17.1 -> 1.21.5
desde 1.13 hasta 1.16.9 el plugin no es compatible, requieren estrictamente java 8

Todo list:

    -Refactorizar el sistema de informes de transacciones, migrar a la nueva arquitectura basada en eventos de transacciones
    -Razonar lo siguiente: donde se deberia realizar el redondeo de monedas en el sistema, ya que actualmente se esta realizando en el modelo de base de datos.
           lo mas probable es que se deba realizar en el modelo de dominio, ya que es donde se define la logica de negocio y no en la infraestructura.
    -refactor config file, add message file
    -create command for edit accountCanReciveCurrency attribute
    -complete use case and comand for delete accountDb


-placeholder for top currency holders (ex: %blockdynasty_<currency>_top_#_#%)- check
-añadir al placeholder la posibilidad de tener el money ademas de formateado con simbolo o sin simbolo -CHECK


---->

#comandos de administracion para permitir:dar dinero, setear dinero, crear un "buycommand" para comprar comandos
/economy take
/economy give
/economy set
/economy buycommand

#comandos de administracion para crear y editar monedas
/economy currency create

/economy currency delete

/economy currency view

/economy currency color

/economy currency decimals

/economy currency payable

/economy currency rate

/economy currency startbal

/economy currency symbol

/economy currency list

/economy currency default

/economy currency plural

/economy currency singular

#comandos publicos simplificados para los jugadores
/pay

/exchange           #intercambiar monedas

/money            #ver monies

/baltop             #ver top


/offer create

/offer cancel

/offer accept

/offer deny