package stormgate.io.ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IniFile {

    public String fileName;
    public String filePath;
    private ArrayList<Node> nodes;
    private HashMap<String, Node> indexNodes;
    public static String charSeparator = "=";
    public static String charNewLine = "\r\n";
    public static String charIgnore = " \t" + charNewLine;

    private boolean fastCharCompare(char c, String s) {
        for (int i = 0; i < s.length(); i++) {
            if (c == s.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    public IniFile() {
        nodes = new ArrayList<Node>();
        indexNodes = new HashMap<String, Node>();
        fileName = "";
        filePath = "";
    }

    public void write(String key, int value) {
        write(key, value + "");
    }

    public void write(String key, double value) {
        write(key, value + "");
    }

    public void write(String key, boolean value) {
        write(key, value + "");
    }

    public void write(String key, String value) {
        Node node = new Node();
        node.id = key;
        node.data = value;
        indexNodes.put(key, node);
        nodes.add(node);
    }

    public String getValue(String key) {
        Node select = indexNodes.get(key.toLowerCase());
        if (select != null) {
            return select.data;
        }
        return null;
    }

    public int getInt(String key) {
        String value = getValue(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public double getDouble(String key) {
        String value = getValue(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        String value = getValue(key);
        if (value != null) {
            try {
                return Boolean.parseBoolean(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public boolean readFile(String path) {
        File file = new File(path);
        return readFile(file);
    }

    public boolean readFile(File file) {
        FileReader istream;

        fileName = "";
        filePath = "";

        try {
            istream = new FileReader(file);
            fileName = file.getName();
            filePath = file.getAbsolutePath();
            nodes.clear();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file.getAbsolutePath() + " not found.");
            return false;
        }

        int c = 0;
        int fsize = 0;
        int inputState = 0; //0 = Index; 1 = Separator; 2 = Data
        boolean ignoreState = false; //false = before; true = after
        Node cNode = new Node(); //Current node

        try {
            while ((c = istream.read()) != -1) {
                fsize++;

                if (inputState == 0) {
                    if (ignoreState) {
                        if (!fastCharCompare((char) c, charIgnore)) {
                            if (fastCharCompare((char) c, charSeparator)) {
                                ignoreState = true;
                                inputState++;
                            } else {
                                cNode.id += (char) c;
                            }
                        } else {
                            ignoreState = false;
                            inputState++;
                        }
                    } else {
                        if (!fastCharCompare((char) c, charIgnore)) {
                            cNode.id += (char) c;
                            ignoreState = true;
                        }
                    }

                } else if (inputState == 1) {
                    if (ignoreState) {
                        if (!fastCharCompare((char) c, charIgnore)) {
                            cNode.data += (char) c;
                            ignoreState = true;
                            inputState++;
                            //System.out.println("IGNORE STATE! " + (char) c);
                        }
                    } else {
                        if (!fastCharCompare((char) c, charIgnore)) {
                            //cNode.id += (char) c;
                            if (fastCharCompare((char) c, charSeparator)) {
                                ignoreState = true;
                            }
                        }
                    }
                } else if (inputState == 2) {
                    if (ignoreState) {
                        if (fastCharCompare((char) c, charNewLine)) {
                            ignoreState = false;
                            inputState = 0;

                            nodes.add(cNode);
                            indexNodes.put(cNode.id.toLowerCase(), cNode);
                            //System.out.println("ADDED " + cNode.id + " = " + cNode.data);
                            cNode = new Node();
                        } else {
                            cNode.data += (char) c;
                        }
                    } else {
                        if (!fastCharCompare((char) c, charIgnore)) {
                            cNode.data += (char) c;
                            ignoreState = true;
                        }
                    }
                }
            }
            istream.close();
            if (inputState == 2 && !cNode.id.isEmpty() && !cNode.data.isEmpty()) {
                ignoreState = false;
                inputState = 0;

                nodes.add(cNode);
                indexNodes.put(cNode.id.toLowerCase(), cNode);
            }
        } catch (IOException e) {
            System.out.println("Failure to read the file.");
            return false;
        }
        if (fsize == 0) {
            return false;
        }

        return true;
    }

    public boolean saveFile(File file) {
        FileWriter ostream;

        try {
            ostream = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("Not allowed to write to file " + file.getAbsolutePath() + " .");
            return false;
        }

        //Write
        for (int i = 0; i < nodes.size(); i++) {
            try {
                ostream.write(nodes.get(i).id + " = " + nodes.get(i).data + "\r\n");
            } catch (IOException e) {
                System.out.println("Failed to write to file " + file.getAbsolutePath() + " .");
                return false;
            }
        }

        //Flush and close
        try {
            ostream.flush();
            ostream.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Node findIndex(String id) {
        for (int i = 0; i < nodes.size(); i++) {
            if (id.compareTo(nodes.get(i).id) == 0) {
                return nodes.get(i);
            }
        }

        return null;
    }
}
