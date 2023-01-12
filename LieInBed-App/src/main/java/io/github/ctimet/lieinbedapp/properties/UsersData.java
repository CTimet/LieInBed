package io.github.ctimet.lieinbedapp.properties;

public interface UsersData {
    void readData();
    boolean checkAdmin(String user, String password);
}
