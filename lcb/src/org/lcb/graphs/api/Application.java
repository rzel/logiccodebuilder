package org.lcb.graphs.api;

import org.lcb.IOManager;
import org.lcb.builder.GraphDataWalker;
import org.lcb.builder.NodesContainer;
import org.lcb.builder.WalkException;
import org.lcb.node.NodeData;
import org.lcb.xml.XMLElement;

/**
 * Application allows you to integrate the graph application with the
 * builder. 
 *
 * @see {@link NodeData}
 * @see {@link Application}
 * @see {@link GraphDataWalker}
 * @see {@link GraphYEd}
 * 
 */
public interface Application
{
  /**
   * The configuration contains the XML node with the information
   * in lcb->graph->app section. This information will be used to configure
   * and load the graph.
   *  
   * @param configuration information in lcb->graph->app configuration file 
   */
  void configure(IOManager resourceLoader, XMLElement configuration);
  
  /**
   * This return the NodeData root. 
   * 
   * This tree is build using {@link GraphDataWalker} and {@link IGraphData}
   * 
   * @param container The container will create and keep all the Nodes.
   * @return NodeData root of the tree
   * @throws WalkException
   */
  NodeData getRoot(NodesContainer container) throws WalkException;

  /**
   * The statements will be printed in the main template using this tag
   * as a target.
   * 
   * @return statements tag or null to use the default value
   */
  String getStatementsTag();

  /**
   * The methods will be printed in the main template using this tag
   * as a target.
   * 
   * @return methods tag or null to use the default value
   */
  String getMethodsTag();  
}
