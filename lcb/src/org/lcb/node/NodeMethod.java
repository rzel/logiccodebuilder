package org.lcb.node;

import org.lcb.LCBProperties;
import org.lcb.templates.api.NodeTemplate;

/**
 * NodeMethod contains NodeData that will be printed as a call or as a method declaration,
 * depending on the section that need to be print.
 * 
 * This kind of method is generated when multiples flows arrives to the same node.
 * The name of the method is generated.
 * 
 * @see Met
 */
public class NodeMethod extends NodeData
{
  protected static final String ARGS_DEF = LCBProperties.ARGS_DEF;
  protected static final String ARGS_VAL = LCBProperties.ARGS_VAL;

  protected final LCBProperties props;
  private final NodeTemplate callTemplate;

  public NodeMethod(LCBProperties props, NodeTemplate methodTemplate, NodeTemplate callTemplate)
  {
    super(methodTemplate);    
    this.props = props;
    this.callTemplate = callTemplate;
    
    data.put(ARGS_VAL, props.getArgsVal());
    data.put(ARGS_DEF, props.getArgsDef());
  }

  public NodeData getBody()
  {
    for (NodeData body : flows.values())
      return body;
    
    return null;
  }
  
  @Override
  public int getGraphSize()
  {
    if (props.useMethods() && getBody().getGraphSize() >= props.useMethodsGraphSize())
      return 1;
    else
      return getBody().getGraphSize();
  }
  
  protected boolean printAsMethod()
  {
    return props.useMethods();
  }
  
  @Override
  public boolean isMethod()
  {
    return true;
  }
  
  @Override
  public String printCode(String section) 
  {
    if (printAsMethod())
    {
      if (section.equals(NodeData.SECTION_MAIN_CODE))
        return callTemplate.print(data);
      else
        return super.printCode(SECTION_MAIN_CODE);
    }
    else
    {
      if (section.equals(NodeData.SECTION_MAIN_CODE))
        return getBody().printCode(section);
    }
    
    return "";
	}
 
  @Override
  public String toString()
  {    
    return "method-"+data;
  }
}
