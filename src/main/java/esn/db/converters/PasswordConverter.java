package esn.db.converters;

import esn.utils.SimpleUtils;

import javax.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String input) {
        String b = new String(SimpleUtils.getEncodedPassword(input));
        return b;
    }

    @Override
    public String convertToEntityAttribute(String fromBD) {
        //String s = new String(fromBD);
        return fromBD;

    }
}
