package org.lcb.templates.stringtemplate;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.lcb.IOManager;
import org.lcb.xml.XMLElement;

public class StringTemplates
{
  private static final String[] EMPTY = new String[0];
  //private StringTemplateGroup templates;
  
  private StringTemplateGroup stmtGroup;
  private StringTemplateGroup methodsGroup;
  
  public void configure(IOManager ioManager, XMLElement conf) throws Exception
  {
     //templates = new StringTemplateGroup("templates",ioManager.getTemplatesPath());
    
    stmtGroup = new StringTemplateGroup(new InputStreamReader(ioManager.findResource(conf.getAttr("statements"))));
    methodsGroup = new StringTemplateGroup(new InputStreamReader(ioManager.findResource(conf.getAttr("methods"))));
  }
  
  /*private StringTemplateGroup find(StringTemplateGroup templates, String name)
  {
    return new StringTemplateGroup(new StringReader(templates.lookupTemplate(name).toString()));
  }/**/
  
  public StringTemplate[] getStatements()
  {
    //final StringTemplateGroup stmtGroup = find(templates,"statements");
    
    final Object[] names = stmtGroup.getTemplateNames().toArray(); 
    final StringTemplate[] statements = new StringTemplate[names.length];
    for (int i = 0; i < statements.length; i++)
    {
      statements[i] = stmtGroup.getInstanceOf(names[i].toString());
    }
    return statements;
  }

  public StringTemplate getMethod()
  {
    //final StringTemplateGroup methodsGroup = find(templates,"methods");
    return methodsGroup.getInstanceOf("method");
  }

  public StringTemplate getCall()
  {    
    //final StringTemplateGroup methodsGroup = find(templates,"methods");
    return methodsGroup.getInstanceOf("call");
  }

  public StringTemplate getError()
  {    
    //final StringTemplateGroup stmtGroup = find(templates,"statements");
    return stmtGroup.getInstanceOf("error");
  }

  public String[] getFlowDefinitions(StringTemplate template)
  {
    if (template.getFormalArguments().size() > 0)
    {
      final List<String> flowDef = new ArrayList<String>();
      for (Object key : template.getFormalArguments().keySet())
      {
        if (!key.equals(TemplateFactory.NODE))
          flowDef.add(key.toString());
      }
      return flowDef.toArray(new String[flowDef.size()]);
    }
    return EMPTY;
  }

  public String print(StringTemplate template, Map<String, String> data)
  {
    final StringTemplate t = template.getInstanceOf();    
    for (Map.Entry<String, String>  entry : data.entrySet())
      setAttribute(t, entry.getKey(), entry.getValue());
        
    setAttribute(t, TemplateFactory.TEMPLATE_NAME, template.getName().toString());
   
    return t.toString();
  }
  
  private void setAttribute(StringTemplate t, String key, String value)
  {
    if (t.lookupFormalArgument(key) == null)
      t.defineFormalArgument(key);
    
    t.setAttribute(key, value);
  }

}
