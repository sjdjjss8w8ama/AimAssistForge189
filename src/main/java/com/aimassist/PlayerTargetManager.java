package com.aimassist;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.List;

public class PlayerTargetManager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private EntityLivingBase target;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) return;
        AimAssistConfig c = AimAssistMod.CONFIG;
        if (!c.enabled) { target=null; return; }
        if (!mc.isSingleplayer()) { target=null; return; }
        if (c.requireSword && !(mc.thePlayer.getHeldItem()!=null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) { target=null; return; }
        target = findTarget(c);
        if (target == null) return;
        double dx=target.posX-mc.thePlayer.posX, dz=target.posZ-mc.thePlayer.posZ;
        double dy=(target.posY+target.getEyeHeight()*0.65)-(mc.thePlayer.posY+mc.thePlayer.getEyeHeight());
        double horizontal=Math.sqrt(dx*dx+dz*dz);
        float desiredYaw=(float)(Math.atan2(dz,dx)*180.0/Math.PI)-90.0f;
        float desiredPitch=(float)(-Math.atan2(dy,horizontal)*180.0/Math.PI);
        float factor=1.0f/Math.max(1.0f,c.smoothness);
        mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(desiredYaw-mc.thePlayer.rotationYaw)*factor*(c.speedX/10.0f);
        mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(desiredPitch-mc.thePlayer.rotationPitch)*factor*(c.speedY/10.0f);
        mc.thePlayer.rotationPitch=MathHelper.clamp_float(mc.thePlayer.rotationPitch,-90,90);
    }
    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Post event) {
        AimAssistConfig c=AimAssistMod.CONFIG;
        if(event.type!=RenderGameOverlayEvent.ElementType.ALL || !c.drawFov || !c.visualsFov || mc.thePlayer==null || !mc.isSingleplayer()) return;
        ScaledResolution sr=new ScaledResolution(mc);
        float radius=(float)(Math.tan(Math.toRadians(c.fov/2.0))*sr.getScaledWidth()/Math.tan(Math.toRadians(70.0/2.0)));
        RenderUtils.circle(sr.getScaledWidth()/2,sr.getScaledHeight()/2,radius,(c.color & 0x00FFFFFF) | (((int)(c.alpha*255))<<24),c.borderWidth);
    }
    private EntityLivingBase findTarget(AimAssistConfig c) {
        List<Entity> entities=mc.theWorld.getLoadedEntityList();
        EntityLivingBase best=null; double bestAngle=Double.MAX_VALUE;
        for (Entity e:entities) {
            if (!(e instanceof EntityMob) || !(e instanceof EntityLivingBase) || e==mc.thePlayer || e.isDead) continue;
            EntityLivingBase living=(EntityLivingBase)e;
            if (c.ignoreInvisible && living.isInvisible()) continue;
            double distance=mc.thePlayer.getDistanceToEntity(living);
            if (distance>c.maxDistance) continue;
            double dx=living.posX-mc.thePlayer.posX, dz=living.posZ-mc.thePlayer.posZ;
            float yaw=(float)(Math.atan2(dz,dx)*180/Math.PI)-90;
            float diff=Math.abs(MathHelper.wrapAngleTo180_float(yaw-mc.thePlayer.rotationYaw));
            if (diff>c.fov/2.0f) continue;
            if (diff<bestAngle) { bestAngle=diff; best=living; }
        }
        return best;
    }
}
