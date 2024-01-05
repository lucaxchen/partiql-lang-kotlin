package org.partiql.plugin.internal.fn.scalar

import org.partiql.value.ClobValue
import org.partiql.value.DecimalValue
import org.partiql.value.Float32Value
import org.partiql.value.Float64Value
import org.partiql.value.Int16Value
import org.partiql.value.Int32Value
import org.partiql.value.Int64Value
import org.partiql.value.Int8Value
import org.partiql.value.IntValue
import org.partiql.value.PartiQLValue
import org.partiql.value.PartiQLValueExperimental
import org.partiql.value.StringValue
import org.partiql.value.SymbolValue
import org.partiql.value.check
import org.partiql.value.clobValue
import org.partiql.value.decimalValue
import org.partiql.value.float32Value
import org.partiql.value.float64Value
import org.partiql.value.int16Value
import org.partiql.value.int32Value
import org.partiql.value.int64Value
import org.partiql.value.int8Value
import org.partiql.value.intValue
import org.partiql.value.stringValue
import org.partiql.value.symbolValue
import java.math.BigDecimal
import java.math.BigInteger

@OptIn(PartiQLValueExperimental::class)
@JvmName("binaryOpInt8ByteToInt")
internal inline fun binaryOpInt8(lhs: PartiQLValue, rhs: PartiQLValue, op: (Byte, Byte) -> Int): Int8Value {
    val lhsValue = lhs.check<Int8Value>().value!!
    val rhsValue = rhs.check<Int8Value>().value!!
    return int8Value((op(lhsValue, rhsValue)).toByte())
}

@OptIn(PartiQLValueExperimental::class)
@JvmName("binaryOpInt8ByteToByte")
internal inline fun binaryOpInt8(lhs: PartiQLValue, rhs: PartiQLValue, op: (Byte, Byte) -> Byte): Int8Value {
    val lhsValue = lhs.check<Int8Value>().value!!
    val rhsValue = rhs.check<Int8Value>().value!!
    return int8Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
@JvmName("binaryOpInt16ShortToInt")
internal inline fun binaryOpInt16(lhs: PartiQLValue, rhs: PartiQLValue, op: (Short, Short) -> Int): Int16Value {
    val lhsValue = lhs.check<Int16Value>().value!!
    val rhsValue = rhs.check<Int16Value>().value!!
    return int16Value((op(lhsValue, rhsValue)).toShort())
}

@OptIn(PartiQLValueExperimental::class)
@JvmName("binaryOpInt16ShortToShort")
internal inline fun binaryOpInt16(lhs: PartiQLValue, rhs: PartiQLValue, op: (Short, Short) -> Short): Int16Value {
    val lhsValue = lhs.check<Int16Value>().value!!
    val rhsValue = rhs.check<Int16Value>().value!!
    return int16Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpInt32(lhs: PartiQLValue, rhs: PartiQLValue, op: (Int, Int) -> Int): Int32Value {
    val lhsValue = lhs.check<Int32Value>().value!!
    val rhsValue = rhs.check<Int32Value>().value!!
    return int32Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpInt64(lhs: PartiQLValue, rhs: PartiQLValue, op: (Long, Long) -> Long): Int64Value {
    val lhsValue = lhs.check<Int64Value>().value!!
    val rhsValue = rhs.check<Int64Value>().value!!
    return int64Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpInt(lhs: PartiQLValue, rhs: PartiQLValue, op: (BigInteger, BigInteger) -> BigInteger): IntValue {
    val lhsValue = lhs.check<IntValue>().value!!
    val rhsValue = rhs.check<IntValue>().value!!
    return intValue((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpDecimal(lhs: PartiQLValue, rhs: PartiQLValue, op: (BigDecimal, BigDecimal) -> BigDecimal): DecimalValue {
    val lhsValue = lhs.check<DecimalValue>().value!!
    val rhsValue = rhs.check<DecimalValue>().value!!
    return decimalValue((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpFloat32(lhs: PartiQLValue, rhs: PartiQLValue, op: (Float, Float) -> Float): Float32Value {
    val lhsValue = lhs.check<Float32Value>().value!!
    val rhsValue = rhs.check<Float32Value>().value!!
    return float32Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpFloat64(lhs: PartiQLValue, rhs: PartiQLValue, op: (Double, Double) -> Double): Float64Value {
    val lhsValue = lhs.check<Float64Value>().value!!
    val rhsValue = rhs.check<Float64Value>().value!!
    return float64Value((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpString(lhs: PartiQLValue, rhs: PartiQLValue, op: (String, String) -> String): StringValue {
    val lhsValue = lhs.check<StringValue>().value!!
    val rhsValue = rhs.check<StringValue>().value!!
    return stringValue((op(lhsValue, rhsValue)))
}

// TODO: We are still debating on whether symbol is a value. It looks like it may not be, and therefore, this
//  will be removed.
@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpSymbol(lhs: PartiQLValue, rhs: PartiQLValue, op: (String, String) -> String): SymbolValue {
    val lhsValue = lhs.check<SymbolValue>().value!!
    val rhsValue = rhs.check<SymbolValue>().value!!
    return symbolValue((op(lhsValue, rhsValue)))
}

@OptIn(PartiQLValueExperimental::class)
internal inline fun binaryOpClob(lhs: PartiQLValue, rhs: PartiQLValue, op: (ByteArray, ByteArray) -> ByteArray): ClobValue {
    val lhsValue = lhs.check<ClobValue>().value!!
    val rhsValue = rhs.check<ClobValue>().value!!
    return clobValue((op(lhsValue, rhsValue)))
}
