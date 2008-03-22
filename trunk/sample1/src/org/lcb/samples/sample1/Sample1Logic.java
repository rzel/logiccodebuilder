package org.lcb.samples.sample1;

public class Sample1Logic
{
    private final Sample1App app;

    public Sample1Logic(Sample1App app)
    {
        this.app = app;
    }

    public void start()
    {
        System.out.println("app.isMorning():"+(app.isMorning()));
        if(app.isMorning())
        {
        	app.sayGoodMorning(); 
        }
        else
        {
        	System.out.println("app.isAfternoon():"+(app.isAfternoon()));
        	if(app.isAfternoon())
        	{
        		app.sayGoodAfternoon(); 
        	}
        	else
        	{
        		System.out.println("app.isNight():"+(app.isNight()));
        		if(app.isNight())
        		{
        			app.sayGoodNight(); 
        		}
        		else
        		{
        			app.sayHi(); 
        		}
        	}
        }
    }

}