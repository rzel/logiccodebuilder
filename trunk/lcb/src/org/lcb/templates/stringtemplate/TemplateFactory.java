package org.lcb.templates.stringtemplate;

import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.lcb.IOManager;
import org.lcb.templates.api.NodeTemplate;
import org.lcb.templates.api.Template;
import org.lcb.templates.api.Templates;
import org.lcb.xml.XMLElement;

public class TemplateFactory implements Templates
{
  private final StringTemplates stringTemplates = new StringTemplates();
  //@Override
  public void configure(IOManager ioManager, XMLElement configuration) throws Exception
  {
    stringTemplates.configure(ioManager, configuration);
  }

  public String apply(String template, Map<String, Object> data)
  {
    final StringTemplate strTemplate = new StringTemplate(template);
    for ( Entry<String, Object> entry: data.entrySet())
      strTemplate.setAttribute(entry.getKey(), entry.getValue());

    return strTemplate.toString();
  }
  
  //@Override
  public NodeTemplate[] getStatements()
  {
    final StringTemplate[] statements = stringTemplates.getStatements();
    final NodeTemplate[] nodeTemplates = new NodeTemplate[statements.length];
    for (int i = 0; i < nodeTemplates.length; i++)
      nodeTemplates[i] = new NodeTemplate(new Template(this, statements[i]));

    return nodeTemplates;
  }
  
  //@Override
  public NodeTemplate getError()
  {
    return new NodeTemplate(new Template(this, stringTemplates.getError()));
  }
  
  //@Override
  public NodeTemplate getMethod()
  {
    return new NodeTemplate(new Template(this, stringTemplates.getMethod()));
  }
  
  //@Override
  public NodeTemplate getCall()
  {
    return new NodeTemplate(new Template(this, stringTemplates.getCall()));
  }
  
  public String[] getFlowDefinitions(Object template)
  {
    return stringTemplates.getFlowDefinitions((StringTemplate) template);
  }

  public String print(Object template, Map<String, String> data)
  {
    return stringTemplates.print((StringTemplate) template, data);
  }
}
