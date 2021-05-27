package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.assault;

import org.bukkit.entity.EntityType;

public class AssaultPremise {
    protected final EntityType entityType;
    protected final String message;
    protected final double amount;

    public AssaultPremise(EntityType entityType, String message, double amount) {
        this.entityType = entityType;
        this.message = message;
        this.amount = amount;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getMessage() {
        return message;
    }

    public double getAmount() {
        return amount;
    }
}
