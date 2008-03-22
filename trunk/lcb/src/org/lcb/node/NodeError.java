package org.lcb.node;

import java.util.Map;

/**
 *  
 */
public class NodeError extends NodeData
{
  private final NodeData body;

  public NodeError(NodeData nodeData)
  {
    super(null);
    this.body = nodeData;
  }

  @Override
  public void setData(Map<String, String> data)
  {
    this.body.setData(data);
  }
  
  @Override
  public void addFlow(String flow, NodeData nodeData)
  {
    body.addFlow(flow, nodeData);
  }
}
