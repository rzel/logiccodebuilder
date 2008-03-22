package org.lcb.monitor;

import java.io.File;

public class FileMonitor implements Monitor
{
  private final Monitors monitors;
  private final ChangeListener<FileMonitor> listener;
  private final File file;
  
  private long lastModified;  
  
  public FileMonitor(Monitors monitors, ChangeListener<FileMonitor> listener, File file)
  {
    this.monitors = monitors;
    this.listener = listener;
    this.file = file;
    
    lastModified = file.lastModified();
  }
  
  public Monitors getMonitors()
  {
    return monitors;
  }
  
  public File getFile()
  {
    return file;
  }
  
  public void update()
  {
    lastModified = file.lastModified();
  }
  
  public void check()
  {
    if (lastModified != file.lastModified())
    {       
      listener.onChange(this);
      update();
    }
  }
}
