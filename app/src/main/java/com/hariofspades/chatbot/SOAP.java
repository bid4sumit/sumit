package com.hariofspades.chatbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by sumit.bx.kumar on 07-04-2017.
 */

public class SOAP {

    public String web1="<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:fmw=\"http://fmw.vodacom.com/\">\n" +
            "   <soap:Header/>\n" +
            "   <soap:Body>\n" +
            "      <fmw:loginFilter>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>value1234</arg0>\n" +
            "         <!--Optional:-->\n" +
            "         <arg1>value2234</arg1>\n" +
            "      </fmw:loginFilter>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";

    public String web2="<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:fmw=\"http://fmw.vodacom.com/\">\n" +
            "   <soap:Header/>\n" +
            "   <soap:Body>\n" +
            "      <fmw:firstInteractionWithBotMore>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>value111</arg0>\n" +
            "         <!--Optional:-->\n" +
            "         <arg1>value222</arg1>\n" +
            "      </fmw:firstInteractionWithBotMore>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";

    /*public String web3="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    \t<soap:Body>\n" +
            "        \t\t<ns1:process xmlns:ns1=\"http://xmlns.oracle.com/CUR/AndroidBOTInteraction/BPELProcess1\">\n" +
            "            \t\t\t<ns1:opCode>LastInteractionBOT</ns1:opCode>\n" +
            "            \t\t\t<ns1:input1>501</ns1:input1>\n" +
            "            \t\t\t<ns1:input2>yesterday,today</ns1:input2>\n" +
            "        </ns1:process>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>n";*/

    public  String web3="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    \t<soap:Body>\n" +
            "        \t\t<ns1:process xmlns:ns1=\"http://xmlns.oracle.com/CUR/AndroidBOTInteraction/BPELProcess1\">\n" +
            "            \t\t\t<ns1:opCode>LastInteractionBOT</ns1:opCode>\n" +
            "            \t\t\t<ns1:input1>value1234</ns1:input1>\n" +
            "            \t\t\t<ns1:input2>value2234</ns1:input2>\n" +
            "        </ns1:process>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>\n";


    public  String web4="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    \t<soap:Body>\n" +
            "        \t\t<ns1:process xmlns:ns1=\"http://xmlns.oracle.com/CUR/AndroidBOTInteraction/BPELProcess1\">\n" +
            "            \t\t\t<ns1:opCode>LastInteractionBOT</ns1:opCode>\n" +
            "            \t\t\t<ns1:input1>501</ns1:input1>\n" +
            "            \t\t\t<ns1:input2>yesterday,toda</ns1:input2>\n" +
            "        </ns1:process>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>\n";

    public String login_xml="";



    public String IB_Retrigger_Fire_IB(String xml) {

        Properties dbProp = null;
        String path = "";
        System.out.print(xml);

        try {
            //dbProp = PropertiesUtil.getDatabaseProperties();

            //path = dbProp.getProperty("IB.pathSoap");

            URL url = new URL("http://dfmw001zatcrh.vodacom.corp:8001/AndroidWebService-Project1-context-root/FmwSOAOSBOperationSoap12HttpPort");

            System.out.println("URL is :" + url);

            URLConnection connection = url.openConnection();
            //connection.
            HttpURLConnection httpcon = (HttpURLConnection) connection;

            String req = xml;
            //System.out.println("*******!!!!!!!!!THe SAOPXML REQUEST IS HERE !!!!!******\n"+req+"\n\n\n!!!!!!!!!!!!!!!!\n\n");

            //System.out.println("Request is : " + req);
            byte[] b = req.getBytes();

            //httpcon.setRequestProperty("Content-Length", String.valueOf(500));
            httpcon.setRequestProperty("Content-Type", "application/soap+xml;charset=utf-8");
            httpcon.setRequestProperty("SOAPAction", "");
            httpcon.setRequestMethod("POST");
            httpcon.setDoOutput(true);
            httpcon.setDoInput(true);
            OutputStream out = httpcon.getOutputStream();
            out.write(b);
            out.close();
            int httpRespCode = httpcon.getResponseCode();
            System.out.println("httpRespCode is : " + httpRespCode);

            if (httpRespCode == 200) {
                System.out.println("Success");
                BufferedReader in = null;
                InputStreamReader isr = new InputStreamReader(httpcon.getInputStream());
                in = new BufferedReader(isr);
                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    line = line.replaceAll("&lt;", "<");
                    line = line.replaceAll("&gt;", ">");
                    line = line.replaceAll("<!\\[CDATA\\[", "");
                    line = line.replaceAll("]]>", "");
                    line = line.replaceAll("SA ID/Passport No", "Unique_ID");
                    sb.append(line);
                }

                System.out.println("*******!!!!!!!!!THe SAOPXML RESPONSE IS HERE !!!!!******\n"+sb.toString()+"\n\n\n!!!!!!!!!!!!!!!!\n\n");

                return sb.toString();

            } else if (httpRespCode == 202) {
                /*InputStreamReader isr = new InputStreamReader(httpcon.getErrorStream());
                 BufferedReader bff = new BufferedReader(isr);
                 System.out.println(bff);
                 System.out.println("Input Stream is :" + isr.toString());*/

                System.out.println("Successfully Fired Request!!!!!");

                return "Successfully Fired Request!";
            } else if (httpRespCode == 500) {
               /* InputStreamReader isr = new InputStreamReader(httpcon.getErrorStream());
                 BufferedReader bff = new BufferedReader(isr);
                 System.out.println(bff);
                 System.out.println("Input Stream is :" + isr.toString());
*/
                System.out.println("URL Unreachable..!!!!!");

                return "URL Unreachable";
            } else {
                /*InputStreamReader isr = new InputStreamReader(httpcon.getErrorStream());
                 BufferedReader bff = new BufferedReader(isr);
                 System.out.println(bff);*/
                System.out.println("Error Occurred in Connecting to Server...!!");

                return "Server not Reachable.";
            }
        } catch (Exception e) {
            System.out.println("Exception occured :" + e.toString());
            return "Exception Occurred Hello";
        }
    }

    public String getWeb1() {
        return web1;
    }

    public void setWeb1(String web1) {
        this.web1 = web1;
    }

    public String getWeb2() {
        return web2;
    }

    public void setWeb2(String web2) {
        this.web2 = web2;
    }

    public String getWeb3() {
        return web3;
    }

    public void setWeb3(String web3) {
        this.web3 = web3;
    }




    public String getWeb4() {
        return web4;
    }

    public void setWeb4(String web4) {
        this.web4 = web4;
    }


}
