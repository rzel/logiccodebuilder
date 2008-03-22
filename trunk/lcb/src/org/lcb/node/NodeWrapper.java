package org.lcb.node;

import java.util.Map;

import org.lcb.LCBProperties;
import org.lcb.templates.api.NodeTemplate;

/**
 * NodeWrapper is a helper to convert nodes into methods. The tree in memory of NodeData is build
 * with this wrappers, so any NodeData can be change into a NodeMethod at any moment
 */
public class NodeWrapper extends NodeData
{  
  private final NodeTemplate methodTemplate;
  private final NodeTemplate callTemplate;
  private final LCBProperties props;
  
  private NodeData codeNode;
  
  public NodeWrapper(LCBProperties props, NodeData codeNode, NodeTemplate methodTemplate, NodeTemplate callTemplate)
  {
    super(null);    
    this.props = props;
    this.methodTemplate = methodTemplate;
    this.callTemplate = callTemplate;
    
    this.codeNode = codeNode;
  }

  @Override
  public void setData(Map<String, String> data)
  {
    this.codeNode.setData(data);
  }

  @Override
  public void addFlow(String flow, NodeData nodeData)
  {
    codeNode.addFlow(flow, nodeData);
  }

  @Override
  public void addGraphSize(int amount)
  {
    codeNode.addGraphSize(amount);
  }
  
  @Override
  public int getGraphSize()
  {
    return codeNode.getGraphSize();
  }
  /**
   * Change the NodeData into a NodeMethod.
   */
  public void changeToMethod()
  {
    if (!codeNode.isMethod())
      codeNode = new NodeAutoMethod(props, codeNode, methodTemplate, callTemplate);
  }
  
  public NodeData getNode()
  {
    return codeNode;
  }

  public boolean isMethod()
  {
    return codeNode.isMethod();
  }

  @Override
  public String printCode(String section)
  {
    return codeNode.printCode(section);
  }
  
  @Override
  public String toString()
  {
    return codeNode.toString();
  }
}
