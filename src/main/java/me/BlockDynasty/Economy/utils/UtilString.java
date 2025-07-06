

package me.BlockDynasty.Economy.utils;

import com.google.common.collect.Lists;
import me.BlockDynasty.Economy.config.file.F;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class UtilString {

    public static boolean validateInput(CommandSender sender, String input) {
        double amount;
        try {
            amount = Double.parseDouble(input);
            if (amount < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getPrefix() + F.getUnvalidAmount());
            return false;
        }
        return true;
    }

    public static String format(double money) {
        DecimalFormat format = new DecimalFormat();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(symbols);
        format.setGroupingUsed(true);
        format.setGroupingSize(3);
        double roundOff = Math.round(money * 100.0) / 100.0;
        return format.format(roundOff);
    }

    public static String colorize(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colorize(List<String> message){
        List<String> colorizedList = Lists.newArrayList();
        for(String str : message){
            colorizedList.add(colorize(str));
        }
        return colorizedList;
    }

}
