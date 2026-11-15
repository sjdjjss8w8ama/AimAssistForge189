package com.aimassist;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.io.IOException;

public class ClickGUI extends AimAssistGUI {
    private Category category=Category.AIM_ASSIST;
    private int left,top;
    private int sliderDrag=-1;
    private boolean savedPulse=false;

    @Override public void initGui(){ left=width/2-205; top=height/2-145; }
    @Override public void drawScreen(int mx,int my,float pt) {
        drawDefaultBackground();
        RenderUtils.roundedPanel(left,top,410,290,0xFF111318);
        RenderUtils.roundedPanel(left+8,top+8,112,274,0xFF191D26);
        fontRendererObj.drawString("AIM",left+20,top+20,0xFF8B5CF6);
        fontRendererObj.drawString("ASSIST",left+20,top+32,0xFFFFFFFF);
        String[] names={"Aim Assist","Visuals","General"};
        Category[] cats=Category.values();
        for(int i=0;i<3;i++){
            int y=top+62+i*34; boolean active=category==cats[i];
            Gui.drawRect(left+14,y,left+114,y+27,active?0xFF30264A:(inside(mx,my,left+14,y,100,27)?0xFF282E3A:0xFF191D26));
            fontRendererObj.drawString(names[i],left+23,y+9,active?0xFFBDA5FF:0xFFA0A5B1);
        }
        fontRendererObj.drawString(category==Category.AIM_ASSIST?"Aim Assist":category==Category.VISUALS?"Visuals":"General",left+137,top+20,0xFFFFFFFF);
        drawContent(mx,my);
        fontRendererObj.drawString("RIGHT SHIFT  •  ESC TO CLOSE",left+137,top+270,0xFF777E8D);
        super.drawScreen(mx,my,pt);
    }
    private void drawContent(int mx,int my) {
        AimAssistConfig c=AimAssistMod.CONFIG;
        int x=left+137,w=250,y=top+48;
        if(category==Category.AIM_ASSIST){
            toggle(x,y,w,"Enable Aim Assist",c.enabled,mx,my,0);
            toggle(x,y+27,w,"Require Sword",c.requireSword,mx,my,1);
            Slider.draw(this,x,y+58,w,"Horizontal Speed (X)",c.speedX,0,20,"");
            Slider.draw(this,x,y+91,w,"Vertical Speed (Y)",c.speedY,0,20,"");
            Slider.draw(this,x,y+124,w,"FOV",c.fov,1,180,"°");
            Slider.draw(this,x,y+157,w,"Maximum Distance",c.maxDistance,1,32," m");
            Slider.draw(this,x,y+190,w,"Smoothness",c.smoothness,1,20,"");
            fontRendererObj.drawString("Target Mode: MOBS (single-player only)",x,y+225,0xFFA0A5B1);
        } else if(category==Category.VISUALS) {
            toggle(x,y,w,"Draw FOV",c.drawFov,mx,my,2);
            toggle(x,y+27,w,"Show FOV Circle",c.visualsFov,mx,my,3);
            toggle(x,y+54,w,"Menu Animation",c.menuAnimation,mx,my,4);
            Slider.draw(this,x,y+91,w,"Transparency",c.alpha,0.05f,1,"");
            Slider.draw(this,x,y+124,w,"Border Width",c.borderWidth,0.5f,5," px");
            fontRendererObj.drawString("Circle color: Purple (#8B5CF6)",x,y+160,0xFFA0A5B1);
        } else {
            toggle(x,y,w,"Master Enabled",c.enabled,mx,my,0);
            toggle(x,y+27,w,"Auto Save",c.autoSave,mx,my,5);
            Gui.drawRect(x,y+65,x+118,y+91,0xFF30264A);
            fontRendererObj.drawString("Save Settings",x+13,y+74,0xFFFFFFFF);
            Gui.drawRect(x+128,y+65,x+250,y+91,0xFF30264A);
            fontRendererObj.drawString("Reset Settings",x+137,y+74,0xFFFFFFFF);
            fontRendererObj.drawString("Menu key: RIGHT SHIFT",x,y+111,0xFFA0A5B1);
        }
    }
    private void toggle(int x,int y,int w,String s,boolean value,int mx,int my,int id){
        ToggleButton.draw(x,y,w,s,value,inside(mx,my,x,y,w,22));
    }
    private boolean inside(int mx,int my,int x,int y,int w,int h){return mx>=x&&mx<=x+w&&my>=y&&my<=y+h;}
    @Override protected void mouseClicked(int mx,int my,int button) throws IOException {
        if(button!=0)return;
        for(int i=0;i<3;i++) if(inside(mx,my,left+14,top+62+i*34,100,27)){category=Category.values()[i];return;}
        int x=left+137,y=top+48,w=250; AimAssistConfig c=AimAssistMod.CONFIG;
        if(category==Category.AIM_ASSIST){
            if(inside(mx,my,x,y,w,22)) c.enabled=!c.enabled;
            else if(inside(mx,my,x,y+27,w,22)) c.requireSword=!c.requireSword;
            else sliderDrag=sliderAt(mx,my,x,y+58,w,0,20,0);
            if(sliderDrag==-1) {
                if(inside(mx,my,x,y+91,w,20)) sliderDrag=1;
                else if(inside(mx,my,x,y+124,w,20)) sliderDrag=2;
                else if(inside(mx,my,x,y+157,w,20)) sliderDrag=3;
                else if(inside(mx,my,x,y+190,w,20)) sliderDrag=4;
            }
        } else if(category==Category.VISUALS){
            if(inside(mx,my,x,y,w,22)) c.drawFov=!c.drawFov;
            else if(inside(mx,my,x,y+27,w,22)) c.visualsFov=!c.visualsFov;
            else if(inside(mx,my,x,y+54,w,22)) c.menuAnimation=!c.menuAnimation;
            else if(inside(mx,my,x,y+91,w,20)) sliderDrag=5;
            else if(inside(mx,my,x,y+124,w,20)) sliderDrag=6;
        } else {
            if(inside(mx,my,x,y,w,22)) c.enabled=!c.enabled;
            else if(inside(mx,my,x,y+27,w,22)) c.autoSave=!c.autoSave;
            else if(inside(mx,my,x,y+65,118,26)) c.save();
            else if(inside(mx,my,x+128,y+65,122,26)) c.reset();
        }
        c.dirty=true; if(c.autoSave)c.save();
    }
    private int sliderAt(int mx,int my,int x,int y,int w,float min,float max,int id){
        if(inside(mx,my,x,y-2,w,20)) { setSlider(id,mx,x,w); return id; } return -1;
    }
    private void setSlider(int id,int mx,int x,int w){
        float p=Math.max(0,Math.min(1,(mx-x)/(float)w)); AimAssistConfig c=AimAssistMod.CONFIG;
        switch(id){
            case 0:c.speedX=p*20;break; case 1:c.speedY=p*20;break; case 2:c.fov=1+p*179;break;
            case 3:c.maxDistance=1+p*31;break; case 4:c.smoothness=1+p*19;break;
            case 5:c.alpha=0.05f+p*0.95f;break; case 6:c.borderWidth=0.5f+p*4.5f;break;
        }
        c.dirty=true; if(c.autoSave)c.save();
    }
    @Override protected void mouseClickMove(int mx,int my,int button,long elapsed){
        if(sliderDrag>=0){setSlider(sliderDrag,mx,left+137,250);}
    }
    @Override protected void mouseReleased(int mx,int my,int state){sliderDrag=-1;}
    @Override protected void keyTyped(char c,int key) throws IOException {
        if(key==Keyboard.KEY_ESCAPE || key==Keyboard.KEY_RSHIFT){mc.displayGuiScreen(null);return;}
        super.keyTyped(c,key);
    }
    @Override public boolean doesGuiPauseGame(){return false;}
}
