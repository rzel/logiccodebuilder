package org.lcb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.lcb.help.Help;
import org.lcb.xml.XMLElement;

public class IOManager
{
  private static final String XML_MAIN_TEMPLATE = "main-template";
  private static final String XML_OUT           = "out";
  private static final String XML_IN            = "in";


  private static final String NEWLINE = LogicCodeBuilder.NEWLINE;  
  private final File baseDir;
  private final XMLElement conf;
  
  public IOManager(File confFile)  
  {
    this.baseDir = confFile;
    this.conf = XMLElement.load(baseDir); 
  }
  
  public XMLElement getConf()
  {
    return conf;
  }
  
  public void writeMainOut(String code) throws IOException 
  {
    final FileWriter output = new FileWriter(getOutputFile());
    output.write(code);
    output.close();
    
    System.out.println("Code successfully generated. "+ getOutputFile().getAbsolutePath());
  }
  
  public File getOutputFile()
  {
    final XMLElement xmlMain  = getConf().getChild(XML_MAIN_TEMPLATE);
    final File fOutput = findFile(xmlMain.getAttr(XML_OUT));
    return fOutput;
  }
  
  public String loadMainTemplate() throws Exception
  {
    final XMLElement xmlMain = getConf().getChild(XML_MAIN_TEMPLATE);    
    final String fTemplate = xmlMain.getAttr(XML_IN);
    
    return loadText(fTemplate);
  }
  
  private String loadText(String file) throws Exception
  {
    final InputStream input = findResource(file);
    if (input == null)
      Help.getHelp().mainTemplateNotFound();
    
    final Reader reader = new InputStreamReader(input);
    final char[] charBuffer = new char[(int)input.available()];
    reader.read(charBuffer);
    reader.close();
    return new String(charBuffer);
  }
  
  public InputStream findResource(String resource) throws FileNotFoundException  
  {
    final File file = new File(resource);
    if (file.exists())
      return new FileInputStream(file);
    
    final File fileInConf = new File(baseDir.getParent(), resource);
    if (fileInConf.exists())
      return new FileInputStream(fileInConf);
    
    final InputStream in = getClass().getResourceAsStream(resource);
    if (in != null)
      return in;
    
    final InputStream inBase = getClass().getResourceAsStream("/"+resource);
    if (inBase != null)
      return inBase;
    
    throw new FileNotFoundException(NEWLINE+"Resource not fount: "+resource+ NEWLINE+"Checked places" +NEWLINE+
                                     "File system:" + NEWLINE +
                                     "      "+ file.getAbsolutePath() + NEWLINE + 
                                     "      "+ fileInConf.getAbsolutePath() + NEWLINE+
                                     "Classpath: "+ NEWLINE+
                                     "      "+ resource + NEWLINE+
                                     "      /"+ resource +
                                     NEWLINE
                                     ); 
  }

  public File findFile(String resource)
  {
    final File file = new File(resource);
    if (file.exists())
      return file;
    
    final File fileInConf = new File(baseDir.getParent(), resource);    
    return fileInConf;    
  }
}
