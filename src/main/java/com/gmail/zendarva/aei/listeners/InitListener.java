package com.gmail.zendarva.aei.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

/**
 * Created by James on 7/27/2018.
 */
public class InitListener implements InitializationListener {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialization() {
        LOGGER.info("Adding AEI Mixins");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.aei.json");

    }
}
