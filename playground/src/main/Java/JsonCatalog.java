import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.partiql.spi.catalog.Catalog;
import org.partiql.spi.catalog.Identifier;
import org.partiql.spi.catalog.Name;
import org.partiql.spi.catalog.Session;
import org.partiql.spi.catalog.Table;
import org.partiql.spi.function.AggOverload;
import org.partiql.spi.function.FnOverload;
import java.util.Collection;
import java.util.List;

final class JsonCatalog implements Catalog {

    private final String dataPath = "/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/main/resources/data/data_3.json";


    @NotNull
    @Override
    public String getName() {
        return "JSON_EXAMPLE";
    }

    @Nullable
    @Override
    public Table getTable(@NotNull Session session, @NotNull Name name) {
        return new JsonTable(Name.of(getName()), dataPath);
    }

    @Nullable
    @Override
    public Name resolveTable(@NotNull Session session, @NotNull Identifier identifier) {
        return Name.of(getName());
    }

    @NotNull
    @Override
    public Collection<FnOverload> getFunctions(@NotNull Session session, @NotNull String s) {
        return List.of();
    }

    @NotNull
    @Override
    public Collection<AggOverload> getAggregations(@NotNull Session session, @NotNull String s) {
        return List.of();
    }
}
