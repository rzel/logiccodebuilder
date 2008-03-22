/*
 * Created on Nov 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lcb.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XMLUtils
{
  public static final String W3C = "<?xml version = \"1.0\"?>";
  public static final String RESOURCES = "resources";
  public static final String FILE = "file";
  public static final String URL = "url";

  private static final Map<String, String> strings = new HashMap<String, String>();
  
  /**
   * @param location must be = resources | file | url
   * @param url location of the xml
   */
  public static XMLElement load(String location, String url)
  {
    try
    {
      XMLElement element = null;
      if (location.equals(RESOURCES))
      {
        final InputStream input = XMLUtils.class.getResourceAsStream(url);
        element = load(input);
        input.close();
      }
      else if (location.equals(FILE))
      {
        element = load(new File(url));
      }
      else if (location.equals(URL))
      {
        final InputStream input = new URL(url).openStream();
        element = load(input);
        input.close();
      }
      else
        throw new Exception("Invalid location " + location + " for url " + url);

      return element;
    }
    catch (Exception e)
    {
      throw new RuntimeException("URL: " + url, e);
    }
  }

  public static XMLElement load(File file) throws Exception
  {
    final InputStream in = new FileInputStream(file);
    XMLElement root = load(in);
    in.close();
    return root;
  }

  public static XMLElement load(InputStream input) throws Exception
  {
    final XMLParser parser = new XMLParser();
    return parser.parse(input);
  }
  
  public static final String getString(String string)
  {
    final String val = strings.get(string); 
    if (val != null)
      return val;
   
    strings.put(string, string);
    return string;
  }

  /**
   *@deprecated use write
   */
  public static void dump(XMLElement xml, OutputStream output)
  {
    final PrintWriter pw = new PrintWriter(output);
    write(pw, xml, "   ");
    pw.flush();
  }

  /**
   *@deprecated use write
   */
  public static void dump(XMLElement xml, PrintWriter pw, String offset)
  {
    write(pw, xml, "   ");
  }

  public static void write(XMLElement xml, OutputStream output)
  {
    final PrintWriter pw = new PrintWriter(output);
    pw.println(W3C);
    write(pw, xml, "   ");
    pw.flush();
  }

  public static void write(XMLElement xml, File file)
  {
    try
    {
      final PrintWriter pw = new PrintWriter(file);
      pw.println(W3C);
      write(pw, xml, "   ");
      pw.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static void write(PrintWriter pw, XMLElement xml, String offset)
  {
    pw.print(offset);
    pw.print("<");
    pw.print(xml.getName());
    final String[] attrs = xml.getAttrsName();
    
    for (int i = 0; i < attrs.length; i++)
    {
      pw.print(" ");
      pw.print(attrs[i]);
      pw.print("=\"");
      pw.print(normalize(xml.getAttr(attrs[i])));
      pw.print("\"");
    }
    final XMLElement[] children = xml.getChildren();
    if (children.length == 0 && (xml.getValue() == null))
    {
      /*pw.print(">");
      pw.print("</");
      pw.print(xml.getName());
      pw.println(">");*/
      pw.println(" />");
    }
    else if (children.length == 0)
    {
      pw.print(">");
      if (xml.getValue() != null)
        pw.print(normalize(xml.getValue()));

      pw.print("</");
      pw.print(xml.getName());
      pw.println(">");
    }
    else
    {
      pw.println(">");
      if (xml.getValue() != null)
        pw.print(xml.getValue());

      for (int i = 0; i < children.length; i++)
      {
        write(pw, children[i], "  " + offset);
      }

      pw.print(offset);
      pw.print("</");
      pw.print(xml.getName());
      pw.println(">");
    }
  }

  public static void main(String[] args)
  {
    try
    {
      XMLElement root = load(new File("F:\\data.xml"));
      dump(root, System.out);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static String normalize(final String s)
  {
    if (s == null)
    {
      return "";
    }

    final StringBuffer str = new StringBuffer();
    final int len = s.length();

    for (int i = 0; i < len; i++)
    {
      final char ch = s.charAt(i);

      switch (ch)
      {
        case '<':
        {
          str.append("&lt;");
          break;
        }
        case '>':
        {
          str.append("&gt;");
          break;
        }
        case '&':
        {
          str.append("&amp;");
          break;
        }
        case '"':
        {
          str.append("&quot;");
          break;
        }
        case '\n':
        {
          if (i > 0)
          {
            str.charAt(str.length() - 1);
            str.append("\n");
          }
          else
          {
            str.append("\n");
          }
          break;
        }
        default:
        {
          str.append(ch);
        }
      }
    }

    return (str.toString());
  }
  
  public static void applyIncludes(XMLElement root, XMLElement toApply, boolean recursive)
  {
    final String val = toApply.getAttr("include");
    if (val != null && val.length() > 0)
    {
      toApply.addAttr("include","");
      final XMLElement value = root.findChild(val);
      if (value == null)
        throw new RuntimeException("Element " + val + " not found in root " + root.getName());
      
      final String[] attrs = value.getAttrsName();
      for (String attr : attrs)
      {
        if (toApply.getAttr(attr) == null)
          toApply.addAttr(attr, value.getAttr(attr));
      }
    }
    
    final XMLElement[] includes = toApply.getChildren("include");
    for (XMLElement element : includes)
      throw new UnsupportedOperationException("Not implemented!!" + element.toString());

    if (recursive)
    {
      final XMLElement[] children = toApply.getChildren();
      for (XMLElement element : children)
        applyIncludes(root, element, recursive);
    }
  }
}




