package stormgate.io.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class XMLParser
{

	private XMLNode root;

	public XMLParser()
	{
		root = new XMLNode();
	}

	public boolean readFile(String path)
	{
		File file = new File(path);

		if (file.exists()) {
			return readFile(file);
		}
		return false;
	}

	public XMLNode getRoot()
	{
		return root;
	}

	public boolean readFile(File file)
	{

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getChildNodes();

			root = new XMLNode();
			root.name = "root";
			XMLNode child;

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				child = parseNode(nNode);
				if (child != null) {
					root.addChild(child);
				}
			}
			
			return true;

		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}

	private XMLNode parseNode(Node node)
	{
		XMLNode parent = new XMLNode();

		if (node != null) {
			NodeList nList = node.getChildNodes();

			parent.name = node.getNodeName();
			String value = node.getNodeValue();

			if (value != null) {
				parent.value = value.trim();
			}

			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					parent.setProperty(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
				}
			}

			XMLNode child;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				child = parseNode(nNode);
				if (child != null) {
					parent.addChild(child);
				}
			}
		}

		return parent;
	}
}
