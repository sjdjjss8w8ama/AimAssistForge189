package com.aimassist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "aimassist", name = "Aim Assist", version = "1.0.0", clientSideOnly = true)
public class AimAssistMod {
    public static final KeyBinding OPEN_GUI = new KeyBinding("key.aimassist.menu", Keyboard.KEY_RSHIFT, "key.categories.aimassist");
    public static final AimAssistConfig CONFIG = new AimAssistConfig();
    public static final AimAssistManager MANAGER = new AimAssistManager();
    private static final Minecraft MC = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CONFIG.load();
        ClientRegistry.registerKeyBinding(OPEN_GUI);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(MANAGER);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (OPEN_GUI.isPressed() && MC.thePlayer != null) {
            MC.displayGuiScreen(new ClickGUI());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && CONFIG.autoSave && CONFIG.dirty) CONFIG.save();
    }
}
