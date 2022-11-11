package org.LinkTechTips;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Thread.*;

public final class Main {
    public static void noaa() throws IOException {
        int bytesum = 0;
        int byteread;
        // 取得当前时间
        Calendar calendar = Calendar.getInstance() ;
        // 取得时间偏移量
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        // 取得夏令时差
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        // 从本地时间里扣除这些差量,即可以取得UTC时间
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String hour = simpleDateFormat.format(calendar.getTime());

        String fileUrl  = "https://tgftp.nws.noaa.gov/data/observations/metar/cycles/"+ hour + "Z.TXT";
        URL url = new URL(fileUrl);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        try (FileOutputStream fs = new FileOutputStream(System.getProperty("user.dir") + "/metar.txt")) {
            System.out.println("Start get NOAA METAR");
            byte[] buffer = new byte[8192];
            if ((byteread = inStream.read(buffer)) != -1) {
                do {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                } while ((byteread = inStream.read(buffer)) != -1);
            }
        }
        System.out.println("Get NOAA METAR done!");
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        while (true) {
            noaa();
            System.out.println("MetarSpider will be activated again in 30 minutes");
            Thread.sleep(1000 * 60 * 30);
        }
    }
}
