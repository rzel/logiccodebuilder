package org.lcb.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lcb.IOManager;
import org.lcb.xml.XMLElement;

public class Monitors
{
  private static final String XML_FILE          = "file";
  
  private final List<Monitor> monitors = new ArrayList<Monitor>();
  private final Thread t;
  private long millsInterval;
  
  public Monitors()
  {
    t = new Thread(new Daemon());
    t.setPriority(Thread.MIN_PRIORITY);
    t.start();
  }
  
  public void setup(IOManager ioManager, XMLElement conf, ChangeListener<FileMonitor> changeListener)
  {
    final XMLElement xmlMonitors = conf;
    if (xmlMonitors != null && xmlMonitors.getAttr("enabled", false))
    {
      millsInterval = xmlMonitors.getAttr("millis-interval", 1000);
      final XMLElement[] xmlMonitorConf = xmlMonitors.getChildren("monitor");
      for (XMLElement xmlMonitor : xmlMonitorConf)
      {
        final File file = ioManager.findFile(xmlMonitor.getAttr(XML_FILE));
        final FileMonitor fMonitor = new FileMonitor(this, changeListener, file);
        addMonitor(fMonitor);
        
        System.out.println("Monitor on: " + file.getAbsolutePath());
      }
      System.out.println();
    }
  }
  
  public void updateAll()
  {
    for (Monitor monitor : monitors)
      monitor.update();
  }
  
  public void addMonitor(Monitor monitor)
  {
    monitors.add(monitor);
  }
  
  private class Daemon implements Runnable
  {
    //@Override
    public void run()
    {
      for(;;)
      {
        try
        {
          for (Monitor monitor : monitors)
          {
            Thread.yield();
            monitor.check();
          }
          
          Thread.sleep(millsInterval);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }    
  }
}
