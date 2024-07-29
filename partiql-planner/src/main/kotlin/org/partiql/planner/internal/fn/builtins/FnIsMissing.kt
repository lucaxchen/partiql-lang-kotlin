// ktlint-disable filename
@file:Suppress("ClassName")

package org.partiql.planner.internal.fn.builtins

import org.partiql.planner.internal.fn.Fn
import org.partiql.planner.internal.fn.FnParameter
import org.partiql.planner.internal.fn.FnSignature
import org.partiql.value.MissingValue
import org.partiql.value.PartiQLValue
import org.partiql.value.PartiQLValueExperimental
import org.partiql.value.PartiQLValueType.ANY
import org.partiql.value.PartiQLValueType.BOOL
import org.partiql.value.boolValue

@OptIn(PartiQLValueExperimental::class)
internal object Fn_IS_MISSING__ANY__BOOL : Fn {

    override val signature = FnSignature(
        name = "is_missing",
        returns = BOOL,
        parameters = listOf(FnParameter("value", ANY)),
        isNullable = false,
        isNullCall = false,
        isMissable = false,
        isMissingCall = false,
    )

    override fun invoke(args: Array<PartiQLValue>): PartiQLValue {
        return boolValue(args[0] is MissingValue)
    }
}
