package org.lcb.samples.sample1;

import java.util.Calendar;

import javax.swing.JOptionPane;

public class Sample1App
{

  public boolean isMorning()
  {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 11;
  }

  public void sayGoodMorning()
  {
    JOptionPane.showMessageDialog(null, "Good Morning", "LCB Sample1", JOptionPane.INFORMATION_MESSAGE);
  }

  public boolean isAfternoon()
  {
    return  Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 11 && 
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18;
  }

  public void sayGoodAfternoon()
  {
    JOptionPane.showMessageDialog(null, "Good Afternoon", "LCB Sample1", JOptionPane.INFORMATION_MESSAGE);
  }

  public boolean isNight()
  {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 18; 
  }

  public void sayGoodNight()
  {
    JOptionPane.showMessageDialog(null, "Good Night", "LCB Sample1", JOptionPane.INFORMATION_MESSAGE);  
  }

  public void sayHi()
  {
    JOptionPane.showMessageDialog(null, "Hi!", "LCB Sample1", JOptionPane.INFORMATION_MESSAGE);
  }

}
