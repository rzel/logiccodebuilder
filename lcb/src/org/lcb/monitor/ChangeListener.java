package org.lcb.monitor;


public interface ChangeListener<T extends Monitor>
{
  public void onChange(T monitor);
}
