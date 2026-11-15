package com.aimassist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class AimAssistConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public boolean enabled = true, requireSword = true, ignoreInvisible = true, ignoreTeammates = true;
    public boolean drawFov = true, menuAnimation = true, autoSave = true, visualsFov = true;
    public float speedX = 2.0f, speedY = 1.0f, fov = 45.0f, maxDistance = 6.0f, smoothness = 5.0f;
    public float alpha = 0.65f, borderWidth = 1.5f;
    public int color = 0xFF8B5CF6;
    public String targetMode = "MOBS";
    public boolean dirty = false;
    private File file() {
        return new File(net.minecraftforge.fml.common.Loader.instance().getConfigDir(), "aimassist.json");
    }
    public synchronized void load() {
        File f = file();
        if (!f.isFile()) { save(); return; }
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            AimAssistConfig c = GSON.fromJson(r, AimAssistConfig.class);
            if (c != null) copyFrom(c);
            validate(); dirty = false;
        } catch (Exception ex) { validate(); }
    }
    public synchronized void save() {
        validate();
        File f = file(), tmp = new File(f.getParentFile(), f.getName() + ".tmp");
        try {
            if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) return;
            try (Writer w = new OutputStreamWriter(new FileOutputStream(tmp), StandardCharsets.UTF_8)) { GSON.toJson(this, w); }
            try {
                Files.move(tmp.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            dirty = false;
        } catch (IOException ignored) { if (tmp.exists()) tmp.delete(); }
    }
    public void reset() {
        AimAssistConfig d = new AimAssistConfig();
        copyFrom(d); dirty = true; if (autoSave) save();
    }
    private void copyFrom(AimAssistConfig c) {
        enabled=c.enabled; requireSword=c.requireSword; ignoreInvisible=c.ignoreInvisible; ignoreTeammates=c.ignoreTeammates;
        drawFov=c.drawFov; menuAnimation=c.menuAnimation; autoSave=c.autoSave; visualsFov=c.visualsFov;
        speedX=c.speedX; speedY=c.speedY; fov=c.fov; maxDistance=c.maxDistance; smoothness=c.smoothness;
        alpha=c.alpha; borderWidth=c.borderWidth; color=c.color; targetMode=c.targetMode;
    }
    public void validate() {
        speedX=clamp(speedX,0,20); speedY=clamp(speedY,0,20); fov=clamp(fov,1,180);
        maxDistance=clamp(maxDistance,1,32); smoothness=clamp(smoothness,1,20);
        alpha=clamp(alpha,0.05f,1); borderWidth=clamp(borderWidth,0.5f,5);
        if (!"MOBS".equals(targetMode)) targetMode="MOBS";
    }
    private float clamp(float v,float min,float max) { return Float.isNaN(v)||Float.isInfinite(v)?min:Math.max(min,Math.min(max,v)); }
}
