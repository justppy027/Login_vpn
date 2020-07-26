package com.telephone.coursetable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CheckCode {
    /**
     * @non-ui
     * @any-ui
     * @any
     * get the check code from the internet, and fill the check code image in specified ImageView
     * @param aty this method must be call in a non-UI thread, due to it has network operation, so
     *            the activity to which the ImageView belongs must be specified to call the runOnUiThread()
     * @return
     * - 0 success
     * - -1 cannot open url
     * - -2 cannot get input stream
     * - -3 cannot close input stream
     */
    public static HttpConnectionAndCode ShowCheckCode(final Activity aty, final ImageView im, final String urlString, final StringBuilder cookie_out, final String cookie_delimiter){
        HttpURLConnection cnt = null;
        InputStream is = null;
        Bitmap bmp = null;
        URL url = null;
        int resp_code = 0;
        try {
            url = new URL(urlString);
            cnt = (HttpURLConnection) url.openConnection();
            cnt.setDoInput(true);
            cnt.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(null, -1);
        }
        try {
            resp_code = cnt.getResponseCode();
            Log.e("ShowCheckCode() response code", ""+resp_code);
            is = cnt.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(null, -2);
        }
        bmp = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(null, -3);
        }

        //if success, update the ImageView with the check-code
        final Bitmap bmpf = bmp;
        aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                im.setImageBitmap(bmpf);
            }
        });

        /*
        if success and cookie_out(the cookie StringBuilder passed in) is not null, write the cookie set
        by server into the cookie_out */
        if (cookie_out != null) {
            CookieManager cookieman = new CookieManager();
            //getHeaderFields() returns the header fields of response
            List<String> cookies = cnt.getHeaderFields().get("Set-Cookie");

            if (cookies != null) {
                for (String cookie : cookies) {
                    cookieman.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            if (cookieman.getCookieStore().getCookies().size() > 0) {
                cookie_out.append(TextUtils.join(cookie_delimiter, cookieman.getCookieStore().getCookies()));
            }
        }

        //do not disconnect, keep alive
        //cnt.disconnect();
        return new HttpConnectionAndCode(cnt, 0);
    }

    /**
     * @ui
     * @any-ui
     * @any
     * this method call ShowCheckCode() in a new thread, so you can call it in UI thread. The @param aty is
     * still necessary. The return value of ShowCheckCode() is shown in @param res.
     * @param res pass in a HttpConnectionAndCode, the return value of ShowCheckCode() will be shown in that.
     */
    public static void ShowCheckCode_thread(final Activity aty, final ImageView im, final String urlString, final StringBuilder sb, final String cookie_delimiter, final HttpConnectionAndCode res){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpConnectionAndCode temp = ShowCheckCode(aty, im, urlString, sb, cookie_delimiter);
                res.c = temp.c;
                res.code = temp.code;
            }
        }).start();
    }
}