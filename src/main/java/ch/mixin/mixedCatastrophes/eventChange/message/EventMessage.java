package ch.mixin.mixedCatastrophes.eventChange.message;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.eventChange.EventChange;
import org.bukkit.ChatColor;

public class EventMessage {
    private final EventChange eventChange;
    private final String text;

    private boolean title;
    private AspectType cause;
    private ChatColor color;

    public EventMessage(EventChange eventChange, String text) {
        this.eventChange = eventChange;
        this.text = text;
    }

    public EventChange finish(){
        return eventChange;
    }

    public EventMessage withTitle(boolean title) {
        this.title = title;
        return this;
    }

    public EventMessage withCause(AspectType cause) {
        this.cause = cause;
        return this;
    }

    public EventMessage withColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public String getText() {
        return text;
    }

    public boolean isTitle() {
        return title;
    }

    public AspectType getCause() {
        return cause;
    }

    public ChatColor getColor() {
        return color;
    }
}
