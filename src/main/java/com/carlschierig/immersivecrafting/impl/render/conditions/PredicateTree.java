package com.carlschierig.immersivecrafting.impl.render.conditions;

import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.condition.CompoundICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PredicateTree implements ICRenderable {
    private final ICPredicate predicate;
    private final List<List<Node>> tree;

    public PredicateTree(ICPredicate predicate) {
        this.predicate = predicate;
        // set up tree root
        tree = new ArrayList<>();
        buildTree();
    }

    private void buildTree() {
        new Visitor().visit(predicate);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        var yCoord = 0;
        var pose = draw.pose();
        for (var layer : tree) {
            var xCoord = 0;
            for (var node : layer) {
                var condition = node.condition;
                pose.pushPose();
                pose.translate(xCoord, yCoord, 0);
                condition.render(draw, x, y, delta);
                xCoord += 20;
                pose.popPose();
            }
            yCoord += 20;
        }
    }

    private static class Node {
        private final ICCondition parent;
        private final ICCondition condition;

        public Node(ICCondition condition) {
            this(condition, condition);
        }

        public Node(ICCondition condition, ICCondition parent) {
            this.parent = parent;
            this.condition = condition;
        }
    }

    private class Visitor implements PredicateVisitor {
        private Stack<Node> nodes;
        private int layer;

        private void incrementLayer() {
            layer++;
            if (layer >= tree.size()) {
                tree.add(new ArrayList<>());
            }
        }

        private void decrementLayer() {
            layer--;
        }

        public void visitCompound(CompoundICCondition condition) {
            tree.get(layer).add(new Node(condition));

            incrementLayer();
            for (var child : condition.getChildren()) {
                child.accept(this);
            }
            decrementLayer();
        }

        @Override
        public void visitCondition(ICCondition condition) {
            tree.get(layer).add(new Node(condition));
        }

        @Override
        public void visit(ICPredicate predicate) {
            tree.add(new ArrayList<>());
            layer = 0;

            PredicateVisitor.super.visit(predicate);
        }
    }
}
