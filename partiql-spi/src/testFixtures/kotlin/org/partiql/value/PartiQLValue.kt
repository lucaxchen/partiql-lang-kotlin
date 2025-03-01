/*
 * Copyright Amazon.com, Inc. or its affiliates.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at:
 *
 *      http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.partiql.value

import com.amazon.ionelement.api.IonElement
import org.partiql.spi.errors.PError
import org.partiql.spi.errors.PErrorKind
import org.partiql.spi.errors.PRuntimeException
import org.partiql.spi.errors.Severity
import org.partiql.value.datetime.Date
import org.partiql.value.datetime.Time
import org.partiql.value.datetime.Timestamp
import org.partiql.value.helpers.ToIon
import org.partiql.value.util.PartiQLValueVisitor
import java.math.BigDecimal
import java.math.BigInteger
import java.util.BitSet

public typealias Annotations = List<String>

/**
 * TODO
 *  - Implement ANY
 *  - Implement comparators
 */
public sealed interface PartiQLValue {

    public val type: PartiQLValueType

    public val annotations: Annotations

    public val isNull: Boolean

    public fun copy(annotations: Annotations = this.annotations): PartiQLValue

    public fun withAnnotations(annotations: Annotations): PartiQLValue

    public fun withoutAnnotations(): PartiQLValue

    public fun <R, C> accept(visitor: PartiQLValueVisitor<R, C>, ctx: C): R

    public companion object {
        /**
         * Provides a total, natural ordering over [PartiQLValue] as defined by section 12.2 of the PartiQL specification
         * (https://partiql.org/assets/PartiQL-Specification.pdf#subsection.12.2). PartiQL treats Ion typed nulls as `NULL`
         * for the purposes of comparisons and Ion annotations are not considered for comparison purposes.
         *
         * The ordering rules are as follows:
         *
         *  * [NullValue] and [MissingValue] are always first or last and compare equally.  In other words,
         *    comparison cannot distinguish between `NULL` or `MISSING`.
         *  * The [BoolValue] values follow with `false` coming before `true`.
         *  * The [NumericValue] types come next ordered by their numerical value irrespective
         *    of precision or specific type.
         *      For `FLOAT` special values, `nan` comes before `-inf`, which comes before all normal
         *      numeric values, which is followed by `+inf`.
         *  * [DateValue] values follow and are compared by the date from earliest to latest.
         *  * [TimeValue] values follow and are compared by the time of the day (point of time in a day of 24 hours)
         *      from earliest to latest. Note that time without time zone is not directly comparable with time with time zone.
         *  * [TimestampValue] values follow and are compared by the point of time irrespective of precision and
         *    local UTC offset.
         *  * The [TextValue] types come next ordered by their lexicographical ordering by
         *    Unicode scalar irrespective of their specific type.
         *  * The [BlobValue] and [ClobValue] types follow and are ordered by their lexicographical ordering
         *    by octet.
         *  * [ListValue] comes next, and their values compare lexicographically based on their
         *    child elements recursively based on this definition.
         *  * [SexpValue] follows and compares within its type similar to `LIST`.
         *  * [StructValue] values follow and compare lexicographically based on the *sorted*
         *    (as defined by this definition) members, as pairs of field name and the member value.
         *  * [BagValue] values come finally (except with [nullsFirst] == true), and their values
         *    compare lexicographically based on the *sorted* child elements.
         *
         * @param nullsFirst whether [NullValue], [MissingValue], and typed Ion null values come first
         */
        @JvmStatic
        @JvmOverloads
        @Deprecated("This will be removed in a future major-version release.")
        public fun comparator(nullsFirst: Boolean = true): Comparator<PartiQLValue> = PartiQLValueComparatorInternal(nullsFirst)
    }
}

public sealed interface ScalarValue<T> : PartiQLValue {

    public val value: T?

    override val isNull: Boolean
        get() = value == null

    override fun copy(annotations: Annotations): ScalarValue<T>

    override fun withAnnotations(annotations: Annotations): ScalarValue<T>

    override fun withoutAnnotations(): ScalarValue<T>
}

public sealed interface CollectionValue<T : PartiQLValue> : PartiQLValue, Iterable<T> {

    override val isNull: Boolean

    override fun iterator(): Iterator<T>

    override fun copy(annotations: Annotations): CollectionValue<T>

    override fun withAnnotations(annotations: Annotations): CollectionValue<T>

    override fun withoutAnnotations(): CollectionValue<T>
}

public abstract class BoolValue : ScalarValue<Boolean?> {

    override val type: PartiQLValueType = PartiQLValueType.BOOL

    abstract override fun copy(annotations: Annotations): BoolValue

    abstract override fun withAnnotations(annotations: Annotations): BoolValue

    abstract override fun withoutAnnotations(): BoolValue
}

public sealed class NumericValue<T : Number> : ScalarValue<T> {

    public abstract fun toInt8(): Int8Value

    public abstract fun toInt16(): Int16Value

    public abstract fun toInt32(): Int32Value

    public abstract fun toInt64(): Int64Value

    public abstract fun toInt(): IntValue

    public abstract fun toDecimal(): DecimalValue

    public abstract fun toFloat32(): Float32Value

    public abstract fun toFloat64(): Float64Value

    abstract override fun copy(annotations: Annotations): NumericValue<T>

    abstract override fun withAnnotations(annotations: Annotations): NumericValue<T>

    abstract override fun withoutAnnotations(): NumericValue<T>
}

public abstract class Int8Value : NumericValue<Byte>() {

    override val type: PartiQLValueType = PartiQLValueType.INT8

    abstract override fun copy(annotations: Annotations): Int8Value

    abstract override fun withAnnotations(annotations: Annotations): Int8Value

    abstract override fun withoutAnnotations(): Int8Value
}

public abstract class Int16Value : NumericValue<Short>() {

    override val type: PartiQLValueType = PartiQLValueType.INT16

    abstract override fun copy(annotations: Annotations): Int16Value

    abstract override fun withAnnotations(annotations: Annotations): Int16Value

    abstract override fun withoutAnnotations(): Int16Value
}

public abstract class Int32Value : NumericValue<Int>() {

    override val type: PartiQLValueType = PartiQLValueType.INT32

    abstract override fun copy(annotations: Annotations): Int32Value

    abstract override fun withAnnotations(annotations: Annotations): Int32Value

    abstract override fun withoutAnnotations(): Int32Value
}

public abstract class Int64Value : NumericValue<Long>() {

    override val type: PartiQLValueType = PartiQLValueType.INT64

    abstract override fun copy(annotations: Annotations): Int64Value

    abstract override fun withAnnotations(annotations: Annotations): Int64Value

    abstract override fun withoutAnnotations(): Int64Value
}

public abstract class IntValue : NumericValue<BigInteger>() {

    override val type: PartiQLValueType = PartiQLValueType.INT

    abstract override fun copy(annotations: Annotations): IntValue

    abstract override fun withAnnotations(annotations: Annotations): IntValue

    abstract override fun withoutAnnotations(): IntValue
}

public abstract class DecimalValue : NumericValue<BigDecimal>() {

    override val type: PartiQLValueType = PartiQLValueType.DECIMAL

    abstract override fun copy(annotations: Annotations): DecimalValue

    abstract override fun withAnnotations(annotations: Annotations): DecimalValue

    abstract override fun withoutAnnotations(): DecimalValue
}

public abstract class Float32Value : NumericValue<Float>() {

    override val type: PartiQLValueType = PartiQLValueType.FLOAT32

    abstract override fun copy(annotations: Annotations): Float32Value

    abstract override fun withAnnotations(annotations: Annotations): Float32Value

    abstract override fun withoutAnnotations(): Float32Value
}

public abstract class Float64Value : NumericValue<Double>() {

    override val type: PartiQLValueType = PartiQLValueType.FLOAT64

    abstract override fun copy(annotations: Annotations): Float64Value

    abstract override fun withAnnotations(annotations: Annotations): Float64Value

    abstract override fun withoutAnnotations(): Float64Value
}

public sealed class TextValue<T> : ScalarValue<T> {

    public abstract val string: String?

    abstract override fun copy(annotations: Annotations): TextValue<T>

    abstract override fun withAnnotations(annotations: Annotations): TextValue<T>

    abstract override fun withoutAnnotations(): TextValue<T>
}

public abstract class CharValue : TextValue<Char>() {

    override val type: PartiQLValueType = PartiQLValueType.CHAR

    override val string: String?
        get() = value?.toString()

    abstract override fun copy(annotations: Annotations): CharValue

    abstract override fun withAnnotations(annotations: Annotations): CharValue

    abstract override fun withoutAnnotations(): CharValue
}

public abstract class StringValue : TextValue<String>() {

    override val type: PartiQLValueType = PartiQLValueType.STRING

    override val string: String?
        get() = value

    abstract override fun copy(annotations: Annotations): StringValue

    abstract override fun withAnnotations(annotations: Annotations): StringValue

    abstract override fun withoutAnnotations(): StringValue
}

public abstract class SymbolValue : TextValue<String>() {

    override val type: PartiQLValueType = PartiQLValueType.SYMBOL

    override val string: String?
        get() = value

    abstract override fun copy(annotations: Annotations): SymbolValue

    abstract override fun withAnnotations(annotations: Annotations): SymbolValue

    abstract override fun withoutAnnotations(): SymbolValue
}

public abstract class ClobValue : ScalarValue<ByteArray> {

    override val type: PartiQLValueType = PartiQLValueType.CLOB

    abstract override fun copy(annotations: Annotations): ClobValue

    abstract override fun withAnnotations(annotations: Annotations): ClobValue

    abstract override fun withoutAnnotations(): ClobValue
}

public abstract class BinaryValue : ScalarValue<BitSet> {

    override val type: PartiQLValueType = PartiQLValueType.BINARY

    abstract override fun copy(annotations: Annotations): BinaryValue

    abstract override fun withAnnotations(annotations: Annotations): BinaryValue

    abstract override fun withoutAnnotations(): BinaryValue
}

public abstract class ByteValue : ScalarValue<Byte> {

    override val type: PartiQLValueType = PartiQLValueType.BYTE

    abstract override fun copy(annotations: Annotations): ByteValue

    abstract override fun withAnnotations(annotations: Annotations): ByteValue

    abstract override fun withoutAnnotations(): ByteValue
}

public abstract class BlobValue : ScalarValue<ByteArray> {

    override val type: PartiQLValueType = PartiQLValueType.BLOB

    abstract override fun copy(annotations: Annotations): BlobValue

    abstract override fun withAnnotations(annotations: Annotations): BlobValue

    abstract override fun withoutAnnotations(): BlobValue
}

public abstract class DateValue : ScalarValue<Date> {

    override val type: PartiQLValueType = PartiQLValueType.DATE

    abstract override fun copy(annotations: Annotations): DateValue

    abstract override fun withAnnotations(annotations: Annotations): DateValue

    abstract override fun withoutAnnotations(): DateValue
}

public abstract class TimeValue : ScalarValue<Time> {

    override val type: PartiQLValueType = PartiQLValueType.TIME

    abstract override fun copy(annotations: Annotations): TimeValue

    abstract override fun withAnnotations(annotations: Annotations): TimeValue

    abstract override fun withoutAnnotations(): TimeValue
}

public abstract class TimestampValue : ScalarValue<Timestamp> {

    override val type: PartiQLValueType = PartiQLValueType.TIMESTAMP

    abstract override fun copy(annotations: Annotations): TimestampValue

    abstract override fun withAnnotations(annotations: Annotations): TimestampValue

    abstract override fun withoutAnnotations(): TimestampValue
}

public abstract class IntervalValue : ScalarValue<Long> {

    override val type: PartiQLValueType = PartiQLValueType.INTERVAL

    abstract override fun copy(annotations: Annotations): IntervalValue

    abstract override fun withAnnotations(annotations: Annotations): IntervalValue

    abstract override fun withoutAnnotations(): IntervalValue
}

public abstract class BagValue<T : PartiQLValue> : CollectionValue<T> {

    override val type: PartiQLValueType = PartiQLValueType.BAG

    abstract override fun copy(annotations: Annotations): BagValue<T>

    abstract override fun withAnnotations(annotations: Annotations): BagValue<T>

    abstract override fun withoutAnnotations(): BagValue<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BagValue<*>
        if (other.annotations != this.annotations) return false

        // one (or both) null.bag
        if (this.isNull || other.isNull) return this.isNull == other.isNull

        // both not null, compare values
        val lhs = this.toList()
        val rhs = other.toList()
        // this is incorrect as it assumes ordered-ness, but we don't have a sort or hash yet
        val result = lhs == rhs
        return result
    }

    override fun hashCode(): Int {
        // TODO
        return type.hashCode()
    }
}

public abstract class ListValue<T : PartiQLValue> : CollectionValue<T> {

    override val type: PartiQLValueType = PartiQLValueType.LIST

    abstract override fun copy(annotations: Annotations): ListValue<T>

    abstract override fun withAnnotations(annotations: Annotations): ListValue<T>

    abstract override fun withoutAnnotations(): ListValue<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ListValue<*>
        if (other.annotations != this.annotations) return false

        // one (or both) null.list
        if (this.isNull || other.isNull) return this.isNull == other.isNull

        // both not null, compare values
        val lhs = this.toList()
        val rhs = other.toList()
        return lhs == rhs
    }

    override fun hashCode(): Int {
        // TODO
        return type.hashCode()
    }
}

public abstract class SexpValue<T : PartiQLValue> : CollectionValue<T> {

    override val type: PartiQLValueType = PartiQLValueType.SEXP

    abstract override fun copy(annotations: Annotations): SexpValue<T>

    abstract override fun withAnnotations(annotations: Annotations): SexpValue<T>

    abstract override fun withoutAnnotations(): SexpValue<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SexpValue<*>) return false
        if (other.annotations != this.annotations) return false

        // one (or both) null.sexp
        if (this.isNull || other.isNull) return this.isNull == other.isNull

        // both not null, compare values
        val lhs = this.toList()
        val rhs = other.toList()
        return lhs == rhs
    }

    override fun hashCode(): Int {
        // TODO
        return type.hashCode()
    }
}

public abstract class StructValue<T : PartiQLValue> : PartiQLValue {

    override val type: PartiQLValueType = PartiQLValueType.STRUCT

    public abstract val fields: Iterable<String>

    public abstract val values: Iterable<T>

    public abstract val entries: Iterable<Pair<String, T>>

    public abstract operator fun get(key: String): T?

    public abstract fun getAll(key: String): Iterable<T>

    abstract override fun copy(annotations: Annotations): StructValue<T>

    abstract override fun withAnnotations(annotations: Annotations): StructValue<T>

    abstract override fun withoutAnnotations(): StructValue<T>

    /**
     * Checks equality of struct entries, ignoring ordering.
     *
     * @param other
     * @return
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StructValue<*>) return false
        if (other.annotations != this.annotations) return false

        // one (or both) null.struct
        if (this.isNull || other.isNull) return this.isNull == other.isNull

        // both not null, compare fields
        val lhs = this.entries.asIterable().groupBy({ it.first }, { it.second })
        val rhs = other.entries.asIterable().groupBy({ it.first }, { it.second })

        // check size
        if (lhs.size != rhs.size) return false
        if (lhs.keys != rhs.keys) return false

        // check values
        lhs.entries.forEach { (key, values) ->
            val lGroup: Map<PartiQLValue, Int> = values.groupingBy { it }.eachCount()
            val rGroup: Map<PartiQLValue, Int> = rhs[key]!!.groupingBy { it }.eachCount()
            val matches = lGroup == rGroup
            if (!matches) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        // TODO
        return entries.toList().hashCode()
    }

    override fun toString(): String {
        if (isNull) {
            return "null"
        }
        return super.toString()
    }
}

public abstract class NullValue : PartiQLValue {

    override val type: PartiQLValueType = PartiQLValueType.NULL

    override val isNull: Boolean = true

    public abstract fun withType(type: PartiQLValueType): PartiQLValue

    abstract override fun copy(annotations: Annotations): NullValue

    abstract override fun withAnnotations(annotations: Annotations): NullValue

    abstract override fun withoutAnnotations(): NullValue
}

public abstract class MissingValue : PartiQLValue {

    override val type: PartiQLValueType = PartiQLValueType.MISSING

    override val isNull: Boolean = false

    abstract override fun copy(annotations: Annotations): MissingValue

    abstract override fun withAnnotations(annotations: Annotations): MissingValue

    abstract override fun withoutAnnotations(): MissingValue
}

public fun PartiQLValue.toIon(): IonElement = accept(ToIon, Unit)

@Throws(PRuntimeException::class)
public inline fun <reified T : PartiQLValue> PartiQLValue.check(): T {
    if (this is T) return this else {
        throw unexpectedTypeException()
    }
}

fun unexpectedTypeException(): PRuntimeException {
    return PRuntimeException(
        PError(
            PError.TYPE_UNEXPECTED,
            Severity.ERROR(),
            PErrorKind.EXECUTION(),
            null,
            mapOf()
        )
    )
}
