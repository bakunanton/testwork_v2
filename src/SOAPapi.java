/**
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.*;

import java.io.Writer;
import java.io.*;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
/**
 *вфв
 * @author Admin
 */

public class  SOAPapi{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String urlLogin = "https://login.salesforce.com/services/Soap/c/37.0";
            String urlReq = "https://ap2.salesforce.com/services/Soap/c/37.0";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), urlLogin);
            // Process the SOAP Response
            String ses  = printSOAPResponse(soapResponse);
            if(ses.length() !=0 ) {
                System.out.println("\n");
                System.out.print("Logged");
                System.out.println("\n");
                SOAPMessage soapAllObjects = soapConnection.call(getAllDescribe(ses), urlReq);
                printSObject(soapAllObjects);
                soapConnection.close();
            }else{
                System.out.println("\n");
                System.out.print("Not logged");
                System.out.println("\n");
                soapConnection.close();
            }
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }


    }



    private static SOAPMessage createSOAPRequest() throws Exception{

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI ="urn:enterprise.soap.sforce.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", serverURI);

        /*example soap message
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:enterprise.soap.sforce.com">
            <soapenv:Body>
                <urn:login>
                    <urn:username>?</urn:username>
                    <urn:password>?</urn:password>
                </urn:login>
            </soapenv:Body>
        </soapenv:Envelope>
        */
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("login", "urn");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("username");
        soapBodyElem1.addTextNode("testwork@free.com");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("password");
        soapBodyElem2.addTextNode("123456testVxXFfdm9YS7Of6ShWC1GthIkT");
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI);

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);


        return soapMessage;

    }

    private static SOAPMessage getAllDescribe(String sessionId)throws Exception{
//          <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:enterprise.soap.sforce.com">
//         <soapenv:Header>
//          <urn:SessionHeader>
//         <urn:sessionId></urn:sessionId>
//           </urn:SessionHeader>
//       </soapenv:Header>
//        <soapenv:Body>
//      <urn:describeGlobal>
//          </urn:describeGlobal>
//      </soapenv:Body>
//         </soapenv:Envelope>
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI ="urn:enterprise.soap.sforce.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", serverURI);
        // SOAP header
        SOAPHeader header  = envelope.getHeader();
        SOAPElement sessionHeader = header.addChildElement("SessionHeader","urn");
        SOAPElement sesid = sessionHeader.addChildElement("sessionId","urn");
        sesid.addTextNode(sessionId);
        //SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement bodyElem = soapBody.addChildElement("describeGlobal","urn");
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI);

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);


        return soapMessage;





    }


    private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        Writer xml = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StreamResult result = new StreamResult(xml);
        transformer.transform(sourceContent, result);
        System.out.println("\n");
        System.out.println("\n");
        String xmlInput = xml.toString();
        System.out.print(xmlInput);
        String sessionId = "";

        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(xmlInput.getBytes("utf-8"))));
                System.out.println("\n");
                System.out.print("sessionId = true");
                sessionId = document.getElementsByTagName("sessionId").item(0).getTextContent();
            } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(sessionId);
        return sessionId;
    }


    private static String printSObject(SOAPMessage soapResponse) throws Exception {
        Writer xml = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StreamResult result = new StreamResult(xml);
        transformer.transform(sourceContent, result);
        String xmlInput = xml.toString();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(xmlInput.getBytes("utf-8"))));
            System.out.println("");
            for(int i = 0 ; i  <=  document.getElementsByTagName("sobjects").getLength() ; i++){
                System.out.println("name :" + document.getElementsByTagName("name").item(i).getTextContent());
                System.out.println("label Object :" + document.getElementsByTagName("label").item(i).getTextContent());
                System.out.println("custom Object :" + document.getElementsByTagName("custom").item(i).getTextContent());
                System.out.println("deletable Object :" + document.getElementsByTagName("deletable").item(i).getTextContent());
                System.out.println("updateable Object :" + document.getElementsByTagName("updateable").item(i).getTextContent());
                System.out.println("undeletable Object :" + document.getElementsByTagName("undeletable").item(i).getTextContent());
                System.out.println("queryable Object :" + document.getElementsByTagName("queryable").item(i).getTextContent());
                System.out.println("----------------------------------------");

            }

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
       return null;

    }



}


