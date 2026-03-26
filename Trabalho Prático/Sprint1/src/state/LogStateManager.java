package state;

import config.LogConfig;
import config.LogConfigSnapshot;

import java.util.ArrayDeque;
import java.util.Deque;

public class LogStateManager {

    private final LogConfig logConfig;
    private final Deque<LogConfigSnapshot> snapshots;

    public LogStateManager(LogConfig logConfig) {
        this.logConfig = logConfig;
        this.snapshots = new ArrayDeque<>();
    }

    public void saveState() {
        snapshots.push(logConfig.createSnapshot());
    }

    public boolean restoreLastState() {
        if (snapshots.isEmpty()) {
            return false;
        }

        logConfig.restore(snapshots.pop());
        return true;
    }

    public int getSavedStatesCount() {
        return snapshots.size();
    }
}