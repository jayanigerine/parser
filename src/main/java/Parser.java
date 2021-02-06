import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Parser {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Enter the format of the input file\n1. for XML \n2. for JSON");
        Scanner s = new Scanner(System.in);
        int format = s.nextInt();
        switch (format) {
            case 1:

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new File("test.xml"));
                document.getDocumentElement().normalize();

                NodeList dateList = document.getElementsByTagName("date");

                for (int i = 0; i < dateList.getLength(); i++) {
                    int sum = 0;
                    int count = 0;
                    Node date = dateList.item(i);
                    if (date.getNodeType() == Node.ELEMENT_NODE) {
                        Element dateElement = (Element) date;
                        System.out.println("Node name:" + date.getNodeName() + "   value : " + dateElement.getAttribute("value"));
                        NodeList cityList = dateElement.getElementsByTagName("city");
                        for (int j = 0; j < cityList.getLength(); j++) {
                            Node city = cityList.item(j);
                            if (city.getNodeType() == Node.ELEMENT_NODE) {
                                Element cityElement = (Element) city;
                                sum += Double.parseDouble(cityElement.getElementsByTagName("temperature").item(0).getTextContent());
                            }
                        }
                        int averageTemp = sum / cityList.getLength();
                        System.out.println("Average temp for date: " + dateElement.getAttribute("value") + ": " + averageTemp);

                    }
                }
                break;

            case 2:

                JSONParser parser = new JSONParser();
                try {
                    FileReader reader = new FileReader("test.json");
                    Object obj = parser.parse(reader);
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray weathers = (JSONArray) jsonObject.get("weather");
                    Iterator itr = weathers.iterator();
                    HashMap<String, Long> averageTempMap = new HashMap<String, Long>();

                    while (itr.hasNext()) {
                        JSONObject element = (JSONObject) itr.next();
                        if (averageTempMap.containsKey((String) element.get("date"))) {
                            Long x = averageTempMap.get((String) element.get("date"));
                            averageTempMap.put((String) element.get("date"), ((x + (Long) element.get("temperature"))) / 2);
                        } else {
                            averageTempMap.put((String) element.get("date"), (Long) element.get("temperature"));
                        }

                    }
                    System.out.println("Average temperature is:" + averageTempMap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


}