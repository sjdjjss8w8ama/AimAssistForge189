package com.aimassist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;

public class Slider {
    public static void draw(GuiScreen s, int x, int y, int w, String label, float value, float min, float max, String suffix) {
        FontRenderer f=Minecraft.getMinecraft().fontRendererObj;
        f.drawString(label, x, y, 0xFFFFFFFF);
        String v=String.format(java.util.Locale.US,"%.1f%s",value,suffix);
        f.drawString(v,x+w-f.getStringWidth(v),y,0xFFB8AFCF);
        int by=y+13;
        Gui.drawRect(x,by,x+w,by+4,0xFF353A48);
        int fill=(int)(w*Math.max(0,Math.min(1,(value-min)/(max-min))));
        Gui.drawRect(x,by,x+fill,by+4,0xFF8B5CF6);
        Gui.drawRect(x+fill-2,by-2,x+fill+2,by+6,0xFFFFFFFF);
    }
    public static float update(int mouseX,int mouseY,int x,int y,int w,float min,float max) {
        if (mouseY<y-3 || mouseY>y+9 || mouseX<x || mouseX>x+w) return Float.NaN;
        return min+(max-min)*Math.max(0,Math.min(1,(mouseX-x)/(float)w));
    }
}
