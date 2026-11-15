package com.aimassist;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void panel(int x,int y,int w,int h,int color) {
        Gui.drawRect(x+2,y+2,x+w+2,y+h+2,0x55000000);
        Gui.drawRect(x,y,x+w,y+h,color);
    }
    public static void roundedPanel(int x,int y,int w,int h,int color) {
        panel(x,y,w,h,color);
        Gui.drawRect(x+4,y,x+w-4,y+h,color);
        Gui.drawRect(x,y+4,x+w,y+h-4,color);
    }
    public static void circle(int cx,int cy,float radius,int color,float width) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend(); GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770,771,1,0);
        GL11.glLineWidth(width); GL11.glColor4f(((color>>16)&255)/255f,((color>>8)&255)/255f,(color&255)/255f,((color>>>24)&255)/255f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for(int i=0;i<90;i++){ double a=i*Math.PI*2/90; GL11.glVertex2d(cx+Math.cos(a)*radius,cy+Math.sin(a)*radius); }
        GL11.glEnd();
        GlStateManager.enableTexture2D(); GlStateManager.disableBlend(); GlStateManager.popMatrix();
    }
}
