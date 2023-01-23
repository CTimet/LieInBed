package io.github.ctimet.lieinbedapp0.properties;

public interface UsersData {
    void readData();
    boolean checkAdmin(String user, String password);
}
