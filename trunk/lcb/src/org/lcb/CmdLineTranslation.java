package org.lcb;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
  
  public String doTranslation(final File file) throws Exception
  {
    if (file.exists()) 
    {
      ioManager = new IOManager(file);
      logicCodeBuilder = new LogicCodeBuilder(ioManager);
      logicCodeBuilder.doTranslation();
      startMonitor();
      return ioManager.getOutputFile().getAbsolutePath();
    }
    else
    {
      System.err.println(file);
      Help.getHelp().printConfigFileNotFound();
      return "";
    }
  }
  
  private static void useBasicGui()
  {
    try
    {
      final JFileChooser fc = new JFileChooser();
      fc.addChoosableFileFilter(new LcbFilter());
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setDialogTitle("LCB Project File");
      fc.setCurrentDirectory(new File("."));
      fc.showOpenDialog(null);          
      if (fc.getSelectedFile() != null)
      {
        final String out = new CmdLineTranslation().doTranslation(fc.getSelectedFile());
        JOptionPane.showMessageDialog(null, "Code successfully generated.\n Output file: \n"+out, "Logic Code Builder", JOptionPane.INFORMATION_MESSAGE);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error. "+e.getMessage(), "Logic Code Builder", JOptionPane.ERROR_MESSAGE);
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
          useBasicGui();
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
  
  private static class LcbFilter extends javax.swing.filechooser.FileFilter 
  {
    public boolean accept(File file) 
    {
      if (file.isDirectory())
        return true;
      
      final String filename = file.getName();
      return filename.endsWith(".lcb.xml");
    }
    public String getDescription() 
    {
        return "*.lcb.xml";
    }
}
}
