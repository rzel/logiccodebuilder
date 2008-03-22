package org.lcb.graphs.edg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EdgNode
{
  private final String[] EMPTY = new String[0];
  private final EdgConnector[] EMPTY_FLOWS = new EdgConnector[0];

  private List<EdgConnector> connectors = Collections.emptyList();
  private final Map<String,String> data = new HashMap<String, String>();

  public void setData(Map<String,String> data)
  {    
    this.data.putAll(data);
  }

  public void addData(String key, String data)
  {    
    this.data.put(key, data);
  }

  public Map<String,String> getData()
  {
    return data;
  }

  public void addConnector(EdgConnector connector)
  {
    if (connectors.isEmpty())
      connectors = new ArrayList<EdgConnector>();

    connector.addConnector(connector);
  }

  public String[] getFlowsData()
  {
    if (connectors.isEmpty())
      return EMPTY;

    final String[] flows = new String[connectors.size()];
    for (int i = 0; i < flows.length; i++)
      flows[i] = connectors.get(i).getData().get("NODE()");

    return flows;
  }

  public EdgConnector[] getFlows()
  {
    if (connectors.isEmpty())
      return EMPTY_FLOWS;

    return connectors.toArray(new EdgConnector[connectors.size()]);
  }
}
