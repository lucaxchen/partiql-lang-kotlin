import org.partiql.ast.Statement
import org.partiql.cli.ErrorCodeString
import org.partiql.cli.pipeline.AppPErrorListener
import org.partiql.cli.pipeline.ErrorMessageFormatter
import org.partiql.eval.Mode
import org.partiql.eval.compiler.PartiQLCompiler
import org.partiql.parser.PartiQLParser
import org.partiql.plan.Plan
import org.partiql.planner.PartiQLPlanner
import org.partiql.spi.Context
import org.partiql.spi.catalog.Session
import org.partiql.spi.errors.PError
import org.partiql.spi.errors.PErrorKind
import org.partiql.spi.errors.PRuntimeException
import org.partiql.spi.errors.Severity
import org.partiql.spi.value.Datum
import java.io.PrintStream
import kotlin.jvm.Throws

class Pipeline private constructor(
    private val parser: PartiQLParser,
    private val planner: PartiQLPlanner,
    private val compiler: PartiQLCompiler,
    private val ctx: Context,
    private val mode: Mode,
){

    @Throws(PipelineException::class)
    fun execute(statement: String, session: Session): Datum{
        val ast = parse(statement)
        val plan = plan(ast, session)
        return execute(plan, session)
    }

    private fun parse(source: String): Statement {
        val result = listen(ctx.errorListener as AppPErrorListener){
            parser.parse(source)
        }
        if(result.statements.size != 1){
            throw PipelineException("there are more than one statement")
        }
        return result.statements[0]
    }

    private fun plan(statement: Statement, session: Session): Plan{
        val result = listen(ctx.errorListener as AppPErrorListener){
            planner.plan(statement,session, ctx)
        }
        return result.plan
    }

    private fun execute(plan: Plan, session: Session): Datum{
        val statement = listen(ctx.errorListener as AppPErrorListener){
            compiler.prepare(plan, mode, ctx)
        }
        return listen(ctx.errorListener as AppPErrorListener){
            statement.execute()
        }
    }

    private fun <T> listen(listener: AppPErrorListener, action: () -> T): T{
        listener.clear()
        val result = try{
            action.invoke()
        } catch (e: PipelineException){
            throw e
        } catch (e: PRuntimeException){
            val message = ErrorMessageFormatter.message(e.error)
            throw PipelineException(message)
        }
        if(listener.hasErrors()){
            throw PipelineException("Failed with given input, please check the above errors!")
        }
        return result
    }

    companion object{

        fun default(out: PrintStream, config: Config): Pipeline {
            return create(Mode.PERMISSIVE(), out, config)
        }

        fun strict(out: PrintStream, config: Config): Pipeline {
            return create(Mode.STRICT(), out, config)
        }

        private fun create(mode: Mode, out: PrintStream, config: Config): Pipeline {
            val listener = config.getErrorListener(out)
            val ctx = Context.of(listener)
            val parser = PartiQLParser.Builder().build()
            val planner = PartiQLPlanner.builder().build()
            val compiler = PartiQLCompiler.builder().build()
            return Pipeline(parser, planner, compiler, ctx, mode)
        }
    }

    class PipelineException(override val message: String?) : PRuntimeException(
        PError(PError.INTERNAL_ERROR, Severity.ERROR(), PErrorKind.EXECUTION(), null, null)
    )

    class Config(
        private val maxErrors: Int,
        private val inhibitWarnings: Boolean,
        private val warningsAsErrors: Array<ErrorCodeString>
    ){
        fun getErrorListener(out: PrintStream): AppPErrorListener{
            return AppPErrorListener(out, maxErrors, inhibitWarnings, warningsAsErrors)
        }
    }
}