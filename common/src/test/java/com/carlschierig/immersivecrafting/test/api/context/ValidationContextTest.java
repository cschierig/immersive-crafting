package com.carlschierig.immersivecrafting.test.api.context;

import com.carlschierig.immersivecrafting.ImmersiveCraftingCommon;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import net.minecraft.client.main.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidationContextTest {

    @BeforeEach
    void setUp() {
        Main.main(new String[0]);
        ImmersiveCraftingCommon.init();
    }

    @Test
    void testEmptySuperset() {
        Assertions.assertTrue(ValidationContext.EMPTY.supersetOf(ValidationContext.EMPTY));
    }
}
