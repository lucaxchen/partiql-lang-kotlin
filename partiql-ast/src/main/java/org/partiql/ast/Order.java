package org.partiql.ast;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Represents the ordering used within certain clauses.
 *
 * @see OrderBy
 * @see Sort
 */
@EqualsAndHashCode(callSuper = false)
public final class Order extends AstEnum {
    /**
     * Ascending order.
     */
    public static final int ASC = 0;
    /**
     * Descending order.
     */
    public static final int DESC = 1;

    public static Order ASC() {
        return new Order(ASC);
    }

    public static Order DESC() {
        return new Order(DESC);
    }

    private final int code;

    private Order(int code) {
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }

    @NotNull
    @Override
    public String name() {
        switch (code) {
            case ASC: return "ASC";
            case DESC: return "DESC";
            default: throw new IllegalStateException("Invalid Order code: " + code);
        }
    }

    @NotNull
    private static final int[] codes = {
        ASC,
        DESC
    };

    @NotNull
    public static Order parse(@NotNull String value) {
        switch (value) {
            case "ASC": return ASC();
            case "DESC": return DESC();
            default: throw new IllegalArgumentException("No enum constant Order." + value);
        }
    }

    @NotNull
    public static int[] codes() {
        return codes;
    }

    @NotNull
    @Override
    public List<AstNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public <R, C> R accept(@NotNull AstVisitor<R, C> visitor, C ctx) {
        return null;
    }
}
