// ktlint-disable filename
@file:Suppress("ClassName")

package org.partiql.spi.function.builtins

import org.partiql.spi.function.Parameter
import org.partiql.spi.types.PType
import org.partiql.spi.utils.FunctionUtils
import org.partiql.spi.value.Datum

internal val Fn_DATE_ADD_DAY__INT32_DATE__DATE = FunctionUtils.hidden(

    name = "date_add_day",
    returns = PType.date(),
    parameters = arrayOf(
        Parameter("interval", PType.integer()),
        Parameter("datetime", PType.date()),
    ),

) { args ->
    val interval = args[0].int
    val datetime = args[1].localDate
    val datetimeValue = datetime
    val intervalValue = interval.toLong()
    Datum.date(datetimeValue.plusDays(intervalValue))
}

internal val Fn_DATE_ADD_DAY__INT64_DATE__DATE = FunctionUtils.hidden(

    name = "date_add_day",
    returns = PType.date(),
    parameters = arrayOf(
        Parameter("interval", PType.bigint()),
        Parameter("datetime", PType.date()),
    ),

) { args ->
    val interval = args[0].long
    val datetime = args[1].localDate
    val datetimeValue = datetime
    val intervalValue = interval
    Datum.date(datetimeValue.plusDays(intervalValue))
}

internal val Fn_DATE_ADD_DAY__INT32_TIMESTAMP__TIMESTAMP = FunctionUtils.hidden(

    name = "date_add_day",
    returns = PType.timestamp(6),
    parameters = arrayOf(
        Parameter("interval", PType.integer()),
        Parameter("datetime", PType.timestamp(6)),
    ),

) { args ->
    val interval = args[0].int
    val datetime = args[1].localDateTime
    val datetimeValue = datetime
    val intervalValue = interval.toLong()
    Datum.timestamp(datetimeValue.plusDays(intervalValue), 6)
}

internal val Fn_DATE_ADD_DAY__INT64_TIMESTAMP__TIMESTAMP = FunctionUtils.hidden(

    name = "date_add_day",
    returns = PType.timestamp(6),
    parameters = arrayOf(
        Parameter("interval", PType.bigint()),
        Parameter("datetime", PType.timestamp(6)),
    ),

) { args ->
    val interval = args[0].long
    val datetime = args[1].localDateTime
    val datetimeValue = datetime
    val intervalValue = interval
    Datum.timestamp(datetimeValue.plusDays(intervalValue), 6)
}
