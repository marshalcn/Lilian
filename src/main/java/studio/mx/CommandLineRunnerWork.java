package studio.mx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

@Component
public class CommandLineRunnerWork implements CommandLineRunner {
    @Value("${url}")
    private String url;

    private static final String BASE_URL = "ipaddress.com";
    private static final String FULL_URL = "https://ipaddress.com/";
    private static final String APP_URL = "https://ipaddress.com/website/";
    public static String getContent(String urls) throws Exception{
        URL url = new URL(APP_URL + urls);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        Long timeStamp = System.currentTimeMillis() / 1000;
        httpConn.setRequestProperty("authority", BASE_URL);
        httpConn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpConn.setRequestProperty("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,ja-JP;q=0.6,ja;q=0.5");
        httpConn.setRequestProperty("cache-control", "no-cache");
        httpConn.setRequestProperty("cookie", "ezosuibasgeneris-1=36b75277-f7b1-48f4-5725-a7e81d135b28; ezds=ffid%3D1%2Cw%3D1920%2Ch%3D1080; ezux_ifep_280870=true; ezohw=w%3D1920%2Ch%3D929; ezux_lpl_280870=1661160169411|ac4a35c9-e957-49c2-6267-04668912005e|true; ezoadgid_280870=-1; ezoref_280870=ipaddress.com; ezoab_280870=mod16-c; active_template::280870=pub_site.1661217340; ezopvc_280870=1; ezepvv=3362; ezovid_280870=1764774769; lp_280870=https://ipaddress.com/website/" + urls +"; ezovuuidtime_280870=" + timeStamp + "; ezovuuid_280870=6a49d3d5-e7a2-426d-55a3-178d57fee27d; ezux_et_280870=155; ezux_tos_280870=63516");
        httpConn.setRequestProperty("pragma", "no-cache");
        httpConn.setRequestProperty("referer", FULL_URL);
        httpConn.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        httpConn.setRequestProperty("sec-fetch-dest", "document");
        httpConn.setRequestProperty("sec-fetch-mode", "navigate");
        httpConn.setRequestProperty("sec-fetch-site", "same-origin");
        httpConn.setRequestProperty("sec-fetch-user", "?1");
        httpConn.setRequestProperty("upgrade-insecure-requests", "1");
        httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
//        System.out.println(response);
        return response;
    }
    public static String analysis(String content){
        Document document = Jsoup.parse(content);
        String [] ips = document.select("ul[class=comma-separated]").text().split(" ");
        for (String ip: ips){
            System.out.println(ip);
        }
        if (ips.length > 0){
            return ips[0];
        }
        return null;
    }

    @Override
    public void run(String... args) throws Exception {
        String [] urls = url.split(",");
        File hosts = new File("hosts");
        if (urls.length > 0){
            FileWriter fileWriter = new FileWriter(hosts);
            for (String url: urls){
                String content = getContent(url.trim());
                String ip = analysis(content);
                if (ip == null){
                    continue;
                }
                fileWriter.append(ip + "  " + url + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
