/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tccthainan2;

import tccthainan.*;
import guarana.framework.message.Message;
import guarana.toolkit.engine.Scheduler;
import guarana.util.xml.XMLHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.w3c.dom.Document;

//import tcc.integration.solution.TccIntegrationSolution;

/**
 *
 * @author Edinaldo
 */
public class StartApp2 {

    public static String STATS_FOLDER;
      public static String ENTRY_XML = "./entry.xml";
    public static String GUARANA_CONF = "./guarana-conf.xml";
    //private Message<Document> msgList;
    private ArrayList<Message<Document>> msgList;

    String msg = null;

    public static void main(String[] args) throws InterruptedException {

        new StartApp2().start();
        System.out.println("Acabou");
    }

    public void start() throws InterruptedException {
//try {
        int i = 0;
        String summaryFile = STATS_FOLDER + "execution-summary.txt";
        // Document doc = XMLHandler.readXmlFile(ENTRY_XML);
        msg = ">>> Building messages... ";
        System.out.println(msg);

        this.msgList = buildMessages(2);
        
        //Document doc =  XMLHandler.readXmlFile(GUARANA_CONF);
        //XMLHandler.writeXmlFile( doc, GUARANA_CONF );
        Scheduler exec = new Scheduler("Scheduler");
        Integration2 prc = new Integration2();

        exec.registerProcess(prc);
        msg = "Starting workers... ";
        System.out.println(msg);
        exec.start();
        for (Message<Document> m : msgList) {
            prc.communicatorEntry.pushRead(m);
            Thread.sleep(1000);
           // XMLHandler.writeXmlFile(m.getBody(), "test.xml");
            i++;
            System.out.println("Construiu "+i+" mensagens");
        }

        //  prc.communicatorEntry.pushRead(msgList);
        //System.out.println(    msgList.getBody().getXmlEncoding());
        exec.stop();
        Thread.sleep(60 * 1000);
    }

//    private Message<Document> buildMessages(long messages) throws XPathExpressionException {
//        long start = System.currentTimeMillis();
//
//        Document docX1 = XMLHandler.readXmlFile("./entry.xml");
//     
//       
//     
//        Message<Document> m = new Message<Document>();
//        m.setBody(docX1);
//      
//        
//        long end = System.currentTimeMillis();
//        DecimalFormat df = new DecimalFormat("####.##");
//        System.out.println(">>> Time to build the message: " + df.format(((end - start) / 1000f / 60f)) + " min");
//
//        return m;
//
//    }
    private ArrayList<Message<Document>> buildMessages(long messages) {
        ArrayList<Message<Document>> result = new ArrayList<Message<Document>>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < messages; i++) {
            Document msg1 = XMLHandler.readXmlFile("./entry.xml");
            Message<Document> m = new Message<Document>();
            m.setBody(msg1);
            result.add(m);
        }
        long end = System.currentTimeMillis();

        DecimalFormat df = new DecimalFormat("####.##");
        System.out.println(">>> Time to build messages: " + df.format(((end - start) / 1000f / 60f)) + " min");

        return result;

    }
}
