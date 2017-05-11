package net.nxthosts.drele.franklin.testproject002;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView txtScreen;
    private EditText inputHeaders;
    private EditText inputParams;
    private EditText inputUrl;
    private Spinner sMethod;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtScreen = (TextView) findViewById(R.id.txtScreen);
        inputHeaders = (EditText) findViewById(R.id.inputHeaders);
        inputParams = (EditText) findViewById(R.id.inputParams);
        inputUrl = (EditText) findViewById(R.id.inputURL);
        sMethod = (Spinner) findViewById(R.id.sMethod);

        txtScreen.setText("Now Loading...");

        txtScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(inputUrl.getText());
                if (!inputUrl.getText().equals(""))
                    sendRequest(inputUrl.getText().toString());

            }
        });

        //make a handler to listen response data
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                txtScreen.setText(bundle.getString("response"));

            }
        };

    }

    public void sendRequest(String url) {
        JSONData req = new JSONData();
        int method = Arrays.asList(getResources().getStringArray(R.array.method)).indexOf(sMethod.getSelectedItem().toString());
        req.setHeaders(inputHeaders.getText().toString().trim());
        req.setParams(inputParams.getText().toString().trim());

        //Use listener to get response data.
//        JSONData resp = new JSONData();
//        CustomRequest request = new CustomRequest(Request.Method.POST, url, req, new Response.Listener<JSONData>() {
//            @Override
//            public void onResponse(JSONData response) {
//
//            }
//        });

        //Use handler to get response data.
        CustomRequest request = new CustomRequest(method, url, req, handler);

        //Add request to request queue pool
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

}
