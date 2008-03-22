package org.lcb.graphs.qsee;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.lcb.graphs.api.IGraphData;
import org.lcb.templates.api.NodeTemplate;
import org.lcb.xml.XMLElement;
import org.lcb.xml.XMLUtils;

public class GraphQsee implements IGraphData<XMLElement, XMLElement>
{
  private static final String START_NODE = "Flow Chart Start Node";

  private XMLElement xmlQsee;
  private XMLElement xmlTree;
 
  public void loadGraph(InputStream xmlData, String treeName)
  {
    try
    {
      xmlQsee = XMLUtils.load(xmlData);

      final XMLElement xmlProject = xmlQsee.getChild("metatype", "Project");
      xmlTree = xmlProject.getChild("name", treeName);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  //@Override
  public XMLElement getRoot()
  {
    final XMLElement xmlStart = xmlTree.getChild("metatype", START_NODE);
    return xmlStart;
  }
  
  //@Override
  public Map<String, String> getNodeData(XMLElement node)
  {
    Map<String, String> data = new HashMap<String, String>();
    data.put(NodeTemplate.NODE, node.getAttr("name"));
    return data;
  }

  //@Override
  public XMLElement[] getFlows(XMLElement node)
  {
    final String oId = node.getAttr("oid");
    final XMLElement[] xmlFlows = xmlTree.getChildren("Source", oId);
    return xmlFlows;
  }
  
  //@Override
  public XMLElement getDestination(XMLElement nodeOrigin, XMLElement flow)
  {
    final String oIdDest = flow.getAttr("Dest");
    return xmlTree.getChild("oid", oIdDest);
  }

  //@Override
  public String getFlowData(XMLElement nodeOrigin, XMLElement flow)
  {
    return flow.getAttr("name");
  }
}
