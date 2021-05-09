package ch.mixin.eventChange;

import ch.mixin.eventChange.aspect.AspectChange;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.eventChange.message.EventMessage;
import ch.mixin.eventChange.sound.EventSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class EventChange {
    private final EventChangeManager eventChangeManager;
    private final Player player;

    private AspectChange aspectChange;
    private EventMessage eventMessage;
    private EventSound eventSound;

    public EventChange(EventChangeManager eventChangeManager, Player player) {
        this.eventChangeManager = eventChangeManager;
        this.player = player;
    }

    public void execute() {
        eventChangeManager.execute(this);
    }

    public EventChange withAspectChange(HashMap<AspectType, Integer> changeMap) {
        aspectChange = new AspectChange(changeMap);
        return this;
    }

    public EventMessage withEventMessage(String text) {
        eventMessage = new EventMessage(this, text);
        return eventMessage;
    }

    public EventChange withEventSound(Sound sound) {
        eventSound = new EventSound(sound);
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public AspectChange getAspectChange() {
        return aspectChange;
    }

    public EventMessage getEventMessage() {
        return eventMessage;
    }

    public EventSound getEventSound() {
        return eventSound;
    }
}
