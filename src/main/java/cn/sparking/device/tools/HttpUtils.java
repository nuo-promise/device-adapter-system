package cn.sparking.device.tools;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * post.
     * @param url request url.
     * @param param  request param.
     * @return {@link JSONObject}
     */
    public static HttpResponse post(final String url, final String param) {
        try {
            return HttpRequest.post(url)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(param).timeout(3000).execute();
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    /**
     * 发送命令.
     * @param url url
     * @param param param
     * @param appKey appKey
     * @param accessToken accessToke.
     * @return {@link HttpResponse}
     */
    public static HttpResponse post(final String url, final String param, final String appKey, final String accessToken) {
        try {
            return HttpRequest.post(url)
                    .header("app_key", appKey)
                    .header("access_token", accessToken)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(param).timeout(3000).execute();
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }
}
