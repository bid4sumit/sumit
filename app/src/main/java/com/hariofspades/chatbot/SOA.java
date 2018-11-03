package com.hariofspades.chatbot;

import android.widget.TableLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by sumit.bx.kumar on 11-05-2017.
 */




/**
 * Created by sumit.bx.kumar on 07-04-2017.
 */

public class SOA {



    public String web1="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fmw=\"http://fmw.vodacom.com\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <fmw:loginFilter>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args0>value1234</fmw:args0>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args1>value2234</fmw:args1>\n" +
            "      </fmw:loginFilter>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public String web2="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fmw=\"http://fmw.vodacom.com\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <fmw:firstInteractionWithBotMore>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args0>value111</fmw:args0>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args1>value222</fmw:args1>\n" +
            "      </fmw:firstInteractionWithBotMore>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";



    public  String web3="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fmw=\"http://fmw.vodacom.com\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <fmw:voiceToTextKeywords/>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";


    public  String web4="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fmw=\"http://fmw.vodacom.com\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <fmw:LastInteractionWithBot>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args0>value1234</fmw:args0>\n" +
            "         <!--Optional:-->\n" +
            "         <fmw:args1>value2234</fmw:args1>\n" +
            "      </fmw:LastInteractionWithBot>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public String IB_Retrigger_Fire_IB(String xml) {

        Properties dbProp = null;
        String path = "";
        System.out.print(xml);

        try {
            //dbProp = PropertiesUtil.getDatabaseProperties();

            //path = dbProp.getProperty("IB.pathSoap");

            URL url = new URL("http://duim001zatcrh.vodacom.corp:8080/FMWWebService/services/FmwSOAOSBOperation.FmwSOAOSBOperationHttpSoap11Endpoint/");

            System.out.println("URL is :" + url);

            URLConnection connection = url.openConnection();
            //connection.
            HttpURLConnection httpcon = (HttpURLConnection) connection;

            String req = xml;
            //System.out.println("*******!!!!!!!!!THe SAOPXML REQUEST IS HERE !!!!!******\n"+req+"\n\n\n!!!!!!!!!!!!!!!!\n\n");

            //System.out.println("Request is : " + req);
            byte[] b = req.getBytes();

            //httpcon.setRequestProperty("Content-Length", String.valueOf(500));
            httpcon.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
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
