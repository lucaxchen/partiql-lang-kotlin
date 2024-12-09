package org.partiql.ast.graph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.partiql.ast.AstNode;
import org.partiql.ast.AstVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO docs, equals, hashcode
 */
@Builder(builderClassName = "Builder")
@EqualsAndHashCode(callSuper = false)
public class GraphMatch extends AstNode {
    @NotNull
    public final List<GraphPattern> patterns;

    @Nullable
    public final GraphSelector selector;

    public GraphMatch(@NotNull List<GraphPattern> patterns, @Nullable GraphSelector selector) {
        this.patterns = patterns;
        this.selector = selector;
    }

    @Override
    @NotNull
    public Collection<AstNode> children() {
        List<AstNode> kids = new ArrayList<>(patterns);
        if (selector != null) {
            kids.add(selector);
        }
        return kids;
    }

    @Override
    public <R, C> R accept(@NotNull AstVisitor<R, C> visitor, C ctx) {
        return visitor.visitGraphMatch(this, ctx);
    }
}
