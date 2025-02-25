import com.google.gson.JsonStreamParser;
import org.jetbrains.annotations.NotNull;
import org.partiql.spi.catalog.Name;
import org.partiql.spi.catalog.Table;
import org.partiql.spi.types.PType;
import org.partiql.spi.value.Datum;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public final class JsonTable implements Table {

    private final Name name;
    private final String dataPath;

    public JsonTable(@NotNull Name name, @NotNull String dataPath){
        this.name = name;
        this.dataPath = dataPath;
    }


    @Override
    public @NotNull Name getName() {
        return this.name;
    }

    @Override
    public @NotNull PType getSchema() {
        return PType.dynamic(); // using dynamic typing means,
    }

    @Override
    public @NotNull Datum getDatum() {
        return getData();
    }

    private Datum getData(){
        var rows = new ArrayList<Datum>();

        File dataFile = new File(dataPath);
        if(!dataFile.exists() || !dataFile.isFile()){
            throw new RuntimeException("Internal error: there isn't a data file for " + this.dataPath + ".");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            JsonStreamParser reader = new JsonStreamParser(br);

            while (reader.hasNext()) {
                rows.add(new JsonDatum(reader.next()));
            }
        } catch (Exception e){
            System.out.println("Internal error: the File Reader isn't working correctly");
        }

        return Datum.bag(rows);
    }

}
