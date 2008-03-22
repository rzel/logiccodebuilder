package org.lcb.graphs.api;

import java.util.Map;

import org.lcb.builder.GraphDataWalker;

/**
 *  This class provide the nodes and flows information from graph 
 *  representation to be printed later in the target language.
 *  
 *  The CodeBuilder class will provide information every connected 
 *  node.
 *  
 *  Generic T is used as a key to identify a node in the graph
 *  Generic F is used as a key to identify a flow in graph
 *  
 *  Generic T and F are not used directly by the CodeBuilder, the are
 *  used as keys to get the final information.
 *
 *  The IGraphData must identify and provide the information requested
 *  for T and F.
 *
 * @see {@link NodeData}
 * @see {@link Application}
 * @see {@link GraphDataWalker}
 * @see {@link GraphYEd}
 *  
 */
public interface IGraphData<T extends Object, F extends Object>
{ 
  /**
   * The root is the start of the graph, the code will be build using
   * this node as the beginning.
   * 
   * @return The root node of the graph.
   */
  T getRoot();

  /**
   * This will return all the attributes inside the node, the value of the
   * attributes will be used to replace tags in the templates. 
   * 
   * The attributes are in direct relation with the attributes in the template.
   * Templates will set all the attributes with an empty string, this make the
   * attributes optional.
   * 
   * Example
   *    method(node,procedure) ::= <<
   *    <comment>
   *    <node>()
   *    >>
   *    
   *    This template is expecting two attributes from the node ("node"="a == 1", comment="test a value").
   * 
   * @param node this node can be the root or a node from <tt>getDestination</tt>
   * 
   * @return attributes/values 
   */
  Map<String,String> getNodeData(T node);
  
  /**
   * A flow represents the link between nodes. If the node represents a question 
   * that have two answers yes/no the flows will be yes an no.
   * 
   * @param node the source/parent of the flows
   * 
   * @return array with the flow representation.
   */
  F[] getFlows(T node);
  
  /**
   * This will return the text inside the flow or an NULL if the flow has no text.
   * 
   * @param node the source/parent of the flows
   * @return Text inside the flow with (same order of <tt>getFlows</tt>)
   */
  String getFlowData(T nodeOrigin, F flow);
  
  /**
   * CodeBuilder will generate the code following the nodes path. This method must return
   * the node target of the flow.
   *  
   * @param nodeOrigin source/parent of the flow
   * @param flow the flow/link to the next node. 
   * 
   * @return Node target of the flow
   */
  T getDestination(T nodeOrigin, F flow);
}
