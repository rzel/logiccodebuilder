package org.lcb.templates.api;

import java.util.Map;

import org.lcb.IOManager;
import org.lcb.xml.XMLElement;

public interface Templates
{
  public static final String TEMPLATE_NAME = "template";
  public static final String NODE = "node";

  /**
   * The configuration contains the XML node with the information
   * in lcb->templates section. This information will be used to configure
   * and load the graph.
   *  
   * @param configuration information in lcb->templates configuration file 
   */
  void configure(IOManager ioManager, XMLElement configuration)  throws Exception;

  String apply(String template, Map<String, Object> data);
  
  NodeTemplate[] getStatements();

  NodeTemplate getError();

  NodeTemplate getMethod();

  NodeTemplate getCall();

  String[] getFlowDefinitions(Object template);
  
  String print(Object template, Map<String, String> data);

}