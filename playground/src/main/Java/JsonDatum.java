
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.partiql.spi.types.PType;
import org.partiql.spi.value.Datum;
import org.partiql.spi.value.Field;
import org.partiql.spi.value.InvalidOperationException;

import java.util.Iterator;
import java.util.stream.StreamSupport;

public class JsonDatum implements Datum {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Datum.class, new DatumDeserializer()).create();

    private final JsonElement json;
    private PType type;

    public JsonDatum(JsonElement json){
        this.json = json;
    }

    @Override
    public boolean isNull(){
        return json.isJsonNull();
    }

    @NotNull
    @Override
    public PType getType() {
//        System.out.println("Getting into TYPE assignment");
        if (type == null) { // No type is assigned at the first time
            type = PType.unknown();
            if (json.isJsonNull()) {
                type = PType.unknown();
            } else if (json.isJsonObject()) {
                type = PType.struct();
            } else if (json.isJsonArray()) {
//                System.out.println("Array type is assigned");
                type = PType.array();
            } else if (json.isJsonPrimitive()) {
                var prim = json.getAsJsonPrimitive();
                if (prim.isString()) {
                    type = PType.string();
                } else if (prim.isBoolean()) {
                    type = PType.bool();
                } else if (prim.isNumber()) {
                    // If decimals are required, could use something here like `type = PType.decimal()`
                    //   and override `getBigDecimal` rather than `getDouble`
                    type = PType.integer();
                }
            }
        }
        return type;
    }

    @NotNull
    @Override
    public String getString() throws InvalidOperationException, NullPointerException{
        if(getType().equals(PType.string())){
            return json.getAsString();
        }else {
            return Datum.super.getString();
        }

    }

    @Override
    public boolean getBoolean() throws InvalidOperationException, NullPointerException{
        if(getType().equals(PType.bool())){
            return json.getAsBoolean();
        }else {
            return Datum.super.getBoolean();
        }
    }

    @Override
    public int getInt(){
        if(getType().equals(PType.integer())){
            return json.getAsInt();
        }else {
            return Datum.super.getInt();
        }
    }

    @NotNull
    @Override
    public Iterator<Datum> iterator(){
        if(getType().equals(PType.array())){
            return StreamSupport.stream(json.getAsJsonArray().spliterator(), false)
                    .map(elt -> (Datum) new JsonDatum(elt))
                    .iterator();
        }else {
            return Datum.super.iterator();
        }
    }

    @NotNull
    @Override
    public Iterator<Field> getFields(){
        if(getType().equals(PType.struct())){
            return json.getAsJsonObject().entrySet().stream()
                    .map(entry -> Field.of(entry.getKey(), new JsonDatum(entry.getValue())))
                    .iterator();
        }else {
            return Datum.super.getFields();
        }
    }

    @Override
    public Datum get(@NotNull String name){
        if(getType().equals(PType.struct())){
            return new JsonDatum(json.getAsJsonObject().get(name));
        }else {
            return Datum.super.get(name);
        }
    }

    @Override
    public Datum getInsensitive(@NotNull String name) {
        if (getType().equals(PType.struct())) {
            return json.getAsJsonObject().entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                    .findFirst()
                    .map(entry -> (Datum) new JsonDatum(entry.getValue()))
                    .orElseGet(Datum::missing);
        } else {
            return Datum.super.getInsensitive(name); // Allow default to throw error
        }
    }

    @Override
    public Datum lower() throws InvalidOperationException, NullPointerException {
        return gson.fromJson(this.json, Datum.class);
    }

}
