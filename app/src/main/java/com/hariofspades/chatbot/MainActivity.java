package com.hariofspades.chatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.hariofspades.chatbot.Adapter.ChatMessageAdapter;
import com.hariofspades.chatbot.Pojo.ChatMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    String abc = "";
    private ListView mListView;
    private static final int REQUEST_CODE = 1234;
    private Button mButtonSend;
    public Bot bot;
    private String msg, resp;
    public static Chat chat;
    private ChatMessageAdapter mAdapter;
    private Button speakButton;
    private EditText tv;
    private boolean web = false;
    private ArrayList<String> keyWords;
    private int keyWordCount = 0;
    private String keyWordsFound = "";
    private ArrayList<WebResponse> webServiceResponse;
    //TextView tv1;
    public int step = 1;
    public WebResponse selectedResponse;
    public String[] questions;
    public String answer = "empty";
    public int qstncount = 0;
    SOAPI soap;
    TextToSpeech t1;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;
    String lucky="off";
    SOAP soapn;


    //for navigation menu
    private ListView mDrawerList;
    private ArrayAdapter<String> dAdapter;
    String access= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soapn = new SOAP();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor1 = sharedpreferences.edit();
        //lucky=sharedpreferences.getString("lucky","off");
        access = sharedpreferences.getString("access","null");
     /*   if(access.equals(null))
        {
            editor.putString("lucky","on");
            editor.commit();
        }*/

        //for navigation menu
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

     /*   String[] items = new String[] {"Menu", "Profile", "Settings", "About Me", "Exit"};
        final List<String> menu_list = new ArrayList<String>(Arrays.asList(items));
        dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_list);
        mDrawerList.setAdapter(dAdapter);*/

        mListView = (ListView) findViewById(R.id.listView);
        //  mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        speakButton = (Button) findViewById(R.id.speakButton);
        mButtonSend = (Button) findViewById((R.id.sendbutton));
        tv = (EditText) findViewById(R.id.editText);
        keyWords = getKeyWords();
        soap = new SOAPI();
       // speakButton.setBackground(this.getResources().getDrawable(R.drawable.send));

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            //voiceButton.setEnabled(false);
            //  speakButton.setEnabled(false);
            //  speakButton.setText("Recognizer not present");
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setSelection(mAdapter.getCount() - 1);
                startVoiceRecognitionActivity();
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setSelection(mAdapter.getCount() - 1);
                msg = tv.getText().toString();
                voiceFn(msg);
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mListView.setSelection(mAdapter.getCount() - 1);
                return false;
            }
            });



        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String v = "";
                TextView tv7 = (TextView)view.findViewById(android.R.id.text1);
                System.out.print("Inside on item click listener");
                v = (String) parent.getItemAtPosition(position);
                System.out.print(v);

                if(v.equalsIgnoreCase("exit"))
                {   Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
                    finish();
                }else if(v.equalsIgnoreCase("logout")) {
                    Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent i =new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();

                }else if((tv7.getText().toString().equalsIgnoreCase("Feeling lucky on"))){

                    Toast.makeText(getApplicationContext(), "Feeling lucky off", Toast.LENGTH_SHORT).show();
                    //editor1.putString("lucky","off");
                    lucky="off";
                    tv7.setText("Feeling lucky off");

                }else if(tv7.getText().toString().equalsIgnoreCase("Feeling lucky off")) {
                    Toast.makeText(getApplicationContext(), "Feeling lucky on", Toast.LENGTH_SHORT).show();
                   // editor1.putString("lucky","on");
                    lucky="on";
                    tv7.setText("Feeling lucky on");

                }

            }
        });


        /* //checking SD card availablility
        boolean a = isSDCARDAvailable();
        //receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Hari")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Hari/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Hari/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        //get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Hari", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);

    }

    private void addDrawerItems() {

        String[] osArray = { "Menu", "Profile", "Settings", "About Me","Feeling lucky off", "Logout","Exit" };
        dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(dAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        inflater.inflate(R.menu.menu2, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

        //mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    //check SD card availability
    public static boolean isSDCARDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
    }

    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    //Request and response of user and the bot
    public static void mainFunction(String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        System.out.println("Human: " + request);
        System.out.println("Robot: " + response);
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        //intent.putExtra(RecognizerIntent.ACTION_WEB_SEARCH,false);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            //tv.setText(matches.get(0));

            msg = matches.get(0);

            msg = msg.replaceAll("see bell", "siebel");
            msg = msg.replaceAll("cibil", "siebel");
            msg = msg.replaceAll("civil", "siebel");
            msg = msg.replaceAll("audio", "order");
            msg = msg.replaceAll("auto", "order");
            msg = msg.replaceAll("powder", "order");
            msg = msg.replaceAll("folder", "order");
            msg = msg.replaceAll("ap and", "apn");
            msg = msg.replaceAll("add account", "order count");
            msg = msg.replaceAll("discount", "order count");
            msg = msg.replaceAll("one", "1");
            msg = msg.replaceAll("two", "2");
            msg = msg.replaceAll("three", "3");
            msg = msg.replaceAll("four", "4");
            msg = msg.replaceAll("five", "5");
            msg = msg.replaceAll("six", "6");
            msg = msg.replaceAll("seven", "7");
            msg = msg.replaceAll("eight", "8");
            msg = msg.replaceAll("nine", "9");
            msg = msg.replaceAll("at the rate", "@");
            msg = msg.replaceAll("hash", "#");
            msg = msg.replaceAll("dollar", "$");
            msg = msg.replaceAll("star", "*");
            msg = msg.replaceAll("opening bracket", "(");
            msg = msg.replaceAll("closing bracke", ")");
            msg = msg.replaceAll("plus", "+");
            msg = msg.replaceAll("minus", "-");

            voiceFn(msg);


        /*if(msg.toLowerCase().contains("order")||msg.toLowerCase().contains("count"))
            {
                resp=" Execute QUery here";
            }
            else {
                resp = chat.multisentenceRespond(msg);
            }
            if (TextUtils.isEmpty(msg)) {
                return;
            }

            sendMessage(msg);
            mimicOtherMessage(resp);
            mListView.setSelection(mAdapter.getCount() - 1);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ArrayList<String> getKeyWords() {

        //call WebService here to get list of keywords
        // temporarily hardcoded keywords

        ArrayList keyWords = new ArrayList<String>();

        /*keyWords.add("siebel activation order count");
        keyWords.add("siebel activation order");
        keyWords.add("siebel order status");
        keyWords.add("activation order");
        keyWords.add("ccs id for msisdn");
        keyWords.add("ocs id for msisdn");
        keyWords.add("order completed");
        keyWords.add("name of apn");
        keyWords.add("shipment status");
        keyWords.add("shipping status");
        keyWords.add("order status");
        keyWords.add("task count");
        keyWords.add("service type");
        keyWords.add("type of service");
        keyWords.add("order created");
        keyWords.add("order type");
        keyWords.add("apn name");
        keyWords.add("status");
        keyWords.add("count");
        keyWords.add("task");
        keyWords.add("ccs id");
        keyWords.add("ocs id");
        keyWords.add("msisdn");
        keyWords.add("service");
        keyWords.add("order");
        keyWords.add("iccid");
        keyWords.add("apn");
        keyWords.add("ocs");
        keyWords.add("ccs");*/
        keyWords.add("predictive");
        keyWords.add("activation");
        keyWords.add("completed");
        keyWords.add("shipping");
        keyWords.add("service");
        keyWords.add("created");
        keyWords.add("siebel");
        keyWords.add("msisdn");
        keyWords.add("status");
        keyWords.add("order");
        keyWords.add("iccid");
        keyWords.add("count");
        keyWords.add("task");
        keyWords.add("type");
        keyWords.add("name");
        keyWords.add("ocs");
        keyWords.add("ccs");
        keyWords.add("apn");
        keyWords.add("id");


      /*keyWords.add("siebel activation order count");
        keyWords.add("siebel activation order");
        keyWords.add("siebel order status");
        keyWords.add("activation order");
        keyWords.add("shipment status");
        keyWords.add("shipping status");
        keyWords.add("order status");
        keyWords.add("order created");
        keyWords.add("status");
        keyWords.add("order");
        */

        return keyWords;
    }

    public void voiceFn(String msg) {


        if (TextUtils.isEmpty(msg)) {
            return;
        }

        sendMessage(msg);
        System.out.print("\n\ninside fn just message\n\n");
        System.out.print(web);
        if (web == false) {

            for (int i = 0; i < keyWords.size(); i++) {
                if (msg.toLowerCase().contains(keyWords.get(i))) {
                    if(keyWordCount==0)
                    {
                        keyWordsFound = keyWords.get(i);
                    }else {
                        keyWordsFound = keyWordsFound + "," + keyWords.get(i);
                    }
                    System.out.println("\n\n\n" + keyWordsFound);
                    System.out.print("\n\n\n\n");

                    keyWordCount++;

                }
            }
           // sendMessage("Keywords found"+keyWordsFound);

            if (keyWordCount != 0) {
               // sendMessage("Keywords found"+keyWordsFound);
                // webServiceResponse = WebServiceResponseDemo(keyWordsFound, msg);

                //resp = "which of the below queries you want to perform \n ";
              /* mimicOtherMessage(resp);
                for (int i = 0; i < webServiceResponse.size(); i++) {
                    mimicOtherMessage(webServiceResponse.get(i).getName());
                }*/

                new MyTask().execute(msg);


                step = 2;
                web = true;

            } else {
                resp = chat.multisentenceRespond(msg);
                mimicOtherMessage(resp);
                t1.speak(resp, TextToSpeech.QUEUE_FLUSH, null);
                mListView.setSelection(mAdapter.getCount() - 1);
            }

          /*  for(int i=0;i<webServiceResponse.size();i++)
            {
                mimicOtherMessage(webServiceResponse.get(i).getName());
            }*/

            mListView.setSelection(mAdapter.getCount() - 1);
            msg = "";
            tv.setText("");
        } else {
            if (selectedResponse == null) {
                Toast.makeText(getApplicationContext(), "Please Select an action", Toast.LENGTH_LONG).show();
            } else {

                if (questions.length != 0 && qstncount != 0 && qstncount < questions.length - 1) {


                    if (answer.equals("empty")) {
                        step = 3;
                        answer = msg;

                    } else {
                        step = 3;
                        answer = answer + "," + msg;
                    }

                    mimicOtherMessage(questions[qstncount]);
                    mListView.setSelection(mAdapter.getCount() - 1);
                    qstncount++;

                } else {
                    if (answer.equals("empty")) {
                        step = 3;
                        answer = msg;
                    } else {
                        step = 3;
                        answer = answer + "," + msg;
                    }

                    mimicOtherMessage(" Crawling through data please wait ..! ");
                    t1.speak("Crawling through data please wait", TextToSpeech.QUEUE_FLUSH, null);
                    mListView.setSelection(mAdapter.getCount() - 1);
                    new MyTask2().execute("temp");
                  /*  qstncount = 0;
                    questions = null;
                    selectedResponse = null;
                    webServiceResponse = null;
                    keyWordCount = 0;
                    keyWordsFound = " ";
                    step = 1;
                    web = false;*/
                }
            }
            msg = "";
            tv.setText("");
        }

    }

    public ArrayList<WebResponse> WebServiceResponseDemo(String text) {
        ArrayList<WebResponse> tempResponse = new ArrayList<WebResponse>();

        try {

            String msg = text;
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msg.getBytes()));
//System.out.println(parse.getFirstChild().getTextContent());
            NodeList nList = parse.getElementsByTagName("return");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :"
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    WebResponse temp1 = new WebResponse();
                    temp1.setEnv(eElement.getElementsByTagName("env").item(0).getTextContent());
                    temp1.setId(eElement.getElementsByTagName("id").item(0).getTextContent());
                    temp1.setName(eElement.getElementsByTagName("nameOfInfoRequired").item(0).getTextContent());
                    temp1.setQuestions(eElement.getElementsByTagName("parametersOfInfoRequired").item(0).getTextContent());
                    if(access.contains(temp1.getEnv()) || access.contains("all"))
                    tempResponse.add(temp1);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListView.setSelection(mAdapter.getCount() - 1);
            qstncount = 0;
            questions = null;
            selectedResponse = null;
            webServiceResponse = null;
            keyWordCount = 0;
            keyWordsFound = " ";
            step = 1;
            web = false;
            answer = "empty";
            mimicOtherMessage("Sorry server is not responding");
            mimicOtherMessage("Please try after some time");
            mListView.setSelection(mAdapter.getCount() - 1);
        }

        return tempResponse;
    }

    public void selectQuery(String Query) {
        //  tv1 = (TextView) findViewById(R.id.Textv);
        if (selectedResponse == null) {
            for (int i = 0; i < webServiceResponse.size(); i++) {
                if (Query.equalsIgnoreCase(webServiceResponse.get(i).getName())) {
                    selectedResponse = webServiceResponse.get(i);
                    break;
                }
            }

            //tv1.setText("you have selected " + Query);
            if(lucky.contains("off"))
            mimicOtherMessage("You have selected \n " + Query);


            questions = TextUtils.split(selectedResponse.getQuestions(), ",");
            if (qstncount == 0) {
                mimicOtherMessage(questions[0]);
                mListView.setSelection(mAdapter.getCount() - 1);
                qstncount = 1;
            }
        }
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

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
            System.out.print("\ninside asy task1\n ");
            String temp = soapn.getWeb2();

            temp = temp.replace("value111", msg);
            temp = temp.replace("value222", keyWordsFound);
            resp = temp;
            abc = temp;
            resp = soapn.IB_Retrigger_Fire_IB(abc);
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
           // sendMessage("xml is \n " +abc);
            //mimicOtherMessage(result);
            System.out.print(result);
            webServiceResponse = WebServiceResponseDemo(result);
            System.out.print("\n " + abc + "\n");
            if (webServiceResponse.size() > 0) {
                step = 2;
                web = true;
                keyWordCount = 0;
                keyWordsFound = "";
            }
            if (webServiceResponse.size() > 0&& lucky.contains("off")){
                mimicOtherMessage("Which of the following action do you want to perform ? ");
                t1.speak("Which of the following action do you want to perform ? ", TextToSpeech.QUEUE_FLUSH, null);
                for (int i = 0; i < webServiceResponse.size(); i++) {
                    mimicOtherMessage(webServiceResponse.get(i).getName());

                }
            }else if(webServiceResponse.size() > 0&& lucky.contains("on")){
                mimicOtherMessage("Executing the below action ");
                t1.speak("Executing the below action ", TextToSpeech.QUEUE_FLUSH, null);

                    mimicOtherMessage(webServiceResponse.get(0).getName());
                    selectQuery(webServiceResponse.get(0).getName());

                }else if(webServiceResponse.size() == 0)
                {
                mimicOtherMessage("You Don't have access  ");
                t1.speak("You Don't have access  ", TextToSpeech.QUEUE_FLUSH, null);
                qstncount = 0;
                questions = null;
                selectedResponse = null;
                webServiceResponse = null;
                keyWordCount = 0;
                keyWordsFound = " ";
                step = 1;
                web = false;
                answer = "empty";
                }


            mListView.setSelection(mAdapter.getCount() - 1);
        }
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
            System.out.print("inside asy task 2" + myString);

            String temp = soap.getWeb3();
            temp = temp.replace("value1234", selectedResponse.getId());
            temp = temp.replace("value2234", answer);

            resp = soap.IB_Retrigger_Fire_IB(temp);

            // String temp = soap.getWeb3();;
            // resp = soap.IB_Retrigger_Fire_IB(temp);
            //  mimicOtherMessage(resp);

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

            try {
                String count = "";
                String msg1 = result;
                DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msg1.getBytes()));
                NodeList nList = parse.getElementsByTagName("client:Output");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.println("\nCurrent Element :"
                            + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        count = eElement.getElementsByTagName("client:output3").item(0).getTextContent();
                        if(count==null||count.equalsIgnoreCase("")||count.equalsIgnoreCase(" "))
                        {
                            mimicOtherMessage(" Sorry ! \n Cannot find anything  ");
                        }else {
                            mimicOtherMessage(count);
                            t1.speak(count, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        mListView.setSelection(mAdapter.getCount() - 1);
                    }

                    mListView.setSelection(mAdapter.getCount() - 1);
                    qstncount = 0;
                    questions = null;
                    selectedResponse = null;
                    webServiceResponse = null;
                    keyWordCount = 0;
                    keyWordsFound = " ";
                    step = 1;
                    web = false;
                    answer = "empty";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


