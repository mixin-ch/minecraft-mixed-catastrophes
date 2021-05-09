package ch.mixin.eventChange.sound;

import org.bukkit.Sound;

public class EventSound {
    private final Sound sound;

    public EventSound(Sound sound) {
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }
}
