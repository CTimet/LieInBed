package io.github.ctimet.remoteserver;

import io.github.ctimet.remoteserver.connect.Listener;
import io.github.ctimet.remoteserver.task.Task;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppProperties.checkProperties();
        Scanner in = new Scanner(System.in);
        int port = takePort(in);
        String password;
        if (AppProperties.getPps().getProperty("debug").equals("false")) {
            password = takePassword();
        } else {
            password = takePassword0(in);
        }
        try {
            Task.runCached(() -> {
                try {
                    System.out.println("LieInBed-RemoteServer已在端口" + port + "上启用，等待其余组件连接。键入stop以停止工作。");
                    Listener.startConnector(port, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Task.runCached(() -> {
                String nextLine;
                do {
                    nextLine = in.nextLine();
                } while (!nextLine.equalsIgnoreCase("stop"));
                in.close();
                Task.closeRemoteServer();
            });
        } catch (Exception e) {
            System.out.println("发生错误！");
            e.printStackTrace();
        }
    }

    private static int takePort(Scanner in) {
        int port = takePort0(in);
        System.out.println("确认在端口 " + port + " 上启用LieInBed-RemoteServer？请确保此端口已开放至公网并且无其他应用程序占用。");
        System.out.println("确认无误后，请键入y以确认，键入n以重新输入端口号。");
        port = waitPortConfirm(in, port);
        return port;
    }

    private static int waitPortConfirm(Scanner in, int port) {
        String next;
        while (true) {
            next = in.next();
            if (next.equalsIgnoreCase("y")) {
                break;
            } else if (next.equalsIgnoreCase("n")) {
                port = takePort0(in);
                System.out.println("确认在端口 " + port + " 上启用LieInBed-RemoteServer？请确保此端口已开放至公网并且无其他应用程序占用。");
                System.out.println("确认无误后，请键入y以确认，键入n以重新输入端口号。");
            } else {
                System.out.println(next + " 不是有效的命令！请重新键入！");
            }
        }
        return port;
    }

    private static int takePort0(Scanner in) {
        String next;
        int port;
        while (true) {
            System.out.print("请输入要监听的端口号：");
            try {
                next = in.next();
                port = Integer.parseInt(next);
                if (port < 0 || port > 65535) {
                    System.out.println("您输入的不是有效的端口！请重新输入！");
                    continue;
                }
                break;
            } catch (Throwable t) {
                System.out.println("您输入的不是数字！请重新输入！");
            }
        }
        return port;
    }

    private static String takePassword() {
        Console console = System.console();
        char[] password;
        char[] pass;
        while (true) {
            System.out.print("请键入连接密码：");
            password = console.readPassword();
            System.out.print("请确认连接密码：");
            pass = console.readPassword();
            if (Arrays.hashCode(pass) != Arrays.hashCode(password)) {
                System.out.println("密码键入不对等！请重新键入！");
                continue;
            }
            break;
        }
        return new String(password);
    }

    //测试使用的桩函数，Console在IDEA运行时会报错。此函数使用明文传输密码
    private static String takePassword0(Scanner in) {
        String password;
        String pass;
        while (true) {
            System.out.print("请键入连接密码：");
            password = in.next();
            System.out.print("请确认连接密码：");
            pass = in.next();
            if (pass.hashCode() != password.hashCode()) {
                System.out.println("密码键入不对等！请重新键入！");
                continue;
            }
            break;
        }
        return password;
    }
}
