package com.hariofspades.chatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private EditText mEmailView;
    private EditText mPasswordView;
    String username = "";
    String password = "";
    int count = 3;
    Button mEmailSignInButton;
    TextView tvv;
    SharedPreferences.Editor editor;
    SOAP soap;
    SOA soa;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        soap = new SOAP();
        soa = new SOA();
        final Intent i = new Intent(LoginActivity.this, MainActivity.class);
        mEmailView = (EditText) findViewById(R.id.username);

        tvv = (TextView) findViewById(R.id.triesRemaining);

        mPasswordView = (EditText) findViewById(R.id.password);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mEmailView.getText().toString();
                editor.putString("username",username);
                password = mPasswordView.getText().toString();
                editor.putString("password",password);
                editor.commit();

                new MyTask2().execute("temp");

                 /*   if (count != 0) {
                        count--;
                        tvv.setText(count + " tries remaining ");

                    } else {
                        mEmailSignInButton.setEnabled(false);
                        mEmailSignInButton.setText("Please try after some time");

                    }*/


            }
        });


    }




    private class MyTask2 extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array

            String myString = params[0];
            System.out.print("inside asy task 2");
            System.out.print("username" +username);
            System.out.print("password "+password);

            String temp = soa.getWeb1();
            temp = temp.replace("value1234", username);
            temp = temp.replace("value2234", password);
            System.out.print("xml is \n" +temp);

            String resp = soa.IB_Retrigger_Fire_IB(temp);

            System.out.print("response is  \n" +resp);

            // String temp = soap.getWeb3();;
            // resp = soap.IB_Retrigger_Fire_IB(temp);
            //  mimicOtherMessage(resp);

            resp=resp.replace(" xmlns:ns2=\"http://fmw.vodacom.com/\"","");


            return resp;
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String count = "";
            if(result.contains("success")){
            tvv.setText("success");}else{
                tvv.setText(result);
            }
            try {

                String msg1 = result;
                DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msg1.getBytes()));
                NodeList nList = parse.getElementsByTagName("ns:loginFilterResponse");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.println("\nCurrent Element :"
                            + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        count = eElement.getElementsByTagName("ns:return").item(0).getTextContent();                }
                    System.out.print("Parsed Response is  \n" +count);

                        }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(count.contains("success"))
            {   tvv.append(count);
                editor.putString("access",count);
                editor.commit();

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                tvv.append("parsed data is  "+count);
            }
        }
    }




}
