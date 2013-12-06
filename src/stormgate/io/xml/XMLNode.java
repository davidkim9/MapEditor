package stormgate.io.xml;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import stormgate.common.Common;

public class XMLNode
{

	public String name;
	public String value;
	private XMLNode parent;
	private ArrayList<XMLNode> children;
	private HashMap<String, String> properties;

	public XMLNode()
	{
		name = "";
		parent = null;
		value = null;
		children = new ArrayList<XMLNode>();
	}

	public XMLNode(XMLNode parent, String name)
	{
		this.name = name;
		this.value = null;
		this.children = new ArrayList<XMLNode>();
		this.parent = parent;
	}

	public XMLNode(String name, String value)
	{
		this.name = name;
		this.value = value;
		this.children = new ArrayList<XMLNode>();
	}

	public XMLNode(XMLNode parent, String name, String value)
	{
		this.name = name;
		this.value = value;
		this.children = new ArrayList<XMLNode>();
		this.parent = parent;
	}

	public XMLNode(XMLNode parent, String name, ArrayList<XMLNode> children)
	{
		this.name = name;
		this.value = null;
		this.children = children;
		this.parent = parent;
	}

	public XMLNode(XMLNode parent, String name, ArrayList<XMLNode> children, HashMap<String, String> properties)
	{
		this.name = name;
		this.value = null;
		this.children = children;
		this.parent = parent;
		this.properties = properties;
	}

	public XMLNode(String name, ArrayList<XMLNode> children)
	{
		this.name = name;
		this.value = null;
		this.children = children;
	}

	public XMLNode getParent()
	{
		return parent;
	}

	public ArrayList<XMLNode> getChildren()
	{
		return children;
	}

	public boolean hasChildren()
	{
		if (children == null) {
			return false;
		}
		return children.size() > 0;
	}

	public void addChild(XMLNode node)
	{
		children.add(node);
	}

	public void removeChildren()
	{
		children = new ArrayList<XMLNode>();
	}

	public XMLNode getChild(String name)
	{
		for (int i = 0; i < children.size(); i++) {
			XMLNode child = children.get(i);
			if (child.name.equalsIgnoreCase(name)) {
				return child;
			}
		}

		return null;
	}

	public void setProperty(String name, String value)
	{
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(name, value);
	}

	public void setProperty(String name, int value)
	{
		setProperty(name, value + "");
	}

	void setProperty(String name, boolean value)
	{
		setProperty(name, value + "");
	}

	public String getProperty(String name)
	{
		if (properties != null) {
			return properties.get(name);
		}
		return "";
	}

	public int getInteger(String name)
	{
		return Common.parseInt(getProperty(name));
	}

	public double getDouble(String name)
	{
		return Common.parseDouble(getProperty(name));
	}

	public boolean getBoolean(String name)
	{
		String value = getProperty(name);
		if(value != null && !value.equals("false") && !value.equals("0") && value.length() > 0){
			return true;
		}
		return false;
	}

	public void setProperties(HashMap<String, String> properties)
	{
		this.properties = properties;
	}

	public HashMap<String, String> getProperties()
	{
		return properties;
	}

	@Override
	public String toString()
	{
		String returnString = "<" + name;

		if (properties != null) {
			Iterator iterator = properties.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				returnString += " " + key + "=\"" + properties.get(key) + "\"";
			}
		}

		returnString += ">\n";
		if (value != null && value.length() > 0) {
			returnString += value + "\n";
		}
		if (children != null && children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				returnString += children.get(i).toString();
			}
		}
		returnString += "</" + name + ">\n";
		return returnString;
	}
}
