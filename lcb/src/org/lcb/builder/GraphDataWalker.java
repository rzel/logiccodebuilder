package org.lcb.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lcb.graphs.api.IGraphData;
import org.lcb.node.NodeData;

/**
 * This class will walk through the {@link IGraphData} using the flows to jump
 * to the next graph node, in each node it will ask to {@link NodesContainer}
 * for the {@link NodeData}, this node will be added to the node source to
 * build the tree.
 * 
 * @see IGraphData
 * @see NodesContainer
 * @see NodeData
 * 
 * 
 * @param <T>
 *          this refers to {@link IGraphData} type, this is used as a key to
 *          identify nodes in the Graph
 * @param <F>
 *          this refers to {@link IGraphData} type, this is used as a key to
 *          identify flows in the Graph
 */
public class GraphDataWalker<T extends Object, F extends Object>
{
  /**
   * Basic helper. This will show where in the graph was an error.
   */
  private final List<String> stackTrace = new ArrayList<String>();

  /**
   * Walk through the graph
   * 
   * @param container
   *          to create and keep the NodeData
   * @param graphData
   *          graph representation
   * @return the root of the true
   * 
   * @throws WalkException
   *           on any error
   */
  public NodeData go(NodesContainer container, IGraphData<T, F> graphData) throws WalkException
  {
    final T gRoot = graphData.getRoot();
    final F[] gFlows = graphData.getFlows(gRoot);
    
    if (gFlows.length > 0)
    {
      final T firstNode = graphData.getDestination(gRoot, gFlows[0]);
      final NodeData root = walk(container, graphData, firstNode);
      return root;
    }
    else
      return container.newNode(gRoot, new String[0]);
  }

  /**
   * Visit the node and his targets. If the node was already visit it just
   * increment his references
   */
  private NodeData walk(NodesContainer container, IGraphData<T, F> graphData, T gNode) throws WalkException
  {
    try
    {
      if (container.contains(gNode))
        return container.changeToMethod(gNode);
      else
      {
        final String[] gFlows = getFlowsData(graphData, gNode);
        final NodeData nodeData = container.newNode(gNode, gFlows);

        final Map<String, String> data = graphData.getNodeData(gNode);
        nodeData.setData(data);
        stackTrace.add(data.toString());

        for (F gFlow : graphData.getFlows(gNode))
        {
          final T gNodeDest = graphData.getDestination(gNode, gFlow);
          final NodeData nodeDest = walk(container, graphData, gNodeDest);

          final String flowData = graphData.getFlowData(gNode, gFlow);
          nodeData.addFlow(flowData, nodeDest);

          nodeData.addGraphSize(nodeDest.getGraphSize());
        }

        stackTrace.remove(stackTrace.size() - 1);
        return nodeData;
      }
    }
    catch (WalkException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new WalkException(new Exception(getStack(), e));
    }    
  }

  /**
   */
  private String[] getFlowsData(IGraphData<T, F> graphData, T node)
  {
    final F[] flows = graphData.getFlows(node);
    final String[] flowsData = new String[flows.length];

    for (int i = 0; i < flowsData.length; i++)
    {
      flowsData[i] = graphData.getFlowData(node, flows[i]);
    }
    return flowsData;
  }

  /*
   */
  private String getStack()
  {
    final StringBuilder strStack = new StringBuilder();
    for (String node : stackTrace)
    {
      strStack.append("->");
      strStack.append(node);
    }
    return strStack.toString();
  }
}
