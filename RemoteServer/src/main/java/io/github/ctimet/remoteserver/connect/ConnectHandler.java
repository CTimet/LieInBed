package io.github.ctimet.remoteserver.connect;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ConnectHandler implements Runnable {
    private static final HashMap<String, Connection> connMap = new HashMap<>();
    private final Socket socket;
    private final String password;
    public ConnectHandler(Socket socket, String password) {
        this.socket = socket;
        this.password = password;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             Scanner in = new Scanner(input, "UTF-8");
             PrintStream out = new PrintStream(output, true, "UTF-8")) {
            //首先第一步，校验密码
            if (in.nextLine().equals(password)) {
                //第二步，生成对称密钥，然后用发来的公钥加密
                //发来的公钥
                String publicKey = in.nextLine();
                //构建RSA对象
                RSA rsa = new RSA(null, publicKey);
                //生成对称密钥
                byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
                //使用公钥加密对称密钥，并发送回去
                out.println(StrUtil.str(rsa.encrypt(key, KeyType.PublicKey), CharsetUtil.CHARSET_UTF_8));

                //第三步，确认这是哪个连接，并建立Connection
                String connectType = in.nextLine();
                switch (connectType) {
                    case "APP-CONSOLE":
                        if (connMap.containsKey("APP-CONSOLE")) {
                            return;
                        }
                        connMap.put("APP-CONSOLE", new Connection(key, in, out));
                        break;
                    case "APP-PERFORMANCE":
                        if (connMap.containsKey("APP-PERFORMANCE")) {
                            return;
                        }
                        connMap.put("APP-PERFORMANCE", new Connection(key, in, out));
                        break;
                    case "APP-PLAYERS":
                        if (connMap.containsKey("APP-PLAYERS")) {
                            return;
                        }
                        connMap.put("APP-PLAYERS", new Connection(key, in, out));
                        break;
                    case "ROBOT-PERFORMANCE":
                        if (connMap.containsKey("ROBOT-PERFORMANCE")) {
                            return;
                        }
                        connMap.put("ROBOT-PERFORMANCE", new Connection(key, in, out));
                        break;
                    default:
                }
            } else {
                out.println("PW");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
