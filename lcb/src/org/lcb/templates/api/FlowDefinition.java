package org.lcb.templates.api;

import org.lcb.graphs.api.IGraphData;
import org.lcb.templates.api.NodeTemplate.CheckResult;

/**
 * The definition of the flow is the string from the template.
 *  
 * Flow definition can be "n" in this case it will accept any
 * match as a {@link CheckResult#ACCEPTED}.
 * 
 *
 * @see NodeTemplate
 */
public class FlowDefinition
{ 
  /**
   * Accepts any flow
   */
  public static final String ACCEPT_ANY = "n";
  
  private final String flow;

  public FlowDefinition(String flow)
  {
    this.flow = flow;
  }

  /**
   * 
   * @param flowData compare flow data from {@link IGraphData#getFlowData(Object, Object)} to the flow data
   *        in the template
   * @return {@link CheckResult#ACCEPTED} if the flow from template is ACCEPT_ANY
   *         {@link CheckResult#EXACT} if the strings are equal
   *         {@link CheckResult#FAIL} otherwise
   */
  public CheckResult match(String flowData)
  {
    if (flow.equals(ACCEPT_ANY))
    {
      if (flowData == null || flowData.length() == 0)
        return CheckResult.ACCEPTED;
    }

    return flow.equalsIgnoreCase(flowData) ? CheckResult.FAILED : CheckResult.EXACT;
  }
  
  public String getFlow()
  {
    return flow;
  }
  
  @Override
  public String toString()
  {
    return flow;
  }
}


















