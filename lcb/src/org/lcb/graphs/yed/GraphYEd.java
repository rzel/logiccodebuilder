package org.lcb.graphs.yed;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lcb.graphs.api.IGraphData;
import org.lcb.templates.api.NodeTemplate;
import org.lcb.xml.XMLElement;
import org.lcb.xml.XMLUtils;

public class GraphYEd implements IGraphData<XMLElement, XMLElement>
{
  private final Map<String, XMLElement> nodes = new HashMap<String, XMLElement>();
  
  private XMLElement xmlGraph;
  private XMLElement xmlStart;
  private String keyNodeUrl;
  private String keyNodeDescription;

 
  public void loadGraph(InputStream xmlData)
  {
    try
    {
      final XMLElement xmlYEd = XMLUtils.load(xmlData);
      xmlGraph = xmlYEd.getChild("graph");
      
      fillXMLNodes(xmlGraph);      
      xmlStart = findStart();
      
      keyNodeUrl = getID(xmlYEd.getChildren("attr.name", "url", "for","node"));
      keyNodeDescription = getID(xmlYEd.getChildren("attr.name", "description", "for","node"));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public String getID(XMLElement[] elements)
  {
    if (elements.length > 0)
      return elements[0].getAttr("id");
    
    return null;
  }
  
  private void fillXMLNodes(XMLElement xmlGraph)
  {
    final XMLElement[] xmlNodes = xmlGraph.getChildren();
    for (XMLElement element : xmlNodes)
    {
      if (element.getName().equals("node"))
        nodes.put(element.getAttr("id"), element);
      
      final XMLElement[] children = xmlGraph.getChildren();
      for (XMLElement graph : children)
        fillXMLNodes(graph);
    }    
  }
  
  private XMLElement findStart()
  {     
    final Set<String> sources = new HashSet<String>();
    final Set<String> targets = new HashSet<String>();
   
    final XMLElement[] edge = xmlGraph.getChildren("edge");
    for (XMLElement element : edge)
    {
      final String nSource = element.getAttr("source");
      final String nTarget = element.getAttr("target");
      
      sources.add(nSource);
      targets.add(nTarget);
    }
    
    sources.removeAll(targets);
    for (String source : sources)
    {
      return nodes.get(source);
    }
    return null;
  }

  //@Override
  public XMLElement getRoot()
  {
    return xmlStart;
  }
  
  //@Override
  public Map<String, String> getNodeData(XMLElement node)
  {
    Map<String, String> data = new HashMap<String, String>();
    data.put(NodeTemplate.NODE, getNodeLabel(node));
    
    final String URL = getProperty(node, keyNodeUrl);
    if (URL.length() > 0)
      data.put("URL", URL);
    
    final String desc = getProperty(node, keyNodeDescription);
    if (desc.length() > 0)
      data.put("Description", desc);
    
    return data;
  }

  //@Override
  public XMLElement[] getFlows(XMLElement node)
  {    
    final String oId = node.getAttr("id");
    final XMLElement[] xmlFlows = xmlGraph.getChildren("source", oId);
    return xmlFlows;
  }
  
  //@Override
  public XMLElement getDestination(XMLElement nodeOrigin, XMLElement flow)
  {
    final String oIdDest = flow.getAttr("target");
    return nodes.get(oIdDest);
  }

  //@Override
  public String getFlowData(XMLElement nodeOrigin, XMLElement flow)
  {
    return getEdgeLabel(flow);
  }
  
//-----------------------------------------------------------------------------------------------------

  private String getNodeLabel(XMLElement xmlNode)
  {
    final XMLElement xmlLabel = xmlNode.getChild(0).getChild("y:ShapeNode").getChild("y:NodeLabel");
    return xmlLabel.getValue();
  }

  private String getProperty(XMLElement xmlNode, String id)
  {
    if (id == null)
      return "";

    final XMLElement xmlLabel = xmlNode.getChild("key",id);
    if (xmlLabel == null)
      return "";
    else
      return xmlLabel.getValue() == null ? "" : xmlLabel.getValue().trim();
  }

  private String getEdgeLabel(XMLElement xmlNode)
  {
    final XMLElement xmlLabel = xmlNode.getChild(0).getChild("y:PolyLineEdge").getChild("y:EdgeLabel");
    if (xmlLabel == null)
      return null;
    else
      return xmlLabel.getValue() == null ? null : xmlLabel.getValue().trim();
  }
}




















