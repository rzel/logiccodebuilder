/*
 * Created on Nov 16, 2004
 *
 */
package org.lcb.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class XMLElement
{
  public static final String[] EMPTY_STR = new String[0];
  public static final XMLElement[] EMPTY = new XMLElement[0];

  private String name;
  private Map<String,String> attrs;
  private List<XMLElement> children;
  private List<String> attrsNames;
  private String value = null;

  public XMLElement(final String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
  
  public void copyAttr(XMLElement xmlElement)
  {
    final Set<String> keys = xmlElement.attrs.keySet();
    for (String key : keys)
      addAttr(key, xmlElement.getAttr(key));
  }

  public void addAttr(final String name, final String value)
  {
    if (attrs == null)
    {
      attrs = new HashMap<String,String>();
      attrsNames = new ArrayList<String>();
    }
    attrs.put(name, value);
    attrsNames.add(name);
  }

  public void addChild(XMLElement child)
  {
    assert child != null : "Child can't be null";
    if (children == null)
    {
      children = new ArrayList<XMLElement>();
    }

    children.add(child);
  }

  public String getAttr(final String name)
  {
    if (attrs == null)
    {
      return null;
    }
    return attrs.get(name);
  }

  public String getAttr(final String name, final String defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return value;
    }
    return defaultValue;
  }

  public int getAttr(final String name, final int defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return Integer.parseInt(value);
    }
    return defaultValue;
  }

  public long getAttr(final String name, final long defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return Long.parseLong(value);
    }
    return defaultValue;
  }

  public float getAttr(final String name, final float defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return Float.parseFloat(value);
    }
    return defaultValue;
  }

  public double getAttr(final String name, final double defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return Double.parseDouble(value);
    }
    return defaultValue;
  }


  @SuppressWarnings({ "static-access", "unchecked" })
  public <T> Class<T> getAttr(final String name, final Class<T> defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    try
    {
      return (Class<T>) XMLElement.class.forName(value);
    }
    catch(Exception ex)
    { throw new RuntimeException(ex);}
    return defaultValue;
  }

  public boolean getAttr(final String name, final boolean defaultValue)
  {
    final String value = getAttr(name);
    if (value != null)
    {
      return Boolean.valueOf(value).booleanValue();
    }
    return defaultValue;
  }

  public String[] getAttrsName()
  {
    if (attrs != null && attrs.size() > 0)
    {
      final String[] names = new String[attrsNames.size()];
      return attrsNames.toArray(names);
    }
    return EMPTY_STR;
  }

  public void removeChild(XMLElement child)
  {
    if (child != null)
      children.remove(child);
  }

  public int getChildrenSize()
  {
    return children.size();
  }

  public XMLElement getChild(int idx)
  {
    return children.get(idx);
  }

  public XMLElement[] getChildren()
  {
    if (children != null && children.size() > 0)
    {
      return (XMLElement[]) children.toArray(new XMLElement[children.size()]);
    }
    return EMPTY;
  }

  public XMLElement[] getChildren(final String name)
  {
    if (children != null && children.size() > 0)
    {
      final List<XMLElement> result = new ArrayList<XMLElement>();
      for (int i = 0; i < children.size(); i++)
      {
        final XMLElement e = (XMLElement) children.get(i);
        if (e.getName().equals(name))
        {
          result.add(e);
        }
      }
      if (result.size() > 0)
      {
        return result.toArray(new XMLElement[result.size()]);
      }
    }
    return EMPTY;
  }

  public XMLElement[] getChildren(String...attrValue)
  {
    if (children != null && children.size() > 0)
    {
      if (attrValue.length == 0)
        return getChildren();
      else
      {
        final List<XMLElement> result = new ArrayList<XMLElement>();
        result.addAll(children);
        
        for (int i = 0; i < attrValue.length; i+=2)
          filterNodes(result, attrValue[i], attrValue[i+1]);

        if (result.size() > 0)
          return result.toArray(new XMLElement[result.size()]);
      }
    }
    return EMPTY;
  }

  public XMLElement[] getChildren(final String attr, final String value)
  {
    if (children != null && children.size() > 0)
    {
      final List<XMLElement> result = new ArrayList<XMLElement>();
      for (int i = 0; i < children.size(); i++)
      {
        final XMLElement e = (XMLElement) children.get(i);
        final String val = e.getAttr(attr);
        if (val != null && val.equals(value))
        {
          result.add(e);
        }
      }
      if (result.size() > 0)
      {
        return result.toArray(new XMLElement[result.size()]);
      }
    }
    return EMPTY;
  }

  public XMLElement getChild(String attr, String value)
  {
    if (children != null && children.size() > 0)
    {
      for (int i = 0; i < children.size(); i++)
      {
        final XMLElement e = (XMLElement) children.get(i);
        final String val = e.getAttr(attr);
        if (val != null && val.equals(value))
          return e;
      }
    }
    return null;
  }
  
  private void filterNodes(List<XMLElement> elements, String attr, String value)
  {
    for (int i = elements.size()-1; i >= 0; i--)
    {
      final XMLElement e =  elements.get(i);
      final String val = e.getAttr(attr);
      if (val == null || !val.equals(value))
      {
        elements.remove(i);
      }
    }
  }

  public XMLElement getChild(String name)
  {
    if (children != null && children.size() > 0)
    {
      name = XMLUtils.getString(name);
      for (int i = 0; i < children.size(); i++)
      {
        final XMLElement e = (XMLElement) children.get(i);
        if (e.getName().equals(name))
          return e;
      }
    }
    return null;
  }

  /**
   * path is separete by /element/attr=val/...
   *                     /element/element/...
   *                     
   * element = name | attr=val
   * 
   *  attr=val val must be unquote 
   * @param path
   * @return the element or null
   */
  public XMLElement findChild(final String path)
  {
    return findChild(path.split("/"), 0);
  }
  /**
   * element = name | attr=vale 
   * 
   *  attr=val val must be unquote
   * @param path
   * @return the element or null
   */
  public XMLElement findChild(final String[] path, final int start)
  {
    XMLElement e = this;
    for (String p : path)
    {
      if (p.indexOf('=') > 0)
      {
        final String[] attrVal = p.split("=");
        e = e.getChild(attrVal[0], attrVal[1]);
      }
      else
        e = e.getChild(p);
      
      if (e == null)
        break;
    }
    
    return e;
  }

  public void removeAllChildren()
  {
    if (children != null)
      children.clear();
  }

  public String toString()
  {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    write(pw);
    pw.flush();
    return sw.toString();
  }

  public void dump()
  {
    XMLUtils.write(this, System.out);
  }

  public void write(final OutputStream out)
  {
    XMLUtils.write(this, out);
  }

  public void write(final PrintWriter out)
  {
    XMLUtils.write(out, this, "   ");
  }

  public void write(final File out)
  {
    XMLUtils.write(this, out);
  }
  /**
   * @inheritDoc XMLUtils.load(String, String)
   */
  public static XMLElement load(String location, String url)
  {
    return XMLUtils.load(location, url);
  }

  public static XMLElement load(File file)
  {
    try
    {
      return XMLUtils.load(file);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public static XMLElement load(InputStream input)
  {
    try
    {
      return XMLUtils.load(input);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public static String normalize(final String s)
  {
    return XMLUtils.normalize(s);
  }

  public static void applyIncludes(XMLElement root, XMLElement element, boolean recursive)
  {
    XMLUtils.applyIncludes(root, element, recursive);
  }

  public void addAll(XMLElement[] children2)
  {
    for (XMLElement element : children2)
      addChild(element);    
  }
}
