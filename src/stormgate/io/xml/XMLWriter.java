package stormgate.io.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class XMLWriter {

  public XMLWriter() {
  }

  public static String getXML(XMLNode node) {
    return getXML(node, 0);
  }

  public static String getWhiteSpace(int depth) {
    String white = "";
    for(int i = 0 ; i < depth; i++){
      white += "  ";
    }
    return white;
  }

  public static String getXML(XMLNode node, int depth) {
    String xmlString = "";

    xmlString += getWhiteSpace(depth);

    String xmlName = node.name;
    xmlString += "<" + xmlName;

    HashMap<String, String> properties = node.getProperties();

    if (properties != null) {
      Iterator iterator = properties.keySet().iterator();
      String key, value;
      while (iterator.hasNext()) {
        key = (String) iterator.next();
        value = properties.get(key);
        value = Entities.XML.escape(value);
        
        xmlString += " " + key + "=\"" + value + "\"";
      }
    }
    xmlString += ">";

    String xmlValue = node.value;
    if (xmlValue != null && xmlValue.length() > 0) {
      xmlString += Entities.XML.escape(xmlValue);
    }

    ArrayList<XMLNode> children = node.getChildren();
    if (children != null && children.size() > 0) {
      xmlString += "\r\n";
      for (int i = 0; i < children.size(); i++) {
        xmlString += getXML(children.get(i), depth+1);
      }
      xmlString += getWhiteSpace(depth);
    }

    xmlString += "</" + xmlName + ">\r\n";

    return xmlString;
  }
}
