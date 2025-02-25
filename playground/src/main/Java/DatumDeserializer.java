import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.partiql.spi.value.Datum;
import org.partiql.spi.value.Field;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

/// Very simple demo deserializer from JSON to `Datum`
class DatumDeserializer implements JsonDeserializer<Datum> {
    public Datum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonNull()) {
            return Datum.nullValue();
        } else if (json.isJsonObject()) {
            var obj = json.getAsJsonObject();
            Iterable<Field> fields = obj.entrySet().stream().map(entry -> {
                var key = entry.getKey();
                var value = (Datum) context.deserialize(entry.getValue(), typeOfT);
                return Field.of(key, value);
            }).collect(Collectors.toList());
            return Datum.struct(fields);
        } else if (json.isJsonArray()) {
            var arr = json.getAsJsonArray();
            var list = arr
                    .asList()
                    .stream()
                    .map(entry -> (Datum) context.deserialize(entry, typeOfT))
                    .collect(Collectors.toList());
            return Datum.array(list);
        } else if (json.isJsonPrimitive()) {
            var prim = json.getAsJsonPrimitive();
            if (prim.isString()) {
                return Datum.string(prim.getAsString());
            } else if (prim.isBoolean()) {
                return Datum.bool(prim.getAsBoolean());
            } else if (prim.isNumber()) {
                // If decimals are required, could use something here like `Datum.decimal(prim.getAsBigDecimal())`
                return Datum.doublePrecision(prim.getAsDouble());
            }
        }

        throw new IllegalArgumentException("Unknown datum: " + json);
    }
}