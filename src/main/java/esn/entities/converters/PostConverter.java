package esn.entities.converters;

import javax.persistence.AttributeConverter;
import java.util.Base64;

public class PostConverter implements AttributeConverter<byte[], String> {
    @Override
    public String convertToDatabaseColumn(byte[] attribute) {
        return Base64.getEncoder().encodeToString(attribute);
    }

    @Override
    public byte[] convertToEntityAttribute(String dbData) {
        return new byte[0];
    }
}
