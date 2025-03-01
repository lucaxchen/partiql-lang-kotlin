import org.partiql.ast.Statement;
import org.partiql.eval.Mode;
import org.partiql.eval.compiler.PartiQLCompiler;
import org.partiql.parser.PartiQLParser;
import org.partiql.plan.Plan;
import org.partiql.planner.PartiQLPlanner;
import org.partiql.spi.catalog.Session;
import org.partiql.spi.types.PType;
import org.partiql.spi.value.Datum;

public class TestDatabase {
    public static void main(String[] args) {
        String query = "SELECT d.email FROM data_1 AS d WHERE d.name = 'Bob Smith'";
//        String query = "SELECT d.university.departments.courses.title FROM data d WHERE d.university.departments.courses.id='CS101'";
//        String query = "SELECT university.name FROM data";
//        String query = "SELECT d.company.employees[0].name, d.company.employees[0].projects FROM data_3 AS d WHERE d.company.employees[0].id=1";

//        String query = "SELECT t.a FROM {'a':1, 'b':2} AS t"; // LET query is not supported for now

        // Initiating functional parts of PartiQL engine
        PartiQLParser paser = PartiQLParser.standard();
        PartiQLPlanner planner = PartiQLPlanner.standard();
        PartiQLCompiler compiler = PartiQLCompiler.standard();

        JsonCatalog jsonCatalog = new JsonCatalog();
        String currentCatalog = jsonCatalog.getName();

        Session session = Session.builder()
                .catalogs(jsonCatalog)
                .catalog(currentCatalog)
                .build();

        PartiQLParser.Result parseResult = paser.parse(query);
        Statement statement = parseResult.statements.get(0);

        PartiQLPlanner.Result planResult = planner.plan(statement, session);
        Plan plan =  planResult.getPlan();

        org.partiql.eval.Statement executable = compiler.prepare(plan, Mode.STRICT());

        Datum lazilyEvaluatedData = executable.execute();

        printResult(lazilyEvaluatedData, 2, "\n");


    }

    private static void printResult(Datum d, final int indent, String end) {
        if (d.isNull()) {
            System.out.print(("null" + end).indent(indent));
            return;
        }
        if (d.isMissing()) {
            System.out.print(("missing" + end).indent(indent));
            return;
        }

        PType type = d.getType();
        int typeCode = type.code();
        switch (typeCode) {
            case PType.INTEGER -> {
                System.out.print((d.getInt() + end).indent(indent));
            }
            case PType.DOUBLE -> {
                System.out.print((d.getDouble() + end).indent(indent));
            }
            case PType.BOOL -> {
                System.out.print((d.getBoolean() + end).indent(indent));
            }
            case PType.STRING -> {
                System.out.print(("'" + d.getString() + "'" + end).indent(indent));
            }
            case PType.REAL -> {
                System.out.print((d.getFloat() + end).indent(indent));
            }
            case PType.ARRAY -> {
                System.out.print(("[" + end).indent(indent));
                d.forEach(v -> printResult(v, indent + 2, end));
                System.out.print(("]" + end).indent(indent));
            }
            case PType.BAG -> {
                System.out.print(("<<" + end).indent(indent));
                d.forEach(v -> printResult(v, indent + 2, end));
                System.out.print((">>" + end).indent(indent));
            }
            case PType.STRUCT -> {
                System.out.print(("{" + end).indent(indent));
                d.getFields().forEachRemaining(field -> {
                    System.out.print(("'" + field.getName() + "': ").indent(indent + 2).stripTrailing());
                    printResult(field.getValue(), 0, ",");
                });
                System.out.print(("}" + end).indent(indent));
            }
            // and more...
            default -> {
                System.out.print(("UNHANDLED_TYPE: " + type + end).indent(indent));
            }
        }
    }


}

