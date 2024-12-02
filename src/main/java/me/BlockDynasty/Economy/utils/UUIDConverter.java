package me.BlockDynasty.Economy.utils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID attribute) {
        // Convierte el UUID a String antes de guardarlo
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public UUID convertToEntityAttribute(String dbData) {
        // Convierte el String de la base de datos a UUID
        return dbData == null ? null : UUID.fromString(dbData);
    }
}