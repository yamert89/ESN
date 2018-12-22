package esn.db.converters;

import esn.utils.SimpleUtils;

import javax.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<byte[], byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(byte[] input) {
        return SimpleUtils.getEncodedPassword(input);
    }

    @Override
    public byte[] convertToEntityAttribute(byte[] fromBD) {
        return fromBD;

    }
}
