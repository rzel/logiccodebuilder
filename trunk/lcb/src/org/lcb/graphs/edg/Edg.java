package org.lcb.graphs.edg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lcb.IOManager;
import org.lcb.builder.NodesContainer;
import org.lcb.builder.WalkException;
import org.lcb.graphs.api.Application;
import org.lcb.graphs.api.IGraphData;
import org.lcb.node.NodeData;
import org.lcb.xml.XMLElement;

public class Edg implements IGraphData<EdgNode, EdgConnector>, Application
{
  private EdgNode root;
  private final Map<String, EdgNode> figures = new HashMap<String, EdgNode>();
  private final Map<String, EdgConnector> connectors = new HashMap<String, EdgConnector>();
  private final List<EdgStaple> staples = new ArrayList<EdgStaple>();


  public void loadGraph(InputStream input) throws Exception
  {
    final BufferedReader bInput = new BufferedReader(new InputStreamReader(input));
    String line = gotoLine(bInput, "## Figures & Connectors Section");
    for (;line != null;)
    {
      if (line.startsWith("Figure"))
        readFigure(bInput, line);
      else if (line.startsWith("Connector"))
        readConnector(bInput, line);
      else if (line.startsWith("Staple"))
        readStaple(bInput, line);

      line = bInput.readLine().trim();
    }

    for (EdgStaple staple : staples)
    {
      final EdgNode label = figures.remove(staple);
      connectors.get(staple.getConnector()).setData(label.getData());
    }

    for (EdgConnector connector : connectors.values())
    {
      figures.get(connector.getFigure1()).addConnector(connector);
    }
  }

  private void readFigure(BufferedReader input, String strLine) throws Exception
  {
    final String figure[] = strLine.split("\\s+");
    final EdgNode node = new EdgNode();

    figures.put(figure[1], node);

    String line = gotoLine(input, "{");
    line = input.readLine().trim();
    for (;line.equals("}");)
    {
      final int keyEnd = line.indexOf(' ');
      final String key = line.substring(0, keyEnd);
      final String value = line.substring(keyEnd).trim();

      if (key.startsWith("Text"))
      {
        node.addData("NODE()", value.substring(1,value.length()-1));
        if (node.getData().equals("START"))
        {
          if (root != null)
            throw new Exception("Two START nodes found");

          root = node;
        }
      }

      line = input.readLine().trim();
    }
  }

  private void readConnector(BufferedReader input, String strLine) throws Exception
  {
    final String figure[] = strLine.split("\\s+");
    final EdgConnector node = new EdgConnector();

    connectors.put(figure[1], node);

    String line = gotoLine(input, "{");
    line = input.readLine().trim();
    for (;line.equals("}");)
    {
      final int keyEnd = line.indexOf(' ');
      final String key = line.substring(0, keyEnd);
      final String value = line.substring(keyEnd).trim();

      if (key.startsWith("Figure1"))
        node.setFigure1(value);

      if (key.startsWith("Figure2"))
        node.setFigure2(value);

      line = input.readLine().trim();
    }
  }

  private void readStaple(BufferedReader input, String strLine) throws Exception
  {
    final EdgStaple node = new EdgStaple();
    staples.add(node);

    String line = gotoLine(input, "{");
    line = input.readLine().trim();
    for (;line.equals("}");)
    {
      final int keyEnd = line.indexOf(' ');
      final String key = line.substring(0, keyEnd);
      final String value = line.substring(keyEnd).trim();

      if (key.startsWith("Connector"))
        node.setConnector(value);

      if (key.startsWith("Figure1"))
        node.setFigure2(value);

      line = input.readLine().trim();
    }
  }



  public String gotoLine(BufferedReader bInput, String tag) throws IOException
  {
    String line = bInput.readLine();
    for (;line != null;)
    {
      if (line.indexOf(tag) > 0)
        return line;
      line = bInput.readLine();
    }

    return null;
  }

  public EdgNode getRoot()
  {
    return root;
  }

  public EdgConnector[] getFlows(EdgNode node)
  {
    return node.getFlows();
  }

  public EdgNode getDestination(EdgNode nodeOrigin, EdgConnector flow)
  {
    return figures.get(flow.getFigure2());
  }

  public String[] getFlowsData(EdgNode node)
  {
    return node.getFlowsData();
  }

  public String getFlowData(EdgNode nodeOrigin, EdgConnector flow)
  {
    return flow.getData().get("NODE()");
  }

  public Map<String,String> getNodeData(EdgNode node)
  {
    return node.getData();
  }

  public void openNode(EdgNode node)
  {
  }

  public void closeNode(EdgNode node)
  {
  }

  //@Override
  public void configure(IOManager resourceLoader, XMLElement configuration)
  {
    throw new UnsupportedOperationException();
  }

  //@Override
  public NodeData getRoot(NodesContainer builder) throws WalkException
  {
    return null;
  }

  //@Override
  public String getMethodsTag()
  {
    // TODO Auto-generated method stub
    return null;
  }

  //@Override
  public String getStatementsTag()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
