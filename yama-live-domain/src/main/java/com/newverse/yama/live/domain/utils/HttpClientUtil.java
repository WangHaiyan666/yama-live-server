package com.newverse.yama.live.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author liangyu
 */
@Slf4j
public class HttpClientUtil {

    /**
     * 封装 doGet方法，执行
     *
     * @param url get url
     * @return response string
     */
    public static String doGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response;

        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                String result = EntityUtils.toString(entity, "UTF-8");

                EntityUtils.consume(entity);

                httpClient.close();

                return result;
            }
            httpClient.close();
        } catch (IOException e) {
            log.error("httpClient.doGet.error:{}", e.getMessage());
            return null;
        }

        return null;
    }
}
