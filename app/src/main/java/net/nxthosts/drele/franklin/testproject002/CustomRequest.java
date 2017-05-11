package net.nxthosts.drele.franklin.testproject002;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by FrankLin on 2017/05/09.
 */

public class CustomRequest extends Request<JSONObject> {

    public JSONData dataReq;
    public JSONData dataResp;
    private Response.Listener<JSONData> listener;
    private Handler handler;

    public CustomRequest(int method, String url, JSONData request, JSONData response) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.dataReq = request;
        this.dataResp = response;

    }
    public CustomRequest(int method, String url, JSONData request, Response.Listener<JSONData> listener) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.dataReq = request;
        this.dataResp = new JSONData();
        this.listener = listener;
    }

    public CustomRequest(int method, String url, JSONData request, Handler handler) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.dataReq = request;
        this.dataResp = new JSONData();
        this.handler = handler;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap(super.getHeaders());
        //when doesn't send any headers, use default headers.
        if (dataReq.getHeaders() != null)
            headers.putAll(this.dataReq.getMapHeaders());
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap();
        //when doesn't send any parameters
        if (dataReq.getParams() != null)
            params.putAll(this.dataReq.getMapParams());
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String params = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject headers = new JSONObject(response.headers);
            String resp = String.format("{'headers': %s, 'params': %s}", headers.toString(), params);
            //return a success response
            return Response.success(new JSONObject(resp), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        Message msg = new Message();
        Bundle bundle = new Bundle();

        System.out.println("deliver:\t" + Calendar.getInstance().getTime());
        //Convert NetworkResponse to JSONData
        try {
            this.dataResp.setParams(response.getJSONObject("params").toString());
            this.dataResp.setHeaders(response.getJSONObject("headers").toString());
            if (handler != null) {
                bundle.putString("response", this.dataResp.toString());
                msg.setData(bundle);
                handler.handleMessage(msg);
            }
            if (listener != null) {
                listener.onResponse(dataResp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deliverError(VolleyError error) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        NetworkResponse response = error.networkResponse;
        try {
            String params = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject headers = new JSONObject(response.headers);
            this.dataResp.setHeaders(headers.toString());
            this.dataResp.setParams(params);
            if (handler != null) {
                bundle.putString("response", this.dataResp.toString() + "\n" + params);
                msg.setData(bundle);
                msg.what = -1;
                handler.handleMessage(msg);
            }
            if (listener != null) {
                this.listener.onResponse(dataResp);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        this.getErrorListener().onErrorResponse(error);
    }
}
