package com.risenb.expand.network.cookie;


import okhttp3.CookieJar;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2018/3/12
 * 描    述：This interface extends {@link okhttp3.CookieJar} and adds methods to clear the cookies.
 * 修订历史：
 * ================================================
 */
public interface ClearableCookieJar extends CookieJar {
    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();
}
