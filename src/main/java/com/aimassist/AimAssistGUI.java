package com.aimassist;
import net.minecraft.client.gui.GuiScreen;
public class AimAssistGUI extends GuiScreen {
    protected void drawBase(int mouseX,int mouseY,float partialTicks) {
        drawDefaultBackground();
        RenderUtils.roundedPanel(width/2-180,height/2-125,360,250,0xFF191D26);
        fontRendererObj.drawString("AIM ASSIST",width/2-165,height/2-110,0xFF8B5CF6);
        fontRendererObj.drawString("Single-player test entity control",width/2-165,height/2-94,0xFFA0A5B1);
    }
    @Override public boolean doesGuiPauseGame(){ return false; }
}
