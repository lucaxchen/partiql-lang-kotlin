// ktlint-disable filename
@file:Suppress("ClassName")

package org.partiql.planner.internal.fn.builtins

import org.partiql.planner.internal.fn.Fn
import org.partiql.planner.internal.fn.FnParameter
import org.partiql.planner.internal.fn.FnSignature
import org.partiql.value.Float32Value
import org.partiql.value.Float64Value
import org.partiql.value.PartiQLValue
import org.partiql.value.PartiQLValueExperimental
import org.partiql.value.PartiQLValueType.ANY
import org.partiql.value.PartiQLValueType.BOOL
import org.partiql.value.boolValue

@OptIn(PartiQLValueExperimental::class)
internal object Fn_IS_FLOAT32__ANY__BOOL : Fn {

    override val signature = FnSignature(
        name = "is_float32",
        returns = BOOL,
        parameters = listOf(FnParameter("value", ANY)),
        isNullCall = true,
        isNullable = false,
    )

    override fun invoke(args: Array<PartiQLValue>): PartiQLValue {
        return when (val arg = args[0]) {
            is Float32Value -> boolValue(true)
            is Float64Value -> {
                val v = arg.value!!
                boolValue(Float.MIN_VALUE <= v && v <= Float.MAX_VALUE)
            }
            else -> boolValue(false)
        }
    }
}
