package coolweather.com.coolweather.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by HP on 2018/6/27.
 */

public class HttpUtil {

    /**
     * 发送http请求
     * @param address
     * @param callback
     */
    public static void sendHttpRequest(String address, Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
