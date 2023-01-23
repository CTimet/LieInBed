package io.github.ctimet.lieinbedapp0.servers.connect.impl;

import io.github.ctimet.lieinbedapp0.servers.connect.InformationStream;

import java.io.PrintStream;
import java.util.Scanner;

public class LocalInformationStream implements InformationStream {
    private final Scanner in;
    private final PrintStream out;
    public LocalInformationStream(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public boolean hasNextLine() {
        return in.hasNextLine();
    }

    @Override
    public String nextLine() {
        return in.nextLine();
    }

    @Override
    public void println(String line) {
        out.println(line);
    }
}
