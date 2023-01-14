package io.github.ctimet.remoteserver.connect;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.github.ctimet.remoteserver.task.Task;

import java.io.PrintStream;
import java.util.Scanner;

public class Connection {
    private final SymmetricCrypto aes;
    private final Scanner in;
    private final PrintStream out;
    private Listener listener;
    public Connection(byte[] key, Scanner in, PrintStream out) {
        this.aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        this.in = in;
        this.out = out;
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public synchronized void send(String json) {
        out.println(aes.encryptHex(json));
    }

    public void init() {
        Task.runCached(() -> {
            while (in.hasNextLine()) {
                if (listener != null) {
                    listener.listen(aes.decryptStr(in.nextLine(), CharsetUtil.CHARSET_UTF_8));
                }
            }
        });
    }

    interface Listener {
        void listen(String line);
    }
}
