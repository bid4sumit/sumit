package com.hariofspades.chatbot;

/**
 * Created by sumit.bx.kumar on 11-04-2017.
 */



        import java.io.ByteArrayInputStream;
        import java.io.File;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.DocumentBuilder;
        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;
        import org.w3c.dom.Node;
        import org.w3c.dom.Element;

public class XMLParse {
    public static void main(String[] args){

        try {

            String msg = "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">\n" +
                    "   <env:Header>\n" +
                    "      <wsa:MessageID>urn:0BDF39401EB211E79F0DE1383948657B</wsa:MessageID>\n" +
                    "      <wsa:ReplyTo>\n" +
                    "         <wsa:Address>http://www.w3.org/2005/08/addressing/anonymous</wsa:Address>\n" +
                    "         <wsa:ReferenceParameters>\n" +
                    "            <instra:tracking.compositeInstanceCreatedTime xmlns:instra=\"http://xmlns.oracle.com/sca/tracking/1.0\">2017-04-11T14:26:10.510+02:00</instra:tracking.compositeInstanceCreatedTime>\n" +
                    "         </wsa:ReferenceParameters>\n" +
                    "      </wsa:ReplyTo>\n" +
                    "      <wsa:FaultTo>\n" +
                    "         <wsa:Address>http://www.w3.org/2005/08/addressing/anonymous</wsa:Address>\n" +
                    "         <wsa:ReferenceParameters>\n" +
                    "            <instra:tracking.compositeInstanceCreatedTime xmlns:instra=\"http://xmlns.oracle.com/sca/tracking/1.0\">2017-04-11T14:26:10.510+02:00</instra:tracking.compositeInstanceCreatedTime>\n" +
                    "         </wsa:ReferenceParameters>\n" +
                    "      </wsa:FaultTo>\n" +
                    "   </env:Header>\n" +
                    "   <env:Body>\n" +
                    "      <processResponse xmlns:client=\"http://xmlns.oracle.com/CUR/AndroidBOTInteraction/BPELProcess1\" xmlns=\"http://xmlns.oracle.com/CUR/AndroidBOTInteraction/BPELProcess1\">\n" +
                    "         <client:Output>\n" +
                    "            <client:output1>74</client:output1>\n" +
                    "            <client:output2>Activation orders for specified date range</client:output2>\n" +
                    "            <client:output3>null,null,</client:output3>\n" +
                    "         </client:Output>\n" +
                    "         <client:Output>\n" +
                    "            <client:output1>74</client:output1>\n" +
                    "            <client:output2> orders for specified date range</client:output2>\n" +
                    "            <client:output3>null,null,</client:output3>\n" +
                    "         </client:Output>\n" +
                    "      </processResponse>\n" +
                    "   </env:Body>\n" +
                    "</env:Envelope>";
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msg.getBytes()));
//System.out.println(parse.getFirstChild().getTextContent());
            NodeList nList = parse.getElementsByTagName("client:Output");
            for (int temp = 0; temp < nList.getLength(); temp++){
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :"
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("First Name : "
                            + eElement
                            .getElementsByTagName("client:output2")
                            .item(0)
                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}