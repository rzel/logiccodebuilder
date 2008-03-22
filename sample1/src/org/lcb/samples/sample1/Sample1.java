package org.lcb.samples.sample1;

public class Sample1
{
  public static void main(String[] args)
  {
    final Sample1App app = new Sample1App();
    final Sample1Logic logic = new Sample1Logic(app);
    
    logic.start();
  }
}
