package org.lcb;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;

import org.lcb.help.Help;
import org.lcb.monitor.ChangeListener;
import org.lcb.monitor.FileMonitor;
import org.lcb.monitor.Monitors;
import org.lcb.xml.XMLElement;

public class CmdLineTranslation
{
  private IOManager ioManager;
  private LogicCodeBuilder logicCodeBuilder;
  
  public CmdLineTranslation()
  {}
  
  public void startMonitor()
  {    
    final XMLElement xmlMonitors = ioManager.getConf().getChild("monitors");
    if (xmlMonitors != null)
    {
      final Monitors monitors = new Monitors();
      monitors.setup(ioManager, xmlMonitors, new DoTranslation());
    }
  }
  
  public void doTranslation(final File file) throws Exception
  {
    if (file.exists()) 
    {
      ioManager = new IOManager(file);
      logicCodeBuilder = new LogicCodeBuilder(ioManager);
      logicCodeBuilder.doTranslation();
      startMonitor();        
    }
    else
    {
      System.err.println(file);
      Help.getHelp().printConfigFileNotFound();
    }
  }
  
  public static void main(String[] args)
  {
    try
    {
      if (args.length == 0)
      {
        if (new File("lcb-conf.xml").exists())
          new CmdLineTranslation().doTranslation(new File("lcb-conf.xml"));
        else
        {
          final JFileChooser fc = new JFileChooser();
          fc.setDialogType(JFileChooser.OPEN_DIALOG);
          fc.setDialogTitle("LCB Project File");
          fc.setCurrentDirectory(new File("."));
          fc.showOpenDialog(null);          
          if (fc.getSelectedFile() != null)
            new CmdLineTranslation().doTranslation(fc.getSelectedFile());
        }
      }
      else
        for (String arg : args)
          new CmdLineTranslation().doTranslation(new File(arg));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private class DoTranslation implements ChangeListener<FileMonitor> 
  {
    //@Override
    public void onChange(FileMonitor monitor)
    {
      try
      {
        System.out.println("File Changed: " + monitor.getFile().getAbsolutePath());
        logicCodeBuilder.doTranslation();
        monitor.getMonitors().updateAll();
      }
      catch (Exception e)
      {
        Toolkit.getDefaultToolkit().beep();
        e.printStackTrace();
      }
    }
  }
}
