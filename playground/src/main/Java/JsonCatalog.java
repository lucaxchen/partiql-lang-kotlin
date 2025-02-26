import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.partiql.spi.catalog.Catalog;
import org.partiql.spi.catalog.Identifier;
import org.partiql.spi.catalog.Name;
import org.partiql.spi.catalog.Namespace;
import org.partiql.spi.catalog.Session;
import org.partiql.spi.catalog.Table;
import org.partiql.spi.function.AggOverload;
import org.partiql.spi.function.FnOverload;
import java.util.Collection;
import java.util.List;

final class JsonCatalog implements Catalog {

    private final String dataPath = "/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/main/resources/data/";


    @NotNull
    @Override
    public String getName() {
        return "JSON_EXAMPLE"; // Should be dynamic
    }

    @Nullable
    @Override
    public Table getTable(@NotNull Session session, @NotNull Name name) {
        String tableName = name.getName();
        var table = new JsonTable(name, dataPath + tableName + ".jsonl");
        return table;
    }

    @Nullable
    @Override
    public Name resolveTable(@NotNull Session session, @NotNull Identifier identifier) {
//        final Namespace demo = Namespace.Companion.of("data");
        Name name = switch (identifier.toString()){
            case "data_1" -> Name.of("data_1");
            case "data_2" -> Name.of("data_2");
            case "data_3" -> Name.of("data_3");
            default -> throw new IllegalArgumentException("Unknown Table" + identifier);
        };

        return name;
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
