/*
 * Copyright Amazon.com, Inc. or its affiliates.  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  You may not use this file except in compliance with the License.
 *  A copy of the License is located at:
 *
 *       http://aws.amazon.com/apache2.0/
 *
 *  or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 *  language governing permissions and limitations under the License.
 */

package org.partiql.lang.planner.transforms

import org.partiql.annotations.ExperimentalPartiQLSchemaInferencer
import org.partiql.lang.SqlException
import org.partiql.lang.ast.UNKNOWN_SOURCE_LOCATION
import org.partiql.lang.domains.PartiqlAst
import org.partiql.lang.errors.ErrorCode
import org.partiql.lang.errors.Problem
import org.partiql.lang.errors.ProblemHandler
import org.partiql.lang.errors.ProblemSeverity
import org.partiql.lang.errors.Property
import org.partiql.lang.errors.PropertyValueMap
import org.partiql.lang.planner.PlanningProblemDetails
import org.partiql.lang.planner.transforms.impl.Metadata
import org.partiql.lang.planner.transforms.plan.PlanTyper
import org.partiql.lang.planner.transforms.plan.PlanUtils
import org.partiql.lang.syntax.PartiQLParserBuilder
import org.partiql.lang.util.propertyValueMapOf
import org.partiql.plan.Rex
import org.partiql.spi.Plugin
import org.partiql.spi.sources.ColumnMetadata
import org.partiql.spi.sources.ValueDescriptor
import org.partiql.spi.sources.ValueDescriptor.TableDescriptor
import kotlin.jvm.Throws

/**
 * Vends functions, such as [infer], to infer the output [ValueDescriptor] of a PartiQL query.
 */
@ExperimentalPartiQLSchemaInferencer
public object PartiQLSchemaInferencer {

    /**
     * Infers a query's schema.
     *
     * As an example, consider the following query:
     * ```partiql
     * SELECT a FROM t
     * ```
     *
     * The inferred [ValueDescriptor] of the above query will resemble a [ValueDescriptor.TableDescriptor] with a
     * single [ColumnMetadata] named "a".
     *
     * Consider another valid PartiQL query:
     * ```partiql
     * 1 + 1
     * ```
     *
     * In the above example, the inferred [ValueDescriptor] will resemble a [ValueDescriptor.TypeDescriptor] that represents
     * the type: [org.partiql.types.IntType].
     *
     * @param query the PartiQL statement to infer
     * @param ctx relevant metadata for inference
     * @return the description of the output data. The return type of [ValueDescriptor] is subject to change.
     * @throws SqlException always throws a [SqlException].
     */
    @JvmStatic
    @Throws(InferenceException::class)
    public fun infer(
        query: String,
        ctx: Context
    ): ValueDescriptor {
        return try {
            inferInternal(query, ctx)
        } catch (t: Throwable) {
            throw when (t) {
                is SqlException -> InferenceException(
                    t.message,
                    t.errorCode,
                    t.errorContext,
                    t.cause
                )
                else -> InferenceException(
                    err = Problem(
                        UNKNOWN_SOURCE_LOCATION,
                        PlanningProblemDetails.CompileError("Unhandled exception occurred.")
                    ),
                    cause = t
                )
            }
        }
    }

    /**
     * Context object required for performing schema inference.
     */
    public class Context(
        public val session: PlannerSession,
        plugins: List<Plugin>,
        public val problemHandler: ProblemHandler = ProblemThrower()
    ) {
        internal val metadata = Metadata(plugins, session.catalogConfig)
    }

    public class InferenceException(
        message: String = "",
        errorCode: ErrorCode,
        errorContext: PropertyValueMap,
        cause: Throwable? = null
    ) : SqlException(message, errorCode, errorContext, cause) {

        constructor(err: Problem, cause: Throwable? = null) :
            this(
                message = "",
                errorCode = ErrorCode.INTERNAL_ERROR,
                errorContext = propertyValueMapOf(
                    Property.LINE_NUMBER to err.sourceLocation.lineNum,
                    Property.COLUMN_NUMBER to err.sourceLocation.charOffset,
                    Property.MESSAGE to err.details.message
                ),
                cause = cause
            )
    }

    //
    //
    // INTERNAL
    //
    //

    internal class ProblemThrower : ProblemHandler {
        override fun handleProblem(problem: Problem) {
            if (problem.details.severity == ProblemSeverity.ERROR) {
                throw InferenceException(problem)
            }
        }
    }

    private const val DEFAULT_TABLE_NAME = "UNSPECIFIED"

    private fun inferInternal(query: String, ctx: Context): ValueDescriptor {
        val parser = PartiQLParserBuilder.standard().build()
        val ast = parser.parseAstStatement(query) as? PartiqlAst.Statement.Query
            ?: TODO("The PartiQLSchemaInferencer only supports inference on SFW queries at the moment.")

        // Transform to Plan
        val plan = AstToPlan.transform(ast)
        val typedPlan = PlanTyper.type(
            plan.root,
            PlanTyper.Context(
                input = null,
                session = ctx.session,
                metadata = ctx.metadata,
                scopingOrder = PlanTyper.ScopingOrder.LEXICAL_THEN_GLOBALS,
                customFunctionSignatures = emptyList(),
                problemHandler = ctx.problemHandler
            )
        )

        // Convert Logical Plan to Value Descriptor
        return convertSchema(typedPlan)
    }

    private fun convertSchema(rex: Rex): ValueDescriptor = when (rex) {
        is Rex.Agg -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Binary -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Call -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Collection.Array -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Collection.Bag -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Id -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Lit -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Path -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Query.Collection -> when (rex.constructor) {
            null -> {
                val attrs = PlanUtils.getSchema(rex.rel).map { attr -> ColumnMetadata(attr.name, attr.type) }
                TableDescriptor(
                    name = DEFAULT_TABLE_NAME,
                    attributes = attrs
                )
            }
            else -> ValueDescriptor.TypeDescriptor(rex.type!!)
        }
        is Rex.Query.Scalar.Subquery -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Query.Scalar.Pivot -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Tuple -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Unary -> ValueDescriptor.TypeDescriptor(rex.type!!)
        is Rex.Switch -> ValueDescriptor.TypeDescriptor(rex.type!!)
    }
}
