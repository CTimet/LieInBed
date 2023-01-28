package io.github.ctimet.lieinbedapp.server.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONObject;
import io.github.ctimet.lieinbedapp.server.Listener;
import io.github.ctimet.lieinbedapp.server.ServerConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class RemoteServerConnect implements ServerConnect {
    private static final Logger logger = LoggerFactory.getLogger(RemoteServerConnect.class);
    private final SymmetricCrypto aes;
    private final Scanner in;
    private final PrintStream out;
    private final HashMap<Integer, Listener> listenerMap = new HashMap<>();

    private int sendCount = 0;

    public RemoteServerConnect(byte[] key, Scanner in, PrintStream out) {
        this.aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        this.in = in;
        this.out = out;
    }

    @Override
    public void send(JSONObject object, Listener listener) {
        sendCount ++;
        listenerMap.put(sendCount, listener);
        object.set("count", sendCount);
        out.println(aes.encryptHex(object.toString(), StandardCharsets.UTF_8));
    }

    @Override
    public void send(String line, Listener listener) {
        //一个正常的JSON消息应该
        /*
            {
                "cmd":"send xxx"
                "from":"console" //from有好几个值，分别是 console, performance, players, properties 和 nothing
            }
        */
        send(new JSONObject().set("cmd", line), listener);
    }

    @Override
    public void startServer() {

    }

    @Override
    public void stopServer() {

    }

    @Override
    public void stopConnect() {
        out.println("CLOSE CONNECT");
    }

    @Override
    public void restartServer() {

    }

    @Override
    public void init() {
        JSONObject object;
        while (in.hasNextLine()) {
            object = new JSONObject(in.nextLine());
        }
    }
}
