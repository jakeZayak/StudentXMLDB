package Classes;

import XMLgui.NewJFrame;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML {

    public static void main(String[] args) {
        NewJFrame frame = new NewJFrame();
        frame.setDefaultCloseOperation(NewJFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //public static void XMLdelete(Document doc, String guiid) {
        
        
    public static void XMLdelete(Document doc, String guiid) {
        NodeList nlStudentList = doc.getElementsByTagName("student");
        for (int i = 0; i < nlStudentList.getLength(); i++) {
            Node nStudent = nlStudentList.item(i);
            if (nStudent.getNodeType() == Node.ELEMENT_NODE) {
                Element eStudent = (Element) nStudent;
                NodeList attrList = eStudent.getChildNodes();
                for (int j = 0; j < attrList.getLength(); j++) {
                    Node n = attrList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element eAttr = (Element) n;
                        if (eAttr.getTagName().equals("id") && eAttr.getTextContent().equals(guiid)) {
                            Element delete = (Element) doc.getElementsByTagName("student").item(i);
                            Node parent = delete.getParentNode();
                            parent.removeChild(delete);
                            break;
                        }
                    }
                }
            }
        }
        XMLoutput(doc);
    }
        //List<Student> studentList = new ArrayList<>();
//
//        NodeList nlStudentList = doc.getElementsByTagName("student");
//
//        for (int i = 0; i < nlStudentList.getLength(); i++) {
//            Node nStudent = nlStudentList.item(i);
//
//            if (nStudent.getNodeType() == Node.ELEMENT_NODE) {
//                Element eStudent = (Element) nStudent;
//
//                // gets the list of nodes inside of a node
//                NodeList attrList = eStudent.getChildNodes();
//
//                for (int j = 0; j < attrList.getLength(); j++) {
//                    Node n = attrList.item(j);
//
//                    if (n.getNodeType() == Node.ELEMENT_NODE) {
//                        Element eAttr = (Element) n;
//
//                        if (eAttr.getTagName().equals("id") && eAttr.getTextContent().equals(guiid)) {
//                            //casting node to the element type and assigning it to delete
//                            Element delete = (Element) doc.getElementsByTagName("student").item(i);
//                            //identifies the parent of the node
//                            Node parent = delete.getParentNode();
//                            parent.removeChild(delete);
//                            break;
//                        }
//                    }
//                }
//            }
//        }        
    

    public static void XMLoutput(Document doc)
    {
        DOMSource source = new DOMSource(doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = transformerFactory.newTransformer();

            StreamResult result = new StreamResult("E:\\Java\\Students.xml");
            
            try {
                transformer.transform(source, result);
            } catch (TransformerException ex) {
                Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Document XMLadd(Document doc, int id, String fName, String lName) {
        Student addStudent = new Student();
        addStudent.ID = id;     
        addStudent.fName = fName;
        addStudent.lName = lName;
        
            try {
                Element root = doc.getDocumentElement();

                Element newStudent = doc.createElement("student");

                //appending data to xml document (appending all of child elements and then to root)
                Element ID = doc.createElement("id");
                ID.appendChild(doc.createTextNode(Integer.toString(addStudent.ID)));
                newStudent.appendChild(ID);

                Element fname = doc.createElement("firstName");
                fname.appendChild(doc.createTextNode(addStudent.fName));
                newStudent.appendChild(fname);

                Element lname = doc.createElement("lastName");
                lname.appendChild(doc.createTextNode(addStudent.lName));
                newStudent.appendChild(lname);

                root.appendChild(newStudent);

                // doc is nothing but raw data, we are passing it to source.  passing result to transformer to then stream to students.xml              
            } catch (Exception e) 
            {
                Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, e);           
            }
            return doc;
    }

    public static List<Student> XMLpopulate(Document doc) {
        List<Student> studentList = new ArrayList<>();

        NodeList nlStudentList = doc.getElementsByTagName("student");
        //grabbing all of the students
        for (int i = 0; i < nlStudentList.getLength(); i++) {
            Student objStudent = new Student();
            Node nStudent = nlStudentList.item(i);
            //checking all the nodes in the nodelist.  data validation
            if (nStudent.getNodeType() == Node.ELEMENT_NODE) {
                Element eStudent = (Element) nStudent;

                //int iStudent = (int) dStudent;  this is where we would want to convert a double to an int
                NodeList attrList = eStudent.getChildNodes();

                //creating another nodelist to store unique identifiers
                for (int j = 0; j < attrList.getLength(); j++) {
                    Node n = attrList.item(j);
                    // id, firstname, lastname are element nodes.  the actual values are text nodes
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        //casting to element so we can use element specific methods/functions.  we want an element node so we can get the text content
                        Element eAttr = (Element) n;

                        if (eAttr.getTagName().equals("id")) {
                            objStudent.ID = parseInt(eAttr.getTextContent());
                        } else if (eAttr.getTagName().equals("firstName")) {
                            objStudent.fName = eAttr.getTextContent();
                        } else if (eAttr.getTagName().equals("lastName")) {
                            objStudent.lName = eAttr.getTextContent();
                        }
                    }

                }

            }
            studentList.add(objStudent);
        }
        return (studentList);
    }

    public static Document XMLbuilder(String FilePath) 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            
            try {
                doc = builder.parse(FilePath);

            } catch (SAXException ex) {
                Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }

    public static List<Student> XMLsearch(String criteria, int guiid, String guifname, String guilname, Document doc) {
        List<Student> studentList = XMLpopulate(doc);
        List<Student> resultList = new ArrayList<>();

        int searchID = guiid;
        String searchFName = guifname;
        String searchLName = guilname;

        searchFName = searchFName.toLowerCase();
        searchLName = searchLName.toLowerCase();

        if (criteria == "id") {
            for (Student tempStudent : studentList) {
                if (tempStudent.ID == searchID) {
                    resultList.add(tempStudent);
                }
            }
        } else {
            for (Student tempStudent : studentList) {
                if (searchFName.isEmpty() && searchLName.isEmpty()) {
                    resultList.add(tempStudent);
                } else if (tempStudent.fName.toLowerCase().startsWith(searchFName) && tempStudent.lName.toLowerCase().startsWith(searchLName)) {
                    resultList.add(tempStudent);
                } else if (searchFName.isEmpty() && tempStudent.lName.toLowerCase().startsWith(searchLName)) {
                    resultList.add(tempStudent);
                } else if (tempStudent.fName.toLowerCase().startsWith(searchFName) && searchLName.isEmpty()) {
                    resultList.add(tempStudent);
                } else if (tempStudent.fName.toLowerCase().startsWith(searchFName) && tempStudent.lName.toLowerCase().startsWith(searchLName)) {
                    resultList.add(tempStudent);
                } else {
                    continue;
                }
            }

        }
        return (resultList);
    }

//    public static Document XMLsearch(String criteria, Document doc, String fname, String lname, int id) {
//        List<Student> studentList = XMLpopulate(doc);
//        for (Student tempStudent : studentList) {
//            if (fname.isEmpty() && lname.isEmpty()) {
//
//                System.out.println("ID: " + tempStudent.ID);
//                System.out.println("First Name: " + tempStudent.fName);
//                System.out.println("Last Name: " + tempStudent.lName);
//                System.out.println("empty FN && empty LN");
//
//            } else if (tempStudent.fName.toLowerCase().equals(fname) && tempStudent.lName.toLowerCase().equals(lname)) {
//                System.out.println("ID: " + tempStudent.ID);
//                System.out.println("First Name: " + tempStudent.fName);
//                System.out.println("Last Name: " + tempStudent.lName);
//                System.out.println("FN && LN");
//
//            } else if (fname.isEmpty() && tempStudent.lName.toLowerCase().startsWith(lname)) {
//
//                System.out.println("ID: " + tempStudent.ID);
//                System.out.println("First Name: " + tempStudent.fName);
//                System.out.println("Last Name: " + tempStudent.lName);
//                System.out.println("empty FN && starts with LN");
//
//            } else if (tempStudent.fName.toLowerCase().startsWith(fname) && lname.isEmpty()) {
//
//                System.out.println("ID: " + tempStudent.ID);
//                System.out.println("First Name: " + tempStudent.fName);
//                System.out.println("Last Name: " + tempStudent.lName);
//                System.out.println("starts with FN && empty LN");
//
//            } else if (tempStudent.fName.toLowerCase().startsWith(fname) && tempStudent.lName.toLowerCase().startsWith(lname)) {
//
//                System.out.println("ID: " + tempStudent.ID);
//                System.out.println("First Name: " + tempStudent.fName);
//                System.out.println("Last Name: " + tempStudent.lName);
//                System.out.println("starts with FN && starts with LN");
//
//            } else {
//                continue;
//            }
//
//        }
//        return doc;
//    }
    
//    public static void XMLsearch(String criteria, int id,String fname,String lname,Document doc) {
//        List<Student> studentList = XMLpopulate(doc);
//        
//        int searchID = 1;
//
//        fname = fname.toLowerCase();
//        lname = lname.toLowerCase();
//
//        if (radioBtn == true) {
//
//            for (Student tempStudent : studentList) {
//                if (tempStudent.ID == searchID) {
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("FN & LN");
//
//                } else {
//                    break;
//                }
//
//            }
//
//        } else {
//
//            for (Student tempStudent : studentList) {
//                if (fname.isEmpty() && lname.isEmpty()) {
//
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("empty FN && empty LN");
//
//                } else if (tempStudent.fName.toLowerCase().equals(searchFName) && tempStudent.lName.toLowerCase().equals(searchLName)) {
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("FN && LN");
//
//                } else if (searchFName.isEmpty() && tempStudent.lName.toLowerCase().startsWith(searchLName)) {
//
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("empty FN && starts with LN");
//
//                } else if (tempStudent.fName.toLowerCase().startsWith(searchFName) && searchLName.isEmpty()) {
//
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("starts with FN && empty LN");
//
//                } else if (tempStudent.fName.toLowerCase().startsWith(searchFName) && tempStudent.lName.toLowerCase().startsWith(searchLName)) {
//
//                    System.out.println("ID: " + tempStudent.ID);
//                    System.out.println("First Name: " + tempStudent.fName);
//                    System.out.println("Last Name: " + tempStudent.lName);
//                    System.out.println("starts with FN && starts with LN");
//
//                } else {
//                    continue;
//                }
//            }
//
//        }
//
//    }
}