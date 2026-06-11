package com.ame.audio;

import com.ame.AMEEngine;
import com.ame.ConfigLoader;
import com.ame.SoundpackLoader;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AMEAudio {

    private final AMEEngine engine;
    private final JavaBackend backend;
    private final ScheduledExecutorService scheduler;

    public AMEAudio(String configPath, String soundsPath) throws IOException {
        ConfigLoader config = new ConfigLoader(configPath);
        config.load();

        SoundpackLoader sounds = new SoundpackLoader(soundsPath);
        this.backend = new JavaBackend();
        this.engine = new AMEEngine(backend, config, sounds);

        // Tick-Loop — 20 mal pro Sekunde
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(engine::tick, 0, 50, TimeUnit.MILLISECONDS);

        System.out.println("[AMEAudio] Ready.");
    }

    // ── Public API — delegiert an AMEEngine ──────────────────

    public void setContext(String name) {
        engine.setContext(name);
    }

    public void clearContext(String name) {
        engine.clearContext(name);
    }

    public void setBiome(String biome) {
        engine.setBiome(biome);
    }

    public void setSoundpack(String soundpack) {
        engine.setSoundPack(soundpack);
    }

    public void setIntensity(float value, int rampBars) {
        engine.setIntensity(value, rampBars);
    }

    public void triggerStinger(String name) {
        engine.triggerStinger(name);
    }

    public void fadeOut(int bars) {
        engine.fadeOut(bars);
    }

    public void shutdown() {
        scheduler.shutdown();
        backend.shutdown();
    }
}