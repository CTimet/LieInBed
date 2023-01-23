package io.github.ctimet.lieinbedapp.server.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.google.gson.JsonObject;
import io.github.ctimet.lieinbedapp.server.Listener;
import io.github.ctimet.lieinbedapp.server.ListenerType;
import io.github.ctimet.lieinbedapp.server.ServerConnect;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class RemoteServerConnect implements ServerConnect {
    private final SymmetricCrypto aes;
    private final Scanner in;
    private final PrintStream out;
    private final LinkedList<Listener> listeners = new LinkedList<>();
    public RemoteServerConnect(byte[] key, Scanner in, PrintStream out) {
        this.aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        this.in = in;
        this.out = out;
    }

    @Override
    public void println(String line) {
        out.println(aes.encryptHex(line, StandardCharsets.UTF_8));
    }

    @Override
    public void addListener(Listener listener, ListenerType type) {
        listeners.add(listener);
    }

    @Override
    public void startServer() {

    }

    @Override
    public void stopServer() {

    }

    @Override
    public void restartServer() {

    }

    @Override
    public void init() {
        String nextLine;
        JsonObject object = new JsonObject().getAsJsonObject("");
        while (in.hasNextLine()) {
            nextLine = in.nextLine();

        }
    }
}
