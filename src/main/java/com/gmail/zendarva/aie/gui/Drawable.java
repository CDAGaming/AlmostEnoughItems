package com.gmail.zendarva.aie.gui;

import com.gmail.zendarva.aie.api.IDrawable;
import com.gmail.zendarva.aie.gui.widget.Control;

import java.awt.*;

/**
 * Created by James on 7/28/2018.
 */
public abstract class Drawable implements IDrawable{
    protected Rectangle rect;
    public Drawable(int x, int y, int width, int height) {
        rect = new Rectangle(x,y,width,height);
    }
    public Drawable(Rectangle rect){
        this.rect = rect;
    }

    public abstract void draw();

    public boolean isHighlighted(){
        Point mousePoint = AEIRenderHelper.getMouseLoc();
        if (rect.contains(mousePoint.x,mousePoint.y)) {
            if (this instanceof Control)
                AEIRenderHelper.aeiGui.setLastHovered((Control) this);
            return true;
        }
        return false;
    }
}
