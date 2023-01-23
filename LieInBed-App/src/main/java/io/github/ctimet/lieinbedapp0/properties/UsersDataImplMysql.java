package io.github.ctimet.lieinbedapp0.properties;

public class UsersDataImplMysql implements UsersData {
    @Override
    public void readData() {

    }

    @Override
    public boolean checkAdmin(String user, String password) {
        return false;
    }
}
