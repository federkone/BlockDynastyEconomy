Todo list:
    - usar como fuente de la verdad siempre la DB para los balances. todas las operaciones de lectura(validacion de saldo,etc) y escritura(actualizacion de saldos) seran sobre la DB, y en caso de escritura en db exitosa, actualizar la cache.
    - consumiendo la cache PARA LA GUI como placeholder(solo lectura),
    - separando estos casos se soluciona el problema de desyncronizacion de balances entre la cache y la DB.

    -refactor config file, add message file
    -create command for edit accountCanReciveCurrency attribute
    -complete use case and comand for delete account 
    -complete use case delete currency and command //deleting a currency will be delete all balances of that currency- check
    -add rate for exchange/trade/transfer currency, its all ready usesCase/atributes for implement this feature
    -complete use case for balTop and command, this command will show the top X balances of a currency, ej: eco top 10, eco top 5 etc

    -add/complete all test for use cases

-placeholder for top currency holders (ex: %gemseco_<currency>_top_#_#%)- check
-añadir al placeholder la posibilidad de tener el balance ademas de formateado con simbolo o sin simbolo -CHECK




[10:51:58 INFO]: UUID of player Nullplague is 55e72bac-6481-3abe-9c9b-94cefed85271
[10:51:59 INFO]: [BedrockPlayerCheckPlugin] Player Nullplague is not a Floodgate player
[10:51:59 INFO]: Nullplague[/190.19.225.15:43418] logged in with entity id 106 at ([world]434.5, -34.0, 422.5)
[10:52:08 INFO]: [AlonsoLevels] Leaderboard is disabled! Skipping..
[10:52:14 INFO]: UUID of player Cris is 53a2f038-3d3d-325c-ac6f-8ef419f1a18a
[10:52:14 INFO]: [BedrockPlayerCheckPlugin] Player Cris is not a Floodgate player
[10:52:14 INFO]: Cris[/190.19.225.15:42124] logged in with entity id 107 at ([world]434.5, -34.0, 422.5)
[10:52:14 INFO]: + Cris Connected
[10:52:22 INFO]: Nullplague lost connection: Disconnected
[10:52:43 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - Received: account = 55e72bac-6481-3abe-9c9b-94cefed85271
[10:52:43 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - User is not online. Skipping update.
[10:52:43 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - Received: account = 05a671e1-fce9-4f7d-80f2-4d03b1b4ed89
[10:52:43 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - User is not online. Skipping update.
[10:55:12 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - Received: account = 05a671e1-fce9-4f7d-80f2-4d03b1b4ed89
[10:55:12 INFO]: [BlockDynastyEconomy] BlockDynastyEconomy Data Channel - User is not online. Skipping update.

tengo que trabajar si o si en el plugin de economia basandome en el nombre del jugador.
revisar toda la logica de busqueda, carga y descarga de usuarios basada en uuid pasarla a nombre.

elaborar un caso de test donde un nombre "X" con UUID "X"
cambie a nombre "X" con un UUID "Y"
tambien, cambio de nombre manteniendo la misma uuid.

como se comporta el sistema en un caso donde un nombre cambia de uuid.
considerar aplicar una mejora relacionada a deteccion de cambio de uuid.

pd: El problema principal reside en que las uuid no son creíbles en mi constexto por que el server se encuentra en modo
offline, por lo que acepta a ususarios con el juego "pirata"

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
/balance            #ver balances
/baltop             #ver top

/offer create
/offer cancel
/offer accept
/offer deny