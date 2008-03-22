package org.lcb.templates.api;

import java.util.Map;

public class Template
{
  private final Templates templates;
  private final Object template;
  
  public Template(Templates templates, Object template)
  {
    this.templates = templates;
    this.template = template;
  }
  
  public FlowDefinition[] getFlowDefinitions()
  {
    final String[] statements = templates.getFlowDefinitions(template);
    final FlowDefinition[] nodeTemplates = new FlowDefinition[statements.length];
    for (int i = 0; i < nodeTemplates.length; i++)
    {
      nodeTemplates[i] = new FlowDefinition(statements[i]);
    }
    return nodeTemplates;
  }
  
  public String print(Map<String, String> data)
  {
    return templates.print(template, data);
  }

  public String toDebugString()
  {
    return template.toString();
  }
}
