package com.aimassist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
public class ToggleButton {
    public static void draw(int x,int y,int w,String label,boolean value,boolean hover) {
        int bg=hover?0xFF282E3A:0xFF202530;
        Gui.drawRect(x,y,x+w,y+22,bg);
        Minecraft.getMinecraft().fontRendererObj.drawString(label,x+7,y+7,0xFFFFFFFF);
        int color=value?0xFF8B5CF6:0xFF555B68;
        Gui.drawRect(x+w-34,y+5,x+w-8,y+17,color);
        Gui.drawRect(value?x+w-20:x+w-32,y+7,value?x+w-10:x+w-22,y+15,0xFFFFFFFF);
    }
}
