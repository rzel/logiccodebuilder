package org.lcb.templates.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;

/**
 * If the tool is using {@link StringTemplate} engine
 *  
 * Template example for the if statement.
 *      if(node,yes,no) ::= <<
 *      if(<node>)
 *      {
 *        <yes>
 *      }
 *      else
 *      {
 *        <no>
 *      }>>
 * The flows are yes and no.
 *
 * The tag 'node' is ignored as a flow, the rest of the 
 * {@link StringTemplate#getFormalArguments()} will be considered
 * flows.
 *  
 *
 * @see FlowDefinition
 * @see <a href="http://www.stringtemplate.org">
 */
public class NodeTemplate
{
  protected static final String TEMPLATE_NAME = "template";
  public static final String NODE = "node";
  
  public static enum CheckResult {EXACT,ACCEPTED,FAILED}
  
  protected final Template template;
  private List<FlowDefinition> flowDef = Collections.emptyList();

  public NodeTemplate(Template template)
  {
    this.template = template;
    loadFlows();
  }
  /**
   * Read {@link StringTemplate#getFormalArguments()} ignore 'node' the rest are flows
   */
  private void loadFlows()
  {
    final FlowDefinition[] flowDefs = template.getFlowDefinitions();
    if (flowDefs.length > 0)
    {
      flowDef = new ArrayList<FlowDefinition>();
      for (FlowDefinition flowId : flowDefs)
      {
        if (!flowId.getFlow().equals(NODE))
          flowDef.add(flowId);
      }
    }
  }
  /**
   * This will make a match between graph flows and the flow definition from template
   * 
   * @param flows to match
   * @return  {@link CheckResult#FAILED} if flows does not match the template
   *          {@link CheckResult#ACCEPTED} if one or more of the flows makes a partial match the template
   *          {@link CheckResult#EXACT} if all of the flows match the template
   */
  public CheckResult match(final String[] flows)
  {
    if (countFlows() != flows.length)
      return CheckResult.FAILED;

    if (countFlows() == 0)
      return CheckResult.EXACT;

    CheckResult result = CheckResult.EXACT;
    for (String flow : flows)
    {
      final FlowDefinition flowDef = getFlowDef(flow);
      if (flowDef == null)
        return CheckResult.FAILED;
      else if (flowDef.match(flow) == CheckResult.ACCEPTED)
        result = CheckResult.ACCEPTED;
    }
    return result;    
  }

  /**
   * 
   * @param flow
   * @return
   */
  public FlowDefinition getFlowDef(String flow)
  {
    for (FlowDefinition flowDef : getFlows())
    {
      if (flowDef.match(flow) != CheckResult.EXACT)
        return flowDef;
    }
    return null;
  }/**/

  protected int countFlows()
  {
    return flowDef.size();
  }

  public List<FlowDefinition> getFlows()
  {
    return flowDef;
  }

  public Template getTemplate()
  {
    return template;
  }

  public String print(Map<String, String> data)
  { 
    return template.print(data);
  }
  
  @Override
  public String toString()
  {    
    return template.toDebugString() + flowDef + " ::= " + getTemplate().toString();
  }
}
























