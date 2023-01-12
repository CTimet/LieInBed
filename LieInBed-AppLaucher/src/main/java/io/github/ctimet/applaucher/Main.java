package io.github.ctimet.applaucher;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://ctimet.github.io/LieInBedUpdateWebsite/");
        long startTime = System.currentTimeMillis();
//        Connection connection = Jsoup.connect("https://gitee.com/ctimet/LieInBedUpdateWebsite");
        Document document = Jsoup.parse(url, 20000);
        System.out.println("Time to connect: " + (System.currentTimeMillis() - startTime));
        System.out.println("LatestAppVersion: " + document.getElementById("LatestAppVersion").text());
        System.out.println("LatestRobotLibVersion: " + document.getElementById("LatestRobotLibVersion").text());
        System.out.println("LatestWebLibVersion: " + document.getElementById("LatestWebLibVersion").text());
        System.out.println("LatestAppDownloadURL: " + document.getElementById("LatestAppDownloadURL").text());
        System.out.println("LatestRobotDownloadURL: " + document.getElementById("LatestRobotDownloadURL").text());
        System.out.println("LatestWebLibDownloadURL: " + document.getElementById("LatestWebLibDownloadURL").text());
//        System.out.println(document.getElementsByClass("file_content markdown-body").text());
    }
}
