package com.es2.singleton; // This line MUST match the folder path

public class Registry {
    // The single instance of this class
    private static Registry instance;

    // Data fields from your documentation
    private String path;
    private String connectionString;

    // Private constructor so no one else can call 'new Registry()'
    private Registry() {}

    // Static method to get the single instance
    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    // Returns the path where the application stores files
    public String getPath() {
        return path;
    }

    // Sets the path where the application stores files
    public void setPath(String path) {
        this.path = path;
    }

    // Returns the connection string
    public String getConnectionString() {
        return connectionString;
    }

    // Sets the connection string
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}