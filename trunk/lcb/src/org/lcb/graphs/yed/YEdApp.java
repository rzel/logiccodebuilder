package org.lcb.graphs.yed;

import java.io.InputStream;

import org.lcb.IOManager;
import org.lcb.builder.GraphDataWalker;
import org.lcb.builder.NodesContainer;
import org.lcb.builder.WalkException;
import org.lcb.graphs.api.Application;
import org.lcb.node.NodeData;
import org.lcb.xml.XMLElement;

public class YEdApp implements Application
{
  private final GraphYEd yEd = new GraphYEd();
  private XMLElement configuration;

  //@Override
  public void configure(IOManager ioManager, XMLElement configuration)
  {
    try
    {
      this.configuration = configuration;
      final String strFile = configuration.getAttr("file");
      final InputStream input = ioManager.findResource(strFile);
      yEd.loadGraph(input);
      input.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  //@Override
  public String getMethodsTag()
  {
    return configuration.getAttr("methods-tag","methods");
  }

  //@Override
  public String getStatementsTag()
  {
    return configuration.getAttr("statements-tag","statements");    
  }
 
  //@Override
  public NodeData getRoot(NodesContainer container) throws WalkException
  {
    final GraphDataWalker<XMLElement, XMLElement> walker = new GraphDataWalker<XMLElement, XMLElement>();
    final NodeData code = walker.go(container, yEd);
    return code;
  }

}
