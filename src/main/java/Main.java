import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json);

    }

    public static void writeString(String writeFile) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(writeFile);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();

            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

              if (Node.ELEMENT_NODE == node.getNodeType()) {
                  Element element = (Element) node;
                  NodeList nList = element.getChildNodes();
                  Employee employee = new Employee();
                  for (int j = 0; j < nList.getLength(); j++) {
                      Node current = nList.item(j);
                      String atrName = current.getNodeName();
                      String atrValue = current.getTextContent();

                      if (atrName.equals("id")) {
                          employee.id = Long.parseLong(atrValue);
                      }
                      if (atrName.equals("firstName")) {
                          employee.firstName = atrValue;
                      }
                      if (atrName.equals("lastName")) {
                          employee.lastName = atrValue;
                      }
                      if (atrName.equals("country")) {
                          employee.country = atrValue;
                      }
                      if (atrName.equals("age")) {
                          employee.age = Integer.parseInt(atrValue);
                      }

                  }
                  list.add(employee);
              }
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
