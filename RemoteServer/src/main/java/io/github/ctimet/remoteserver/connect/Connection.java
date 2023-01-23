package io.github.ctimet.remoteserver.connect;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Scanner;

public class Connection {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private final SymmetricCrypto aes;
    private final Scanner in;
    private final PrintStream out;
    private Listener listener = line -> logger.warn("抛弃line: {}", line);
    public Connection(byte[] key, Scanner in, PrintStream out) {
        this.aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        this.in = in;
        this.out = out;
    }

    public Connection setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public synchronized void send(String json) {
        out.println(aes.encryptHex(json));
    }

    public void init() {
        while (in.hasNextLine()) {
            listener.listen(aes.decryptStr(in.nextLine(), CharsetUtil.CHARSET_UTF_8));
        }
    }

    interface Listener {
        void listen(String line);
    }
}
