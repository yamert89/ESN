package esn.db.converters;

import esn.utils.SimpleUtils;

import javax.persistence.AttributeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        return SimpleUtils.getEncodedString(s);
    }

    @Override
    public String convertToEntityAttribute(String fromBD) {
        return fromBD;
    }
}
