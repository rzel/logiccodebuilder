package org.lcb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lcb.builder.NodesContainer;
import org.lcb.builder.WalkException;
import org.lcb.graphs.api.Application;
import org.lcb.help.Help;
import org.lcb.node.NodeData;
import org.lcb.templates.api.Templates;
import org.lcb.templates.stringtemplate.TemplateFactory;
import org.lcb.xml.XMLElement;


public class LogicCodeBuilder
{ 
  private static final String XML_APP           = "app";
  private static final String XML_GRAPH         = "graph";  
  private static final String XML_CLASS         = "class"; 
  
  public static final String NEWLINE           = System.getProperty("line.separator");
    
  private final IOManager ioManager;
  private final LCBProperties properties = new LCBProperties();
  private final Templates templates;
  
  public LogicCodeBuilder(IOManager ioManager)  throws Exception
  {
    this.ioManager = ioManager;
    this.templates = new TemplateFactory();
    templates.configure(ioManager, ioManager.getConf().getChild("templates"));
  }
  
  private void load() throws Exception
  {
    final XMLElement additionalProps = ioManager.getConf().getChild("properties");
    if (additionalProps != null)
      properties.addAdditionalProperties(additionalProps);
  }

  private Application loadApp(XMLElement xmlAppConf) throws Exception
  {
    try
    {
      final Application app = xmlAppConf.<Application>getAttr(XML_CLASS, Application.class).newInstance();
      app.configure(ioManager, xmlAppConf);
      return app;
    }
    catch (InstantiationException e)
    {
      Help.getHelp().appInstantiationException(e);
      throw e;
    }
  }

  private NodesContainer loadContainer()
  { 
    final NodesContainer container = new NodesContainer(properties);      
                
    container.loadMethodDefinitions(templates.getMethod(), templates.getCall());
    container.loadDefinitions(templates.getStatements());      
    container.loadErrorDefinition(templates.getError());
    return container;
  }

  void doTranslation() throws Exception
  {       
    load();
    final Map<String, Object> mainValues = new HashMap<String, Object>();
    
    final XMLElement[] xmlAppConf = ioManager.getConf().getChild(XML_GRAPH).getChildren(XML_APP);
    Help.getHelp().cfgNoApp(xmlAppConf.length == 0);
    for (XMLElement xmlParserConf : xmlAppConf)
    {
      final Application app = loadApp(xmlParserConf);
      addInfoToMain(mainValues,app);
    }
    
    final String main = ioManager.loadMainTemplate();
    ioManager.writeMainOut(templates.apply(main, mainValues));
  }
  
  private void addInfoToMain(Map<String, Object> mainValues, final Application app) throws WalkException
  {   
    final NodesContainer container = loadContainer();
    final NodeData code = app.getRoot(container);    
    
    final String statementsTag = properties.checkProperty("statements-tag", app.getStatementsTag());
    final String methodsTag  = properties.checkProperty("methods-tag", app.getMethodsTag());
    
    mainValues.put(statementsTag, code.printCode(NodeData.SECTION_MAIN_CODE));
    mainValues.put(methodsTag, printMethods(container));
  }
  
  private String printMethods(NodesContainer builder)
  {    
    final StringBuffer printedMethod = new StringBuffer();
    for (NodeData method : builder.getMethods())
    {
      final String printed = method.printCode(NodeData.SECTION_METHODS); 
      if (printed.trim().length() > 0)
      {
        if (printedMethod.length() > 0)
          printedMethod.append(NEWLINE).append(NEWLINE);
        
        printedMethod.append(printed);
      }
    }
    return printedMethod.toString();
  }
}












