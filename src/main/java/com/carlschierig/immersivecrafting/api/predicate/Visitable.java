package com.carlschierig.immersivecrafting.api.predicate;

/**
 * Represents a condition which can be visited by a {@link PredicateVisitor}.
 */
public interface Visitable {
    /**
     * Visit the implementing object with the given visitor.
     *
     * @param visitor The visitor which should visit the object.
     */
    void accept(PredicateVisitor visitor);
}
