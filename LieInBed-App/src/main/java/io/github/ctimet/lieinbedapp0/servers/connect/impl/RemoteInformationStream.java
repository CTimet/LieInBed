package io.github.ctimet.lieinbedapp0.servers.connect.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.github.ctimet.lieinbedapp0.servers.connect.InformationStream;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RemoteInformationStream implements InformationStream {
    private final SymmetricCrypto aes;
    private final Scanner in;
    private final PrintStream out;

    public RemoteInformationStream(byte[] key, Scanner in, PrintStream out) {
        this.aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        this.in = in;
        this.out = out;
    }

    @Override
    public boolean hasNextLine() {
        return in.hasNextLine();
    }

    @Override
    public String nextLine() {
        return aes.decryptStr(in.nextLine(), StandardCharsets.UTF_8);
    }

    @Override
    public void println(String line) {
        out.println(aes.encryptHex(line, StandardCharsets.UTF_8));
    }
}
