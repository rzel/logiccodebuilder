package org.lcb.help;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.lcb.templates.api.NodeTemplate;

public class Help
{
  public static final String CFG_NO_APP = "cfgNoApp";
  
  private StringTemplateGroup help;
  private PrintStream pw;
  
  private static final Help gHelp = new Help(System.err);
  
  private Help(PrintStream pw)
  {
    this.pw = pw;
    
    try
    {
      final InputStream input = getClass().getResourceAsStream("help.st");
      final byte[] data = new byte[input.available()];
      input.read(data);
      input.close();
      help = new StringTemplateGroup(new StringReader(new String(data)));
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static Help getHelp()
  {
    return gHelp;
  }
  
  public void cfgNoApp(boolean error)
  {
    printHelp(CFG_NO_APP, error);
  }

  public void appInstantiationException(Exception e)
  {
    printHelp("appInstantiationException", true);
  }

  public void printConfigFileNotFound()
  {
    pw.println(help.getTemplateDefinition("cmdHelp"));
  }

  private void printHelp(String template, boolean error)
  {
    if (error)
      pw.println(help.getTemplateDefinition(template));
  }

  public void doubleMatchWarning(NodeTemplate result, NodeTemplate definition)
  {
    final StringTemplate template = help.getTemplateDefinition("DoubleMatch");
    template.setAttribute("t1", result.toString());
    template.setAttribute("t2", definition.toString());    
    pw.println(template.toString());
  }

  public String getsDefNotFound(String stack, String nodeData)
  {
    final StringTemplate template = help.getTemplateDefinition("defNotFound");
    template.setAttribute("stack", stack);
    template.setAttribute("node", nodeData);    
    return template.toString();
  }

  public void defNotFound()
  {
    final StringTemplate template = help.getTemplateDefinition("defNotFound");
    pw.println(template.toString());
  }

  public void mainTemplateNotFound()
  {
    final StringTemplate template = help.getTemplateDefinition("mainTemplateNotFound");
    pw.println(template.toString());
  }

  
}
