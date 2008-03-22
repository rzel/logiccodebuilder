package org.lcb.templates.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This can accepts any node and print it using the template error. 
 * 
 * 
 * @see MethodTemplate
 * @see NodeTemplate
 */
public class ErrorTemplate extends NodeTemplate
{
  public static final String NEWLINE = System.getProperty("line.separator");
  private static final String BODY = "body";
  
  public ErrorTemplate(Template template)
  {
    super(template);
  }
  
  @Override
  public CheckResult match(final String[] flows)
  {
    return CheckResult.ACCEPTED;
  }
  
  @Override
  public FlowDefinition getFlowDef(String flow)
  {
    return new FlowDefinition(flow);
  }/**/
  
  @Override
  public String print(Map<String, String> data)
  {
    final Map<String,String> replace = new HashMap<String, String>();
    replace.putAll(data);

    final StringBuilder body = new StringBuilder();
    for (Map.Entry<String, String>  entry : replace.entrySet())
    {
      if (!NodeTemplate.NODE.equals(entry.getKey()))
        body.append("  ").append(entry.getKey()).append(NEWLINE);
    }
    
    replace.put(BODY, body.toString());
    
    return template.print(data);
  }  
}
