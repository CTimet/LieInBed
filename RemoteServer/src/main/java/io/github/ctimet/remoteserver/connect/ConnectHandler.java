package io.github.ctimet.remoteserver.connect;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ConnectHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectHandler.class);
    private static final HashMap<String, LinkedList<Connection>> connMap = new HashMap<String, LinkedList<Connection>>() {{
        put("APP", new LinkedList<>());
        put("ROBOT", new LinkedList<>());
        put("WEB", new LinkedList<>());
    }};
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
            //首先第一步，校验密
            if (in.hasNextLine() && in.nextLine().equals(password)) {
                out.println("PWT");
                //第二步，生成对称密钥，然后用发来的公钥加密
                //发来的公钥
                if (!in.hasNextLine()) return;
                String publicKey = in.nextLine();
                //构建RSA对象
                RSA rsa = new RSA(null, publicKey);
                //生成对称密钥
                byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
                //使用公钥加密对称密钥，并发送回去
                out.println(rsa.encryptHex(key, KeyType.PublicKey));

                //第三步，确认这是哪个连接，并建立Connection
                if (!in.hasNextLine()) return;
                switch (in.nextLine()) {
                    case "APP":
                        Connection app = new Connection(key, in, out);
                        connMap.get("APP").add(app);
                        app.init();
                        break;
                    case "ROBOT":
                        Connection robot = new Connection(key, in, out);
                        connMap.get("ROBOT").add(robot);
                        robot.init();
                        break;
                    case "WEB":
                        Connection conn = new Connection(key, in, out);
                        connMap.get("WEB").add(conn);
                        conn.init();
                        break;
                    default:
                        logger.warn("有不明来源的连接尝试连接到此！");
                }
            } else {
                out.println("PWW");
                System.out.println("发送PWW");
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
