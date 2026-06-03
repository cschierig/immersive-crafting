package com.carlschierig.immersivecrafting.compat.client.jei;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.impl.client.render.conditions.PredicateTree;
import com.carlschierig.immersivecrafting.impl.client.render.conditions.TreeScreen;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ICJeiRecipeCategory<T extends ICRecipe> implements IRecipeCategory<T> {
    private final ICJeiRecipeType<T> type;
    private final IJeiHelpers helpers;

    private static final int PADDING = 2;
    private static final int rowWidth = 3;
    private final int inputSlotDims;
    private int width;
    private int inOutWidth;
    private int height;
    private int inOutHeight;
    private final int ingredientWidth;
    private final int outputWidth;
    private int conditionHeight;

    public ICJeiRecipeCategory(ICJeiRecipeType<T> type, IJeiHelpers helpers) {
        this.type = type;
        this.helpers = helpers;
        inputSlotDims = helpers.getGuiHelper().getSlotDrawable().getWidth();

        ingredientWidth = inputSlotDims * rowWidth;
        outputWidth = inputSlotDims * rowWidth;

        computeDimensions();
    }

    @Override
    public ICJeiRecipeType<T> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(ICTranslationHelper.translateRecipeType(type.getType()));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        setIngredients(builder, recipe);
        setOutputs(builder, recipe);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, T recipe, IFocusGroup focuses) {
        renderArrow(builder, recipe);
        renderConditions(builder, recipe);
    }

    private void setIngredients(IRecipeLayoutBuilder builder, T recipe) {
        var guiHelpers = helpers.getGuiHelper();

        var ingredients = recipe.getIngredients();
        // start is moved to the right if we have fewer than 3 ingredients
        int left = ingredients.size() < 3
                ? ((3 - ingredients.size()) * inputSlotDims / 2)
                : 0;

        for (int i = 0; i < ingredients.size(); i++) {
            var ingredient = ingredients.get(i);
            var xCord = left + (i % 3) * inputSlotDims;
            var yCord = (i / 3) * inputSlotDims;
            var slot = builder.addInputSlot(xCord, yCord);
            slot.add(ingredient.getDisplay());
            slot.setStandardSlotBackground();
        }
    }

    private void setOutputs(IRecipeLayoutBuilder builder, T recipe) {
        var guiHelpers = helpers.getGuiHelper();

        var outputs = recipe.getResults();
        // start is moved to the right if we have fewer than three outputs
        int left = inOutWidth - outputWidth;
        left += outputs.size() < 3
                ? ((3 - outputs.size()) * inputSlotDims / 2)
                : 0;

        for (int i = 0; i < outputs.size(); i++) {
            var result = outputs.get(i);
            var xCord = left + (i % 3) * inputSlotDims;
            var yCord = (i / 3) * inputSlotDims;
            var slot = builder.addOutputSlot(xCord, yCord);
            slot.add(result.getDisplay());
            slot.setStandardSlotBackground();
        }
    }

    private void renderArrow(IRecipeExtrasBuilder builder, T recipe) {
        var arrow = helpers.getGuiHelper().getRecipeArrow();
        int xCord = ingredientWidth + PADDING;

        int actualinOutHeight = (Math.max(recipe.getIngredients().size(), recipe.getResults().size()) / rowWidth + 1) * inputSlotDims;
        int yCord = (actualinOutHeight - arrow.getHeight()) / 2;
        builder.addDrawable(arrow, xCord, yCord);
    }

    private void renderConditions(IRecipeExtrasBuilder builder, T recipe) {
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

        builder.addInputHandler(widget);
        builder.addWidget(widget);
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
    }

    /**
     * Computes the dimensions of the recipe category's recipes.
     * Unfortunately, we cannot use dynamic dimensions, so we have to make do with the assumption
     * that no recipe uses more than 9 ingredients.
     */
    private void computeDimensions() {
        var guiHelpers = helpers.getGuiHelper();
        inOutWidth += ingredientWidth; // ingredients
        inOutWidth += PADDING + guiHelpers.getRecipeArrow().getWidth() + PADDING; // arrow
        inOutWidth += outputWidth; // outputs

        width = inOutWidth;

        final var maxIngredientCount = 9;
        final var maxOutputCount = 3;

        // height
        // Ingredients
        var inputHeight = (maxIngredientCount / rowWidth) * inputSlotDims;
        // Outputs
        var outputHeight = (maxOutputCount / rowWidth) * inputSlotDims;

        inOutHeight = Math.max(Math.max(inputHeight, outputHeight), guiHelpers.getRecipeArrow().getHeight());

        // Conditions
        conditionHeight += PADDING; // upper padding
        conditionHeight += Minecraft.getInstance().font.lineHeight + PADDING * 4; // button height
        conditionHeight += PADDING; // bottom padding

        // total height
        height = inOutHeight + conditionHeight;
    }
}
