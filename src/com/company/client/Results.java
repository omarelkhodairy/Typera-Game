package com.company.client;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.io.*;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Custommonkey library is used for xml creation and comparison
 * Jar files for the library are included in the repository
 * in case of error please add the library to project structure
 */

public class Results implements Runnable {
    private String path=null;
    private String fileName = null;
    private int scriptNum =0;
    private String script=null;
    private boolean isValid = false;


    private ArrayList<String> paths = new ArrayList<String>();



    public Results(String fileName, int scriptNum , String script) {
        path = fileName + ".xml";
        this.script = script;
        this.scriptNum = scriptNum;
        paths.add("text1.xml");
        paths.add("text2.xml");
        paths.add("text3.xml");
        paths.add("text4.xml");
        paths.add("text5.xml");
        System.out.println("Constructor");

    }


    public  List compareXML(Reader source, Reader target) throws
                SAXException, IOException{

            //creating Diff instance to compare two XML files
            Diff xmlDiff = new Diff(source, target);

            //for getting detailed differences between two xml files
            DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff);

            return detailXmlDiff.getAllDifferences();
        }

        public int printDifferences(List differences){
            int totalDifferences = differences.size();
            System.out.println("===============================");
            System.out.println("Total differences : " + totalDifferences);
            System.out.println("================================");

            for(Object difference : differences){
                System.out.println(difference);
            }
            return totalDifferences;
        }

    @Override
    public void run() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);


            Attr attr = doc.createAttribute("phrase");
            attr.setValue(script);
            rootElement.setAttributeNode(attr);



            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource dSource = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(dSource, result);
            System.out.println("XML Generated");

            /*
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
            Results results=new Results(path);
            results.run();

             */



            FileInputStream fis1 = null;
            FileInputStream fis2 = null;
            try {
                fis1 = new FileInputStream(paths.get(scriptNum));
                fis2 = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // using BufferedReader for improved performance
            BufferedReader source = new BufferedReader(new InputStreamReader(fis1));
            BufferedReader  target = new BufferedReader(new InputStreamReader(fis2));



            List differences = null;
            try {
                differences = compareXML(source, target);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int check = printDifferences(differences);
            if(check == 0){
                isValid = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isValid() {
        return isValid;
    }
        /*
        // reading two xml file to compare in Java program
        FileInputStream fis1 = null;
        try {
           // fis1 = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileInputStream fis2 = null;
        try {
            fis2 = new FileInputStream("C:/Users/Ahmed/IdeaProjects/Idea/src/com/company/server/texts/test.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // using BufferedReader for improved performance
        BufferedReader source = new BufferedReader(new InputStreamReader(fis1));
        BufferedReader  target = new BufferedReader(new InputStreamReader(fis2));

        //configuring XMLUnit to ignore white spaces


        //comparing two XML using XMLUnit in Java
        List differences = null;
        try {
            differences = compareXML(source, target);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //showing differences found in two xml files
        printDifferences(differences);

    }

         */
}


