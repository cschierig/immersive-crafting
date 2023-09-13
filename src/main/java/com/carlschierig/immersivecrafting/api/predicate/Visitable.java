package com.carlschierig.immersivecrafting.api.predicate;

public interface Visitable {
    void accept(PredicateVisitor visitor);
}
