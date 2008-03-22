package org.lcb.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler
{
  private XMLElement root;
  private List<XMLElement> xmlPath = new ArrayList<XMLElement>(); 
  
  public XMLElement parse(InputStream input)
  {
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(input, this);
      return root.getChild(0);
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
  }

  public void startDocument() throws SAXException
  {
    root = new XMLElement("root");
    xmlPath.add(root);
  }

  public void endDocument() throws SAXException
  {
  }

  public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException
  {
    String eName = lName;
    if (eName.equals(""))
      eName = qName; // namespaceAware = false

    final XMLElement element = new XMLElement(eName);
    xmlPath.get(0).addChild(element);
    xmlPath.add(0, element);
    
    if (attrs != null)
    {
      for (int i = 0; i < attrs.getLength(); i++)
      {
        String aName = attrs.getLocalName(i); // Attr name 
        if ("".equals(aName))
          aName = attrs.getQName(i);
        element.addAttr(aName, attrs.getValue(i));
      }
    }
  }

  public void endElement(String namespaceURI, String sName, String qName) throws SAXException
  {
    if (xmlPath.get(0).getValue() != null)
    {
      if (xmlPath.get(0).getValue().trim().length() == 0)
          xmlPath.get(0).setValue(" ");
      else
        xmlPath.get(0).setValue(xmlPath.get(0).getValue().trim());
    }
    
    xmlPath.remove(0);
  }

  public void characters(char buf[], int offset, int len) throws SAXException
  {
    final String value = new String(buf, offset, len);
    final String pathValue = xmlPath.get(0).getValue() == null?"":xmlPath.get(0).getValue(); 
    xmlPath.get(0).setValue(pathValue + value);
  }
  
  public static void main(String[] args)
  {
    final XMLParser parser = new XMLParser();
    try
    {
      System.out.println("XMLParser.main()xml: "+parser.parse(new FileInputStream("h:\\test.xml")).toString());
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  
}