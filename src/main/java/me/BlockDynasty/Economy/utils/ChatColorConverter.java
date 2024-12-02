package me.BlockDynasty.Economy.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bukkit.ChatColor;

@Converter(autoApply = true)
public class ChatColorConverter implements AttributeConverter<ChatColor, String> {

    @Override
    public String convertToDatabaseColumn(ChatColor color) {
        return color != null ? color.name() : null;
    }

    @Override
    public ChatColor convertToEntityAttribute(String dbData) {
        return dbData != null ? ChatColor.valueOf(dbData) : null;
    }
}