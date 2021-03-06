package tips.volleytest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Volley的简单回调封装
 */
public class MyVolleyRequest {

    private Context mContext;
    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;

    public MyVolleyRequest(Context context, final Callback callback) {

        mContext = context;

        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        };

        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        };
    }

    public void requestGet(String url, String tag) {

        //先取消已有的网络请求，避免重复请求
        MyApplication.getRequestQueue().cancelAll(tag);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, mListener, mErrorListener);
        stringRequest.setTag(tag);

        MyApplication.getRequestQueue().add(stringRequest);
        //启动该全局队列中的调度程序(dispatchers)
        MyApplication.getRequestQueue().start();
    }

    public void requestPost(String url, String tag, final Map<String, String> param) {

        MyApplication.getRequestQueue().cancelAll(tag);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, mListener, mErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return param;
            }
        };
        stringRequest.setTag(tag);

        MyApplication.getRequestQueue().add(stringRequest);
        MyApplication.getRequestQueue().start();
    }

    public interface Callback {
        void onSuccess(String response);

        void onError(VolleyError error);
    }
}
