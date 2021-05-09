package ch.mixin.eventChange.aspect;

import java.util.HashMap;

public class AspectChange {
    private final HashMap<AspectType, Integer> changeMap;

    public AspectChange(HashMap<AspectType, Integer> changeMap) {
        this.changeMap = changeMap;
    }

    public HashMap<AspectType, Integer> getChangeMap() {
        return changeMap;
    }
}
