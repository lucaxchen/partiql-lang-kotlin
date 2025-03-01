// ktlint-disable filename
@file:Suppress("ClassName")

package org.partiql.spi.function.builtins

import org.partiql.spi.function.Fn
import org.partiql.spi.function.builtins.internal.PErrors
import org.partiql.spi.types.PType
import org.partiql.spi.utils.NumberUtils.byteOverflows
import org.partiql.spi.utils.NumberUtils.shortOverflows
import org.partiql.spi.value.Datum

internal object FnTimes : DiadicArithmeticOperator("times") {

    init {
        fillTable()
    }

    override fun getTinyIntInstance(tinyIntLhs: PType, tinyIntRhs: PType): Fn {
        return basic(PType.tinyint()) { args ->
            val arg0 = args[0].byte
            val arg1 = args[1].byte
            val result = arg0 * arg1
            if (result.byteOverflows()) {
                throw PErrors.numericValueOutOfRangeException("$arg0 * $arg1", PType.tinyint())
            } else {
                Datum.tinyint(result.toByte())
            }
        }
    }

    override fun getSmallIntInstance(smallIntLhs: PType, smallIntRhs: PType): Fn {
        return basic(PType.smallint()) { args ->
            val arg0 = args[0].short
            val arg1 = args[1].short
            val result = arg0 * arg1
            if (result.shortOverflows()) {
                throw PErrors.numericValueOutOfRangeException("$arg0 * $arg1", PType.smallint())
            } else {
                Datum.smallint(result.toShort())
            }
        }
    }

    override fun getIntegerInstance(integerLhs: PType, integerRhs: PType): Fn {
        return basic(PType.integer()) { args ->
            val arg0 = args[0].int
            val arg1 = args[1].int
            try {
                val result = Math.multiplyExact(arg0, arg1)
                return@basic Datum.integer(result)
            } catch (e: ArithmeticException) {
                throw PErrors.numericValueOutOfRangeException("$arg0 * $arg1", PType.integer())
            }
        }
    }

    override fun getBigIntInstance(bigIntLhs: PType, bigIntRhs: PType): Fn {
        return basic(PType.bigint()) { args ->
            val arg0 = args[0].long
            val arg1 = args[1].long
            try {
                val result = Math.multiplyExact(arg0, arg1)
                return@basic Datum.bigint(result)
            } catch (e: ArithmeticException) {
                throw PErrors.numericValueOutOfRangeException("$arg0 * $arg1", PType.bigint())
            }
        }
    }

    override fun getNumericInstance(numericLhs: PType, numericRhs: PType): Fn {
        val (p, s) = timesPrecisionScale(numericLhs, numericRhs)
        return basic(PType.numeric(p, s), numericLhs, numericRhs) { args ->
            val arg0 = args[0].bigDecimal
            val arg1 = args[1].bigDecimal
            Datum.numeric(arg0 * arg1, p, s)
        }
    }

    override fun getDecimalInstance(decimalLhs: PType, decimalRhs: PType): Fn {
        val (p, s) = timesPrecisionScale(decimalLhs, decimalRhs)
        return basic(PType.decimal(p, s), decimalLhs, decimalRhs) { args ->
            val arg0 = args[0].bigDecimal
            val arg1 = args[1].bigDecimal
            Datum.decimal(arg0 * arg1, p, s)
        }
    }

    /**
     * SQL Server:
     * p = p1 + p2 + 1
     * s = s1 + s2
     */
    private fun timesPrecisionScale(lhs: PType, rhs: PType): Pair<Int, Int> {
        val (p1, s1) = lhs.precision to lhs.scale
        val (p2, s2) = rhs.precision to rhs.scale
        val p = p1 + p2 + 1
        val s = s1 + s2
        val returnedP = p.coerceAtMost(38)
        val returnedS = s.coerceAtMost(p)
        return returnedP to returnedS
    }

    override fun getRealInstance(realLhs: PType, realRhs: PType): Fn {
        return basic(PType.real()) { args ->
            val arg0 = args[0].float
            val arg1 = args[1].float
            Datum.real(arg0 * arg1)
        }
    }

    override fun getDoubleInstance(doubleLhs: PType, doubleRhs: PType): Fn {
        return basic(PType.doublePrecision()) { args ->
            val arg0 = args[0].double
            val arg1 = args[1].double
            Datum.doublePrecision(arg0 * arg1)
        }
    }
}
