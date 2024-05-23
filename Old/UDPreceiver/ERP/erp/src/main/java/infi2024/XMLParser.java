package infi2024;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class XMLParser {

    public static List<Order> parseXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new java.io.ByteArrayInputStream(xmlString.getBytes()));
        Element root = doc.getDocumentElement();
        NodeList ordersNodes = root.getElementsByTagName("Order");
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < ordersNodes.getLength(); i++) {
            Element orderElem = (Element) ordersNodes.item(i);
            Order order = new Order(
                orderElem.getAttribute("Number"),
                orderElem.getAttribute("WorkPiece"),
                Integer.parseInt(orderElem.getAttribute("Quantity")),
                Integer.parseInt(orderElem.getAttribute("DueDate")),
                Integer.parseInt(orderElem.getAttribute("LatePen")),
                Integer.parseInt(orderElem.getAttribute("EarlyPen")),
                root.getElementsByTagName("Client").item(0).getAttributes().getNamedItem("NameId").getNodeValue()
            );
            orders.add(order);
        }
        return orders;
    }
}
