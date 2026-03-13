package com.es2.memento;

import java.util.ArrayList;

public class Server {
    private ArrayList<String> studentNames;

    public Server() {
        this.studentNames = new ArrayList<>();
    }

    public void addStudent(String studentName) throws ExistingStudentException {
        if (studentNames.contains(studentName)) {
            throw new ExistingStudentException();
        }
        studentNames.add(studentName);
    }

    public Memento backup() {
        return new Memento(studentNames);
    }

    public void restore(Memento state) {
        this.studentNames = state.getState();
    }

    public ArrayList<String> getStudentNames() {
        return new ArrayList<>(studentNames);
    }
}
