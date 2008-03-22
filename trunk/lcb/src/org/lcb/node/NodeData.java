package org.lcb.node;

import java.util.HashMap;
import java.util.Map;

import org.lcb.builder.GraphDataWalker;
import org.lcb.templates.api.FlowDefinition;
import org.lcb.templates.api.NodeTemplate;

/**
 * NodeData is the data container from the graph. This class will contains the attributes
 * from the graph nodes, the flows to other nodes and the template associated with it. 
 *
 */
public class NodeData
{
  public static final String SECTION_MAIN_CODE  = "MAIN-CODE"; 
  public static final String SECTION_METHODS    = "METHODS";
  
  /**
   * This template will be use to print the code, the definition was associated during the walk.
   * 
   * @see GraphDataWalker
   */
  private final NodeTemplate template;
  /**
   * Flow to others nodes, this node is the start of the flows.
   */  
  protected final Map<FlowDefinition,NodeData> flows = new HashMap<FlowDefinition,NodeData>();
  /**
   * Attributes from the graph nodes
   */
  protected final Map<String, String> data = new HashMap<String, String>();
  
  /**
   * The number of nodes bellow this node. If the node is a leaf the value is 1 
   */
  protected int graphSize = 1;

  public NodeData(NodeTemplate nodeTemplate)
  {
    this.template = nodeTemplate;
  }

  public void setData(Map<String, String> data)
  {
    this.data.putAll(data);
  }

  public void addFlow(String name, NodeData nodeDest)
  {
    final FlowDefinition flowDef = template.getFlowDef(name);
    flows.put(flowDef, nodeDest);
  }
  
  public String printCode(String section)
  {
    final Map<String, String> toPrint = new HashMap<String, String>(data);
 
    for (Map.Entry<FlowDefinition, NodeData>  entry : flows.entrySet())
      if (entry.getKey() != null && entry.getValue() != null)
        toPrint.put(entry.getKey().getFlow(), entry.getValue().printCode(section));
  
    return template.print(toPrint);
  }

  public boolean isMethod()
  {
    return false;
  }
  
  public int getGraphSize()
  {
    return graphSize;
  }
  
  public void addGraphSize(int amount)
  {
    graphSize += amount;
  }
  
  @Override
  public String toString()
  {    
    return data.toString();
  }
}
