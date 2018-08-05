package com.gmail.zendarva.aie.gui;

import com.gmail.zendarva.aie.api.IIngredient;
import com.gmail.zendarva.aie.gui.widget.Control;
import com.gmail.zendarva.aie.gui.widget.IFocusable;
import com.gmail.zendarva.aie.util.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.ItemRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by James on 7/28/2018.
 */
public class AEIRenderHelper {
    static Point mouseLoc;
    static GuiItemList aeiGui;
    static GuiContainer overlayedGUI;
    static List<TooltipData> tooltipsToRender = new ArrayList<>();

    public static void setMouseLoc(int x, int y) {
        mouseLoc = new Point(x, y);
    }

    static IFocusable focusedControl;

    public static Point getMouseLoc() {
        return mouseLoc;
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public static void drawAEI(GuiContainer overlayedGui) {
        overlayedGUI = overlayedGui;
        if (aeiGui == null) {
            aeiGui = new GuiItemList(overlayedGui);
        }
        aeiGui.draw();
        renderTooltips();
    }

    public static void resize() {
        if (aeiGui != null) {
            aeiGui.resize();
        }
    }

    public static ItemRenderer getItemRender() {
        return Minecraft.getMinecraft().getRenderItem();
    }

    public static FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    public static GuiContainer getOverlayedGui() {
        return overlayedGUI;
    }

    public static List<String> getTooltip(IIngredient ingredient) {
        return null;
    }

    public static void addToolTip(List<String> text, int x, int y) {
        tooltipsToRender.add(new TooltipData(text, x, y));
    }


    private static void renderTooltips() {
        for (TooltipData tooltipData : tooltipsToRender) {
            getOverlayedGui().drawHoveringText(tooltipData.text, tooltipData.x, tooltipData.y);
        }
        tooltipsToRender.clear();


    }

    public static boolean mouseClick(int x, int y, int button) {
        for (Control control : aeiGui.controls) {
            if (control.isHighlighted() && control.isEnabled() && control.onClick != null) {
                if (focusedControl != null)
                    focusedControl.setFocused(false);
                if (control instanceof IFocusable) {
                    focusedControl = (IFocusable) control;
                    ((IFocusable) control).setFocused(true);
                }
                return control.onClick.apply(button);
            }
        }
        if (focusedControl != null) {
            focusedControl.setFocused(false);
            focusedControl = null;
        }
        return false;
    }

    public static boolean keyDown(int typedChar, int keyCode, int unknown) {
        boolean handled = false;
        if (focusedControl != null && focusedControl instanceof Control) {
            Control control = (Control) focusedControl;
            if (control.onKeyDown != null) {
                handled = control.onKeyDown.accept(typedChar, keyCode, unknown);
            }
            if (control.charPressed != null)
                if (typedChar == 256) {
                    ((IFocusable) control).setFocused(false);
                    focusedControl = null;
                }
            handled = true;
        }
        if (handled == false)
            System.out.println("Fuck");
        return handled;
    }

    public static boolean charInput(long num, int keyCode, int unknown) {
        if (focusedControl != null && focusedControl instanceof Control) {
            Control control = (Control) focusedControl;
            if (control.charPressed != null) {
                if (num == 0)
                    control.charPressed.accept((char) keyCode, unknown);
                else {
                    char[] chars = Character.toChars(keyCode);
                    for (int x = 0; x < chars.length; x++) {
                        control.charPressed.accept(chars[x], unknown);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static class TooltipData {

        private final List<String> text;
        private final int x;
        private final int y;

        public TooltipData(List<String> text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }

    public static void updateSearch(){
        aeiGui.updateView();
    }
}
