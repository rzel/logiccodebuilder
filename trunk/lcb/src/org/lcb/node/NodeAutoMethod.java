package org.lcb.node;

import java.util.Map;

import org.lcb.LCBProperties;
import org.lcb.templates.api.NodeTemplate;

/**
 * NodeMethod contains NodeData that will be printed as a call or as a method declaration,
 * depending on the section that need to be print.
 * 
 * This kind of method is generated when multiples flows arrives to the same node.
 * The name of the method is generated.
 * 
 * @see Met
 */
public class NodeAutoMethod extends NodeMethod
{
  protected static final String ARGS_DEF = LCBProperties.ARGS_DEF;
  protected static final String ARGS_VAL = LCBProperties.ARGS_VAL;

  private static int count = 0;

  public NodeAutoMethod(LCBProperties props, NodeData nodeData, NodeTemplate methodTemplate, NodeTemplate callTemplate)
  {
    super(props, methodTemplate, callTemplate);    
    
    final String name = props.getMethodPrefix() + (count++);
    super.addFlow(methodTemplate.getFlows().get(0).getFlow(), nodeData);
    data.put(NodeTemplate.NODE, name);
  }

  public void setData(Map<String, String> data)
  {
    getBody().setData(data);
  }

  public void addFlow(String flow, NodeData nodeData)
  {
    getBody().addFlow(flow, nodeData);
  }
  
  @Override
  public void addGraphSize(int amount)
  {
    getBody().addGraphSize(amount);
  }
  
  @Override
  protected boolean printAsMethod()
  {    
    return super.printAsMethod() && props.useAutoMethods() && 
           getBody().getGraphSize() >= props.useMethodsGraphSize();
  }
}
