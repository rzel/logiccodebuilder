package org.lcb.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lcb.LCBProperties;
import org.lcb.graphs.api.IGraphData;
import org.lcb.help.Help;
import org.lcb.node.NodeAutoMethod;
import org.lcb.node.NodeData;
import org.lcb.node.NodeMethod;
import org.lcb.node.NodeWrapper;
import org.lcb.templates.api.ErrorTemplate;
import org.lcb.templates.api.FlowDefinition;
import org.lcb.templates.api.NodeTemplate;
import org.lcb.templates.api.NodeTemplate.CheckResult;

/**
 * This container make a match between definition and flows 
 * 
 * The flows from {@link IGraphData} are matched
 * to flows in {@link NodeTemplate}, if the {@link NodeTemplate}
 * can "understand" the flows from the node it is a match and the 
 * definition is associated with the node.
 * 
 * If the association fails the node will be associated to {@link ErrorTemplate}
 * 
 * If more than one association is done it will print a warning and the node will be
 * associated to the first match.
 * 
 * This is a template example for the if statement.
 *      if(node,yes,no) ::= <<
 *      if(<node>)
 *      {
 *        <yes>
 *      }
 *      else
 *      {
 *        <no>
 *      }>>
 * The flows are yes and no.
 * If flows from {@link IGraphData} return an array with ["yes","no"] them the
 * node will produce a match against this {@link NodeTemplate} 
 * 
 * 
 * @see NodeTemplate
 * @see FlowDefinition
 * @see MethodTemplate
 * @see ErrorTemplate
 * @see NodeWrapper
 * @see <a href="http://www.stringtemplate.org">
 */
public class NodesContainer
{
  private final List<NodeTemplate> definitions = new ArrayList<NodeTemplate>();
  private NodeTemplate methodTemplate; 
  private NodeTemplate callTemplate;
  private NodeTemplate errorDef = null;  

  /**
   * Nodes created during the process 
   */
  private final Map<Object, NodeWrapper> wrappers = new HashMap<Object, NodeWrapper>();
  private final List<NodeData> methods = new ArrayList<NodeData>();
  private LCBProperties props;

  public NodesContainer(LCBProperties props)
  {
    this.props = props;
  }
  /**
   * Creates {@link NodeTemplate} for each template in the group
   * 
   * @param templates  
   */
  public void loadDefinitions(NodeTemplate[] templates)
  {
    for (NodeTemplate template : templates)
      definitions.add(template);
  }
  
  /**
   * Creates {@link MethodTemplate} for each template in the group
   * 
   * @param templates  
   */  
  public void loadMethodDefinitions(NodeTemplate method, NodeTemplate call)
  {
    methodTemplate =  method;
    callTemplate = call;
    definitions.add(methodTemplate);
  }
  
  /**
   * Creates one {@link ErrorTemplate} 
   * 
   * @param templates  
   */
  public void loadErrorDefinition(NodeTemplate error)
  {
    errorDef = error;
  }
  
  /**
   * This method make the match between the {@link NodeTemplate} and the flows
   * 
   * @param id key to identify the {@link NodeData}
   * @param flows This flows are from {@link IGraphData#getFlowData(T, F)}
   *  
   * @return The {@link NodeData} associated with the flows. 
   *         This node can be new or if it exits the same instance will be returned.
   */
  public NodeData newNode(Object id, String[] flows)
  {
    final NodeData codeNode = getWrapper(id);
    if (codeNode == null)
    {
      NodeTemplate definition = findDefinition(flows);
      if (definition == null)
      {
        Help.getHelp().defNotFound();
        definition = errorDef;
      }
      
      if (definition == methodTemplate)
      {
        final NodeData node = new NodeMethod(props, methodTemplate, callTemplate);
        final NodeWrapper wrapper = newWrapper(id, node);
        methods.add(wrapper.getNode());
        return wrapper;
      }
      else
      {        
        final NodeData node = new NodeData(definition);
        final NodeWrapper wrapper = newWrapper(id, node);
        return wrapper;
      }
    }
    else
      return codeNode;
  }

  /**
   * Check if the {@link NodeTemplate} for this key already exist
   * @param id the key
   * @return true if exists otherwise false
   */
  public boolean contains(Object id)
  {
    return wrappers.containsKey(id);
  }
 
  /**
   * This will change the {@link NodeData} to a {@link NodeAutoMethod}. This will be
   * reflected in the way that the node is printed.
   *  
   * @param id the key
   * 
   * @return the {@link NodeData} changed
   */
  public NodeData changeToMethod(Object id)
  {
    final NodeWrapper codeNode = wrappers.get(id);
    if (codeNode != null && !codeNode.isMethod())
    {
      codeNode.changeToMethod();
      methods.add(codeNode.getNode());
    }
    return codeNode;
  }

  /**
   * This will return all the methods definition created during the walk
   * 
   * @return all the methods
   */
  public NodeData[] getMethods()
  {
    return methods.toArray(new NodeData[methods.size()]);
  }

  /**
   * The matcher... :) 
   */
  private NodeTemplate findDefinition(String[] flows)
  {
    CheckResult checkResult = CheckResult.FAILED; 
    NodeTemplate result = null;
    
    for (NodeTemplate definition : definitions)
    {
      final CheckResult defResult = definition.match(flows);
      if (defResult != CheckResult.FAILED)
      {
        if (checkResult == CheckResult.FAILED || 
            (checkResult == CheckResult.ACCEPTED && defResult == CheckResult.EXACT))
        {
          result = definition;
          checkResult = defResult; 
        }
        else if (checkResult == CheckResult.EXACT && defResult == CheckResult.EXACT)
        {
          Help.getHelp().doubleMatchWarning(result, definition);          
        }          
      }
    }

    return result;
  }
  
  /**
   * fire wall ;)  
   */
  private NodeData getWrapper(Object id)
  {
    return wrappers.get(id);
  }
  /**
   * Another fire wall ;)  
   */
  private NodeWrapper newWrapper(Object id, NodeData codeNode)
  { 
    final NodeWrapper wrapper = new NodeWrapper(props, codeNode, methodTemplate, callTemplate);
    wrappers.put(id, wrapper);
    return wrapper;
  }
}















