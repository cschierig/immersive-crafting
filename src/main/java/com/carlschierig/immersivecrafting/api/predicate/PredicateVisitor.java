package com.carlschierig.immersivecrafting.api.predicate;

import com.carlschierig.immersivecrafting.api.predicate.condition.CompoundICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.SingleChildICCondition;

/**
 * A visitor for visiting predicate trees.
 */
public interface PredicateVisitor {
    default void visit(ICPredicate predicate) {
        visitCompound(predicate);
    }

    void visitCompound(CompoundICCondition condition);

    void visitCondition(ICCondition condition);

    void visitSingleChildCondition(SingleChildICCondition condition);
}
