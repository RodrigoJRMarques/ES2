package com.es2.memento;

import java.util.ArrayList;

public class BackupService {
    private final Server server;
    private final ArrayList<Memento> snapshots;

    public BackupService(Server server) {
        this.server = server;
        this.snapshots = new ArrayList<>();
    }

    public void takeSnapshot() {
        snapshots.add(server.backup());
    }

    public void restoreSnapshot(int snapshotNumber) throws NotExistingSnapshotException {
        if (snapshotNumber < 0 || snapshotNumber >= snapshots.size()) {
            throw new NotExistingSnapshotException();
        }
        server.restore(snapshots.get(snapshotNumber));
    }
}
