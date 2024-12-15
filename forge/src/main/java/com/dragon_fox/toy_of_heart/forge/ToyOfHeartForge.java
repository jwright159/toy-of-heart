package com.dragon_fox.toy_of_heart.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.dragon_fox.toy_of_heart.ToyOfHeart;

@Mod(ToyOfHeart.MOD_ID)
public final class ToyOfHeartForge {
    public ToyOfHeartForge(FMLJavaModLoadingContext context) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(ToyOfHeart.MOD_ID, context.getModEventBus());

        // Run our common setup.
        ToyOfHeart.init();
    }
}
