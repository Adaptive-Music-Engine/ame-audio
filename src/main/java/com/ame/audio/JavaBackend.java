package com.ame.audio;

import com.ame.IAudioBackend;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavaBackend implements IAudioBackend {

    // Laufende Stems — Key: Stem-ID, Value: Clip-Instanz
    private final Map<String, Clip> activeStems = new HashMap<>();

    public JavaBackend() {
        System.out.println("[AUDIO] JavaSoundBackend initialized.");
    }

    @Override
    public void playStem(String id, float volume) {
        // Falls der Stem schon läuft, erst stoppen
        stopStem(id);

        try {
            Clip clip = loadClip(id);
            setClipVolume(clip, volume);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            activeStems.put(id, clip);
            System.out.println("[AUDIO] Playing stem: " + id + " at volume " + volume);
        } catch (Exception e) {
            System.err.println("[AUDIO] Failed to load stem: " + id + " — " + e.getMessage());
        }
    }

    @Override
    public void setStemVolume(String id, float volume) {
        Clip clip = activeStems.get(id);
        if (clip != null) {
            setClipVolume(clip, volume);
        }
    }

    @Override
    public void stopStem(String id) {
        Clip clip = activeStems.get(id);
        if (clip != null) {
            clip.stop();
            clip.close();
            activeStems.remove(id);
            System.out.println("[AUDIO] Stopped stem: " + id);
        }
    }

    @Override
    public void playOneShot(String id) {
        try {
            Clip clip = loadClip(id);
            setClipVolume(clip, 1.0f);
            clip.start();
            // Clip automatisch schliessen wenn fertig
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            System.out.println("[AUDIO] One-shot: " + id);
        } catch (Exception e) {
            System.err.println("[AUDIO] Failed to load stinger: " + id + " — " + e.getMessage());
        }
    }

    public void shutdown() {
        activeStems.values().forEach(clip -> {
            clip.stop();
            clip.close();
        });
        activeStems.clear();
        System.out.println("[AUDIO] JavaSoundBackend shut down.");
    }

    // ── Intern ───────────────────────────────────────────────

    private Clip loadClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream raw = AudioSystem.getAudioInputStream(new File(path));

        // OGG muss in PCM konvertiert werden damit Java Sound es abspielen kann
        AudioFormat baseFormat = raw.getFormat();
        AudioFormat pcmFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
        );

        AudioInputStream pcmStream = AudioSystem.getAudioInputStream(pcmFormat, raw);
        Clip clip = AudioSystem.getClip();
        clip.open(pcmStream);
        return clip;
    }

    private void setClipVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Volume 0.0-1.0 in Dezibel umrechnen
            float dB = volume > 0
                    ? (float) (Math.log10(volume) * 20)
                    : gainControl.getMinimum();
            gainControl.setValue(Math.max(gainControl.getMinimum(),
                    Math.min(gainControl.getMaximum(), dB)));
        }
    }
}