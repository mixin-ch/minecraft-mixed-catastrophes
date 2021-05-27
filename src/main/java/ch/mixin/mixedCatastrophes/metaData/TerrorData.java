package ch.mixin.mixedCatastrophes.metaData;

import java.util.ArrayList;

public class TerrorData {
    private ArrayList<StalkerData> stalkerDatas;
    private int terrorTimer;
    private int assaultTimer;
    private int stalkerTimer;
    private int paranoiaTimer;

    public TerrorData() {
        stalkerDatas = new ArrayList<>();
        terrorTimer = 0;
        assaultTimer = 0;
        stalkerTimer = 0;
        paranoiaTimer = 0;
    }

    public ArrayList<StalkerData> getStalkerDatas() {
        return stalkerDatas;
    }

    public void setStalkerDatas(ArrayList<StalkerData> stalkerDatas) {
        this.stalkerDatas = stalkerDatas;
    }

    public int getTerrorTimer() {
        return terrorTimer;
    }

    public void setTerrorTimer(int terrorTimer) {
        this.terrorTimer = terrorTimer;
    }

    public int getAssaultTimer() {
        return assaultTimer;
    }

    public void setAssaultTimer(int assaultTimer) {
        this.assaultTimer = assaultTimer;
    }

    public int getStalkerTimer() {
        return stalkerTimer;
    }

    public void setStalkerTimer(int stalkerTimer) {
        this.stalkerTimer = stalkerTimer;
    }

    public int getParanoiaTimer() {
        return paranoiaTimer;
    }

    public void setParanoiaTimer(int paranoiaTimer) {
        this.paranoiaTimer = paranoiaTimer;
    }
}
