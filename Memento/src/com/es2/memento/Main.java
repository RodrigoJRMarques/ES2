package com.es2.memento;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        BackupService backupService = new BackupService(server);

        try {
            server.addStudent("Ana");
            server.addStudent("Bruno");
            backupService.takeSnapshot();

            server.addStudent("Carla");
            backupService.takeSnapshot();

            System.out.println("Estado atual: " + server.getStudentNames());

            backupService.restoreSnapshot(0);
            System.out.println("Após restore do snapshot 0: " + server.getStudentNames());

            try {
                backupService.restoreSnapshot(99);
            } catch (NotExistingSnapshotException e) {
                System.out.println("Snapshot inexistente detectado corretamente.");
            }

            try {
                server.addStudent("Ana");
            } catch (ExistingStudentException e) {
                System.out.println("Aluno duplicado detectado corretamente.");
            }
        } catch (ExistingStudentException | NotExistingSnapshotException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}
