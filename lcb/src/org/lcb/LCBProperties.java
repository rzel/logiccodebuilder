package org.lcb;

import org.lcb.xml.XMLElement;

public class LCBProperties
{  
  public static final String ARGS_DEF = "argsdef";
  public static final String ARGS_VAL = "argsval";
  private static final String METHOD_PREFIX     = "method-prefix";
  private static final String USE_METHODS       = "use-methods";
  private static final String USE_AUTO_METHODS  = "use-auto-methods";  
  private static final String USE_METHODS_GRAPH_SIZE = "use-methods-graph-size";
  
  private static final String DEFAULT_PROPERTIES = "properties.xml";
  private final XMLElement properties;
  
  public LCBProperties()
  {
    properties = XMLElement.load(LogicCodeBuilder.class.getResourceAsStream(DEFAULT_PROPERTIES));
  }
  
  public void addProperty(String name, String value)
  {
    properties.addAttr(name, value);
  }
  
  public void addAdditionalProperties(XMLElement properties)
  {
    this.properties.copyAttr(properties);
  }
  
  public String getProperty(String name)
  {
    return properties.getAttr(name);
  }

  public String getProperty(String name, String defaultValue)
  {
    return properties.getAttr(name, defaultValue);
  }
  
  public String checkProperty(String name, String mainValue)
  {
    if (mainValue == null)      
      return properties.getAttr(name);
    else
      return mainValue;
  }
  
  public String getMethodPrefix()
  {
    return getProperty(METHOD_PREFIX);
  }
  
  public boolean useMethods()
  {
    return properties.getAttr(USE_METHODS, true);
  }
  
  public int useMethodsGraphSize()
  {
    return properties.getAttr(USE_METHODS_GRAPH_SIZE, 2);
  }

  public boolean useAutoMethods()
  {
    return properties.getAttr(USE_AUTO_METHODS, true);
  }

  public String getArgsDef()
  {
    return properties.getAttr(ARGS_DEF, "");
  }

  public String getArgsVal()
  {
    return properties.getAttr(ARGS_VAL, "");
  }
}  

