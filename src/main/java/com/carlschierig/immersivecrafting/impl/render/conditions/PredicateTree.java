package com.carlschierig.immersivecrafting.impl.render.conditions;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.condition.CompoundICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.SingleChildICCondition;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import com.carlschierig.immersivecrafting.mixin.GuiGraphicsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A tree which is constructed from a predicate and rendered using
 * <p>
 * C. Buchheim, M. Jünger, and S. Leipert.
 * Improving Walker’s algorithm to run in linear time.
 * In Michael T. Goodrich and Stephen G. Kobourov, editors,
 * <i>Graph Drawing (Proceedings of 10th International Symposium
 * on Graph Drawing, 2002)</i>, volume 2528 of <i>Lecture Notes in Computer
 * Science</i>, pages 344–353.
 * Springer, 2002.
 */
public class PredicateTree implements ICRenderable {
    private final ICPredicate predicate;
    private Node root;
    private final int baseUnit = 16;
    private final int distance = 3 * baseUnit;
    private final int ySize = baseUnit * 2;

    private int leftmostX;
    private int xShift;

    public PredicateTree(ICPredicate predicate) {
        this.predicate = predicate;
        // TODO: verify algorithm and remove exception catching
        try {
            buildTree();
            firstWalk(root);
            secondWalk(root, -root.prelim);
            xShift = leftmostX / 2;
        } catch (NullPointerException e) {
            ImmersiveCrafting.LOG.error(e.getMessage());
            for (var element : e.getStackTrace()) {
                ImmersiveCrafting.LOG.error(element.toString());
            }
        }
    }

    private void buildTree() {
        new Visitor().visit(predicate);
    }

    private void secondWalk(Node v, int m) {
        v.x = v.prelim + m;
        leftmostX = Math.min(v.x, leftmostX);

        v.y = v.level * ySize;
        for (var child : v.children) {
            secondWalk(child, m + v.mod);
        }
    }

    private void firstWalk(Node v) {
        if (v.isLeaf()) {
            v.prelim = 0;
            if (v.hasLeftSibling()) {
                v.prelim = v.leftSibling.prelim + distance;
            }
        } else {
            var defaultAncestor = v.getLeftmostChild();
            for (var child : v.children) {
                firstWalk(child);
                defaultAncestor = apportion(child, defaultAncestor);
            }
            executeShifts(v);
            var midpoint = 0.5 * (v.getLeftmostChild().prelim + v.getRightmostChild().prelim);
            if (v.hasLeftSibling()) {
                v.prelim = v.leftSibling.prelim + distance;
                v.mod = v.prelim - (int) midpoint;
            } else {
                v.prelim = (int) midpoint;
            }
        }
    }

    private Node apportion(Node v, Node defaultAncestor) {
        Node vi_plus = null;
        Node vo_plus = null;
        Node vi_minus = null;
        Node vo_minus = null;

        var si_plus = 0;
        var so_plus = 0;
        var si_minus = 0;
        var so_minus = 0;

        if (v.leftSibling != null) {
            var w = v.leftSibling;
            vi_plus = v;
            vo_plus = v;
            vi_minus = w;
            vo_minus = getLeftmostSibling(vi_plus);

            si_plus = vi_plus.mod;
            so_plus = vo_plus.mod;
            si_minus = vi_minus.mod;
            so_minus = vo_minus.mod;

            while (nextRight(vi_minus) != null && nextLeft(vi_plus) != null) {
                vi_minus = nextRight(vi_minus);
                vi_plus = nextLeft(vi_plus);
                vo_minus = nextLeft(vo_minus);
                vo_plus = nextRight(vo_plus);

                vo_plus.ancestor = v;
                var shift = (vi_minus.prelim + si_minus) - (vi_plus.prelim + si_plus) + distance;
                if (shift > 0) {
                    moveSubtree(ancestor(vi_minus, v, defaultAncestor), v, shift);
                    si_plus = si_plus + shift;
                    so_plus = so_plus + shift;
                }
                si_minus = si_minus + vi_minus.mod;
                si_plus = si_plus + vi_plus.mod;
                so_minus = so_minus + vo_minus.mod;
                so_plus = so_plus + vo_plus.mod;
            }
        }
        if (nextRight(vi_minus) != null && nextRight(vo_plus) == null) {
            vo_plus.thread = nextRight(vi_minus);
            vo_plus.mod = vo_plus.mod + si_minus - so_plus;
        }
        if (nextLeft(vi_plus) != null && nextLeft(vo_minus) == null) {
            vo_minus.thread = nextLeft(vi_plus);
            vo_minus.mod = vo_minus.mod + si_plus - so_minus;
            return v;
        }
        return defaultAncestor;
    }

    private void executeShifts(Node v) {
        var shift = 0;
        var change = 0;
        for (int i = v.children.size() - 1; i >= 0; i--) {
            var w = v.children.get(i);
            w.prelim += shift;
            w.mod += shift;
            change += w.change;
            shift += w.shift + change;
        }
    }

    private Node getLeftmostSibling(Node node) {
        var current = node;
        while (current.hasLeftSibling()) {
            current = current.leftSibling;
        }
        return current;
    }

    private Node nextRight(Node node) {
        if (node == null) {
            return null;
        }
        if (node.hasChild()) {
            return node.getRightmostChild();
        } else {
            return node.thread;
        }
    }

    private Node nextLeft(Node node) {
        if (node == null) {
            return null;
        }
        if (node.hasChild()) {
            return node.getLeftmostChild();
        } else {
            return node.thread;
        }
    }

    private void moveSubtree(Node w_minus, Node w_plus, int shift) {
        var subtrees = w_minus.number - w_plus.number;
        w_plus.change -= shift / subtrees;
        w_plus.shift += shift;
        w_minus.change += shift / subtrees;
        w_plus.prelim += shift;
        w_plus.mod += shift;
    }

    private Node ancestor(Node vi_minus, Node v, Node defaultAncestor) {
        if (vi_minus.ancestor.isSibling(v)) {
            return vi_minus.ancestor;
        } else {
            return defaultAncestor;
        }
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.pose().pushPose();
        draw.pose().translate(xShift, 0, 0);
        renderSubtree(root, draw, x, y, delta);
        draw.pose().popPose();
    }

    private void renderSubtree(Node node, GuiGraphics draw, int x, int y, float delta) {
        draw.pose().pushPose();
        draw.pose().translate(node.x, node.y, 0);
        // Draw tooltip
        renderTooltip(node, draw, x, y, delta);
        // Draw condition icon
        node.condition.render(draw, x, y, delta);
        draw.pose().popPose();

        // Draw lines
        if (node.hasChild()) {
            var leftX = node.getLeftmostChild().x + baseUnit / 2;
            var rightX = node.getRightmostChild().x + baseUnit / 2;

            var yTop = node.y + baseUnit + 1;
            var yMid = node.y + baseUnit + (ySize - baseUnit) / 2;
            var yBot = node.y + ySize - 1;

            // Draw horizontal line
            draw.fill(leftX, yMid, rightX, yMid + 1, 0xffffffff);

            // Draw vertical line to mid
            draw.fill(node.x + baseUnit / 2, yTop, node.x + +baseUnit / 2 + 1, yMid, 0xffffffff);

            // draw lines to children
            for (int i = 0; i < node.children.size(); i++) {
                var xPos = leftX + i * distance;
                draw.fill(xPos, yMid, xPos + 1, yBot, 0xffffffff);
            }
        }

        for (var child : node.children) {
            renderSubtree(child, draw, x, y, delta);
        }
    }

    private void renderTooltip(Node node, GuiGraphics draw, int x, int y, float delta) {
        var nodeX = node.x + xShift;
        if (x >= nodeX && x <= nodeX + baseUnit && y >= node.y && y <= node.y + baseUnit) {
            var tooltip = node.condition.getTooltip();
            ((GuiGraphicsAccessor) draw).invokeRenderTooltip(Minecraft.getInstance().font, tooltip, x, y, DefaultTooltipPositioner.INSTANCE);
        }
    }

    private static class Node {
        private final ICCondition condition;

        private final Node parent;
        private final List<Node> children = new ArrayList<>();
        private Node leftSibling;
        private Node ancestor = this;
        private Node thread = null;

        private int mod = 0;
        private final int number;
        private int change;
        private int shift;

        int level;
        int prelim;

        int x;
        int y;

        public Node(ICCondition condition, Node parent, int level, int number) {
            this.condition = condition;
            this.level = level;
            this.number = number;
            this.parent = parent;
        }

        private boolean isLeaf() {
            return children.isEmpty();
        }

        private boolean hasLeftSibling() {
            return leftSibling != null;
        }

        private boolean hasChild() {
            return children.size() > 1;
        }

        private Node getLeftmostChild() {
            return children.get(0);
        }

        private Node getRightmostChild() {
            return children.get(children.size() - 1);
        }

        private boolean isSibling(Node other) {
            return other.parent == parent;
        }
    }

    private class Visitor implements PredicateVisitor {
        private final Stack<Node> nodeStack = new Stack<>();
        private int number = 0;

        public void visitCompound(CompoundICCondition condition) {
            var current = nodeStack.peek();
            Node left = null;
            for (var child : condition.getChildren()) {
                var childNode = new Node(child, current, nodeStack.size(), number++);
                if (left != null) {
                    childNode.leftSibling = left;
                }
                left = childNode;

                current.children.add(childNode);
                nodeStack.push(childNode);
                child.accept(this);
                nodeStack.pop();
            }
        }

        @Override
        public void visitCondition(ICCondition condition) {
        }

        @Override
        public void visitSingleChildCondition(SingleChildICCondition condition) {
            var child = condition.getChild();
            if (child instanceof CompoundICCondition compound) {
                visitCompound(compound);
            }
        }

        @Override
        public void visit(ICPredicate predicate) {
            root = new Node(predicate, null, nodeStack.size(), number++);
            nodeStack.push(root);
            PredicateVisitor.super.visit(predicate);
            nodeStack.pop();
        }
    }
}
