package io.github.ctimet.lieinbedapp0.properties;

public class UsersDataImplFile implements UsersData {
    private UsersProperties pps;
    @Override
    public void readData() {
        pps = AppProperties.getUsersProperties();
    }

    @Override
    public boolean checkAdmin(String user, String password) {
        return pps.contains(user, password);
    }
}
