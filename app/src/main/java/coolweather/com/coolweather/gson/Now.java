package coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 2018/7/3.
 */

public class Now {
    public String tmp;

    @SerializedName("cond")
    public More more;

    public class More{
        public String txt;
    }
}
