package io.github.ctimet.lieinbedapp.test;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import java.text.DecimalFormat;
import java.util.Scanner;

public class TestApp {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        do {
            getOsInfo();
        } while (!in.nextLine().equals("STOP"));
        in.close();
    }

    /**
     * 获取cpu利用率
     */
    public static void getOsInfo(){
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();

        double free = cpuInfo.getFree();
        DecimalFormat format = new DecimalFormat("#.00");
        System.out.println("cpu利用率：" + Double.parseDouble(format.format(100.0D - free)));
    }
}