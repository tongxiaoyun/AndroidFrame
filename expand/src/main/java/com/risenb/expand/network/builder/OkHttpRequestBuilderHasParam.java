package com.risenb.expand.network.builder;

import com.risenb.expand.network.MyOkHttp;
import com.risenb.expand.utils.Log;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 带有param的base request body
 * Created by tsy on 2016/12/6.
 */

public abstract class OkHttpRequestBuilderHasParam<T extends OkHttpRequestBuilderHasParam> extends OkHttpRequestBuilder<T> {

    protected Map<String, String> mParams;

    public OkHttpRequestBuilderHasParam(MyOkHttp myOkHttp) {
        super(myOkHttp);
    }

    /**
     * set Map params
     *
     * @param params
     * @return
     */
    public T params(Map<String, String> params) {
        this.mParams = params;
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            //2、拼接字符串
            String key = (String) iterator.next();
            String value = params.get(key).toString();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("&");
        }
        Log.e("RequestParams >>>> " + sb.toString());
        return (T) this;
    }

    /**
     * add param
     *
     * @param key param key
     * @param val param val
     * @return
     */
    public T addParam(String key, String val) {
        if (this.mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, val);
        return (T) this;
    }
}
