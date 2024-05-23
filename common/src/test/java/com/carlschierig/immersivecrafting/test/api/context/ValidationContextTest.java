package com.carlschierig.immersivecrafting.test.api.context;

import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ValidationContextTest {

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void testEmptySuperset() {
        Assertions.assertTrue(ValidationContext.EMPTY.supersetOf(ValidationContext.EMPTY));
    }
}
