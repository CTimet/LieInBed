package io.github.ctimet.lieinbedapp0.servers.connect;

public interface InformationStream {
    boolean hasNextLine();
    String nextLine();
    void println(String line);
}
