package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.impl.render.conditions.PredicateTree;
import com.carlschierig.immersivecrafting.impl.render.conditions.TreeScreen;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ICEmiRecipe implements EmiRecipe {
    private final EmiRecipeCategory category;
    private final ICRecipe recipe;

    private static final int PADDING = 2;
    private int width;
    private int inOutWidth;
    private int height;
    private int inOutHeight;
    private final int ingredientWidth = EmiTexture.SLOT.width * 3;
    private final int outputWidth = EmiTexture.SLOT.width * 3;
    private int conditionHeight;

    public ICEmiRecipe(EmiRecipeCategory category, ICRecipe recipe) {
        this.category = category;
        this.recipe = recipe;
        computeDimensions();
    }


    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return EmiConversionUtil.convertIngredients(recipe.getIngredients());
    }

    @Override
    public List<EmiStack> getOutputs() {
        return EmiConversionUtil.convertStacks(recipe.getResults());
    }

    @Override
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        renderIngredients(widgets);
        renderArrow(widgets);
        renderOutputs(widgets);
        renderConditions(widgets);
    }

    private void renderIngredients(WidgetHolder widgets) {
        var ingredients = recipe.getIngredients();
        // start is moved to the right if we have fewer than 3 ingredients
        int left = ingredients.size() < 3
                ? ((3 - ingredients.size()) * EmiTexture.SLOT.width / 2)
                : 0;

        for (int i = 0; i < ingredients.size(); i++) {
            var ingredient = ingredients.get(i);
            var xCord = left + (i % 3) * EmiTexture.SLOT.width;
            var yCord = (i / 3) * EmiTexture.SLOT.width;
            var slot = new SlotWidget(new ICEmiIngredient(ingredient), xCord, yCord);
            widgets.add(slot);
        }
    }

    private void renderOutputs(WidgetHolder widgets) {
        var outputs = recipe.getResults();
        // start is moved to the right if we have fewer than three outputs
        int left = inOutWidth - outputWidth;
        left += outputs.size() < 3
                ? ((3 - outputs.size()) * EmiTexture.SLOT.width / 2)
                : 0;

        for (int i = 0; i < outputs.size(); i++) {
            var result = outputs.get(i);
            var xCord = left + (i % 3) * EmiTexture.SLOT.width;
            var yCord = (i / 3) * EmiTexture.SLOT.width;
            var slot = new SlotWidget(new ICEmiStack(result), xCord, yCord);
            widgets.add(slot);
        }
    }

    private void renderArrow(WidgetHolder widgets) {
        int xCord = ingredientWidth + PADDING;
        int yCord = (inOutHeight - EmiTexture.EMPTY_ARROW.height) / 2;
        widgets.addTexture(EmiTexture.EMPTY_ARROW, xCord, yCord);
    }

    private void renderConditions(WidgetHolder widgets) {
        var client = Minecraft.getInstance();

        var y = inOutHeight + PADDING;
        var buttonWidth = client.font.width(Component.translatable("immersive_crafting.recipeView.conditions")) + PADDING * 4;
        var buttonHeight = client.font.lineHeight + PADDING * 4;

        var buttonX = (width - buttonWidth) / 2;

        var onPress = new Button.OnPress() {
            @Override
            public void onPress(Button button) {
                var screen = client.screen;
                client.setScreen(new TreeScreen(new PredicateTree(recipe.getPredicate()), screen));
            }
        };
        var button = Button.builder(Component.translatable("immersive_crafting.recipeView.conditions"), onPress)
                .bounds(buttonX, y, buttonWidth, buttonHeight)
                .build();
        var widget = new ButtonWidget(button);
        widgets.add(widget);
    }

    private void computeDimensions() {
        inOutWidth += ingredientWidth; // ingredients
        inOutWidth += PADDING + EmiTexture.EMPTY_ARROW.width + PADDING; // arrow
        inOutWidth += outputWidth; // outputs

        width = inOutWidth;

        // height
        // Ingredients
        var inputHeight = (recipe.getIngredients().size() / 3) * EmiTexture.SLOT.height;
        if (inputHeight == 0 && !getInputs().isEmpty()) {
            inputHeight = EmiTexture.SLOT.height;
        }
        // Outputs
        var outputHeight = (recipe.getResults().size() / 3) * EmiTexture.LARGE_SLOT.height;
        if (outputHeight == 0 && !getOutputs().isEmpty()) {
            outputHeight = EmiTexture.SLOT.height;
        }
        inOutHeight = Math.max(Math.max(inputHeight, outputHeight), EmiTexture.EMPTY_ARROW.height);

        // Conditions
        conditionHeight += PADDING; // upper padding
        conditionHeight += Minecraft.getInstance().font.lineHeight + PADDING * 4; // button height
        conditionHeight += PADDING; // bottom padding

        // total height
        height = inOutHeight + conditionHeight;
    }
}
