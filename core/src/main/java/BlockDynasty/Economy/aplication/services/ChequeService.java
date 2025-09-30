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

package BlockDynasty.Economy.aplication.services;

//los cheques deben ser creados como entidad y guardarlo en la db, donde un cheque tenga una uuid y un itemstack
//se puede mantener una logica interna donde tengan vencimiento y se borre el registro de la db
//esto con el fin de evitar que los cheques se queden en el inventario de los jugadores indefinidamente o si se pierde el item fisico del juego
//a la hora de reclamarlos, se debe corroborar la integridad del mismo, con la uuid o con algun hash de la entidad
//llamar al caso de uso de transferencia, esperar a que sea exitoso, y eliminar el cheque de la db y destruir el itemstack del cheque

public class ChequeService {
/*
    private final BlockDynastyEconomy plugin;
    private final ItemStack chequeBaseItem;

    public ChequeManager(BlockDynastyEconomy plugin) {
        this.plugin = plugin;

        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("cheque.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(UtilString.colorize(plugin.getConfig().getString("cheque.name")));
        meta.setLore(UtilString.colorize(plugin.getConfig().getStringList("cheque.lore")));
        item.setItemMeta(meta);
        chequeBaseItem = item;
    }

    public ItemStack write(String creatorName, Currency currency, double amount) {
        if(!currency.isPayable()) return null;

        if (creatorName.equals("CONSOLE")) {
            creatorName = UtilString.colorize(plugin.getConfig().getString("cheque.console_name"));
        }
        List<String> formatLore = new ArrayList<>();

        for (String baseLore2 : Objects.requireNonNull(chequeBaseItem.getItemMeta().getLore())) {
            formatLore.add(baseLore2.replace("{value}", currency.format(BigDecimal.valueOf(amount))).replace("{player}", creatorName));
        }
        ItemStack ret = chequeBaseItem.clone();
        ItemMeta meta = ret.getItemMeta();
        meta.setLore(formatLore);
        ChequeStorage storage = new ChequeStorage(creatorName,currency.getPlural(), amount,plugin);
        meta.getPersistentDataContainer().set(ChequeStorage.key, ChequeStorageType.INSTANCE,storage);
        ChequeUpdater.tryApplyFallback(ret, storage); //Backward compatibility
        ret.setItemMeta(meta);
        return ret;
    }

    public boolean isValid(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        return storage != null && StringUtils.isNotBlank(storage.getCurrency())&& StringUtils.isNotBlank(storage.getIssuer());
    }

    public double getValue(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        if(storage != null){
           return storage.getValue();
        }
        return 0;
    }

    /**
     *
     * @param itemstack - The Cheque.
     * @return - Currency it represents.
     */
    /*
    public Currency getCurrency(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        if(storage != null){
            return plugin.getCurrencyManager().getCurrency(storage.getCurrency());
        }
        return plugin.getCurrencyManager().getDefaultCurrency();
    }*/
}
