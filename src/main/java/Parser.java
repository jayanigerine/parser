import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parser {
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
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
				float sum = 0;
				int count = 0;
				Node date = dateList.item(i);
				if (date.getNodeType() == Node.ELEMENT_NODE) {
					Element dateElement = (Element) date;
					NodeList cityList = dateElement.getElementsByTagName("city");
					for (int j = 0; j < cityList.getLength(); j++) {
						Node city = cityList.item(j);
						if (city.getNodeType() == Node.ELEMENT_NODE) {
							Element cityElement = (Element) city;
							sum += Float.parseFloat(cityElement.getElementsByTagName("temperature").item(0)
									.getTextContent());
						}
					}
					float averageTemp = sum / cityList.getLength();
					System.out.println(dateElement.getAttribute("value") + "'s average temperature : " + averageTemp);

				}
			}
			break;

		case 2:

			JSONParser parser = new JSONParser();

			FileReader reader = new FileReader("test.json");
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray weathers = (JSONArray) jsonObject.get("weather");
			Iterator itr = weathers.iterator();
			HashMap<String, ArrayList<Float>> tempMap = new HashMap<String, ArrayList<Float>>();

			while (itr.hasNext()) {
				JSONObject element = (JSONObject) itr.next();
				if (tempMap.containsKey((String) element.get("date"))) {
					ArrayList<Float> list = tempMap.get((String) element.get("date"));
					Long currentTemp = (Long) element.get("temperature");
					list.add(currentTemp.floatValue());
					tempMap.put((String) element.get("date"), list);
				}
				else {
					Long currentTemp = (Long) element.get("temperature");
					ArrayList<Float> list = new ArrayList<Float>();
					list.add(currentTemp.floatValue());
					tempMap.put((String) element.get("date"), list);
				}
			}

			Iterator tempMapItr = tempMap.keySet().iterator();
			while (tempMapItr.hasNext()) {
				String date = (String) tempMapItr.next();
				ArrayList<Float> mesurements = tempMap.get(date);
				float sum = 0f;
				for (int i = 0; i < mesurements.size(); i++) {
					sum = sum + mesurements.get(i);
				}
				System.out.println(date + "'s average temperature : " + sum / mesurements.size());
			}
		}

	}
}
