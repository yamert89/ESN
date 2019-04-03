package esn.entities.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class JsonTimeSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(prepUnit(timestamp.getHours()) + ":" + prepUnit(timestamp.getMinutes()) + ":" +
                prepUnit(timestamp.getSeconds()) + " / " +
                prepUnit(timestamp.getDate()) + "." + prepUnit(timestamp.getMonth() + 1));
    }

    private String prepUnit(int unit){
        return unit < 10 ? "0" + unit : String.valueOf(unit);
    }
}
