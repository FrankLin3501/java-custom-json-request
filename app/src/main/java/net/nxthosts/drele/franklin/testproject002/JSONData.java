package net.nxthosts.drele.franklin.testproject002;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by FrankLin on 2017/05/09.
 */

public class JSONData {
    private JSONObject headers;
    private JSONObject params;

    public JSONData() {

    }

    public boolean hasData() {
        return (headers != null && params != null);
    }

    public void setHeaders(String value) {
        try {
            if (value.equals("")) {
                this.headers = new JSONObject();
            } else {
                this.headers = new JSONObject(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setParams(String value) {
        try {
            if (value.equals("")) {
                this.params = new JSONObject();
            } else {
                this.params = new JSONObject(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getHeaders() {
        return this.headers;
    }

    public JSONObject getParams() {
        return this.params;
    }

    public Map<String, String> getMapHeaders() {
        return jsonToMap(this.headers);
    }

    public Map<String, String> getMapParams() {
        return jsonToMap(this.params);
    }

    public Map<String, String> jsonToMap(JSONObject json) {
        Map<String, String> map = new HashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            try {
                String key = keys.next();
                String value = json.getString(key);
                map.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public String toString() {
        if (headers == null && params == null) return "error";
        if (headers == null && params != null) return params.toString();
        if (headers != null && params == null) return headers.toString();

        return String.format("%s\n--------------------------\n%s", headers.toString(), params.toString());
    }
}
