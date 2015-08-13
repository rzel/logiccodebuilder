# **Logic Code Builder - LCB** #

## Objective ##
Use visual tools to represent complex logic and generate code in any programing language.

## Benefits ##
Using LCB decouples the information and data manipulation from the decision making, this separation reduces the complexity  of  the problem.

## Basic Examples ##

This is a basic flow chart example:
> ![http://img72.imageshack.us/img72/4238/graphsample1pk3.gif](http://img72.imageshack.us/img72/4238/graphsample1pk3.gif)

This flow chart will generate this code:
```
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
        if(app.isMorning()) /*este es el url*/
        {
            app.sayGoodMorning();
        }
        else
        {
            if(app.isAfternoon())
            {
                app.sayGoodAfternoon();
            }
            else
            {
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
```
Sample1 project is located at sample1/lcbproject/sample1.lcb.xml

This sample2 is a pseudo-code
> ![http://img208.imageshack.us/img208/7462/sample2ih7.gif](http://img208.imageshack.us/img208/7462/sample2ih7.gif)

This flow chart will generate this code:
```
package org.lcb.samples.sample2;

public class Sample2Logic
{
    private final Sample1App app;

    public Sample2Logic(Sample1App app)
    {
        this.app = app;
    }

    public void start()
    {
        if(condition1)
        {
            if(condition2)
            {
                if(condition3)
                {
                    statement2
                }
                else
                {
                    if(condition4)
                    {
                        statement2
                    }
                    else
                    {
                        if(condition5)
                        {
                            if(condiont6)
                            {
                                statement3
                                stup2(); 
                            }
                            else
                            {
                                procedureSample();
                            }
                        }
                        else
                        {
                            procedureSample();
                        }
                    }
                }
            }
            else
            {
                stup0();
            }
        }
        else
        {
            stup0();
        }
    }

    private void stup0()
    {
        statement1
        stup2(); 
    }
   
    private void procedureSample()
    {
        statement4
    }

    private void stup2()
    {
        statement5
        statement6
        for(int i = 0 ; condiont7Loop; i++ )
        {
            statement7
        }
        statement8
    }
}
```

Sample2 project is located at sample1/lcbproject/sample2.lcb.xml

## The code ##
The code is organized to provide an easy way to integrate different Visual Tools and Templates engines.

## Language generation. ##

LCB uses templates to generate code. This templates can be rewritten to generate any language. (no recompilation required)
Java templates are located at directory 'templates/java'

There is only one ready to use implementation for templates.
  * tringTemplate http://www.stringtemplate.org.

## Project configuration ##
LCB use a XML file with all the information required to generated the code. The XML contains the input file, templates and the output file.
  * The input file contains the graphic data.
  * Templates are divided in
    1. main template: Specifies the format for the output file. Each project must define its own main template.
    1. statements templates: The statements of the target language.
    1. procedures templates: If the target language support procedures this archive will be specify the procedure declaration and call format.

### Visual Tools. ###

Two interfaces are used to integrate visual tools
```
         org.lcb.graphs.api.Application
         org.lcb.graphs.api.IGraphData
```
See class org.decision.graphs.yed.YEdApp.java

There are two ready to use implementation for Visual tools
  * yEd - Java™ Graph Editor
  * QSEE Super Lite


#### yEd - Java™ Graph Editor ####

"yEd is a very powerful graph editor that is written entirely in the Java programming language. It can be used to quickly and effectively generate drawings and to apply automatic layouts to a range of different diagrams and networks"

You can find more information at http://www.yworks.com/en/products_yed_about.htm

The project implements a reader for the XML generated by **yEd - Java™ Graph Editor**, using the flow chart graph is easy to create the visual representation of the logic.

To use yEd specify this in project file:

> 

&lt;graph&gt;


> > <app class="org.decision.graphs.yed.YEdApp"
> > > file="yEdFile.graphml"

> > />

> 

&lt;/graph&gt;



#### QSEE Super Lite ####

"The QSEE multi-CASE tool is a collection of sub-tools designed to aid in the analysis and design of software type systems."
Flow Charts are the only charts supported.

You can find more information at http://www.leedsmet.ac.uk/qsee/complete.htm, please license information

The project implements a reader for the XML generated by **QSEE Super Lite**

To use QSEE specify this in project file:

> 

&lt;graph&gt;


> > <app class="org.decision.graphs.yed.YEdApp"
> > > file="testMethods.graphml"
> > > name="Flow Chart Name"

> > />

> 

&lt;/graph&gt;



## Java templates ##
Java templates are ready to use.

# Project File Sample #
This must be in a file with extension .xml
```
<lcb>
        <!-- properties
          	  argsdef="" (Optional. Default "") Arguments definition, each method created by LCB will contans this value in declaration. see argsval
              argsval="" (Optional. Default "") each call to a method created by LCB will use this value as a parameter, see argsdef
              statements-tag="statements" (Optional. Default "statements"), tag in the main template
              methods-tag="methods"       (Optional. Default "methods"), tag in the main template
              use-methods="true"          (Optional. Default "true"), this will enable or disable the creation of methods
              use-methods-graph-size="2"  (Optional. Default "2"), Minimum number of nodes below required to generate a method
              use-auto-methods="true"     (Optional. Default "true"), Auto generate methods when 2 or more flows arrive to one node
              method-prefix="stup"        (Optional. Default "stup"), Auto methods will have this prefix plus a number. example: stup1(), stup2()
        -->
	<properties
        use-methods="true"
        use-auto-methods="true"
        use-methods-graph-size="2"
        method-prefix="stup"
        argsval=""
        argsdef=""
	/>

    <templates statements="templates/java/statements.st"
               methods="templates/java/methods.st" />

	<graph>
	    <app class="org.lcb.graphs.yed.YEdApp"
		    file="sample2.graphml"
		/>
	</graph>

	<main-template
	    in="sample2.java.st"
	    out="sample2.out"
	/>

</lcb>
```


# Motivation to create this tool #
I was working in my project and found a major problem with my bots that required to check a lot of states and conditions, so I decided to try some visual programming tools and rule based systems. The problem with this tools was that they need a framework and/or have their own language, plus the IDE and debugging tools where incomplete or out of date.

So I decided to try something simple, and use a FSM, a flow chart tool and generate Java from the output file, so I can have the best of the two worlds, a visual representation and Java code, the result was this tool.

I thought maybe this could help others, so I decided to make it public, please let me know what do you think or if you want to use it and need something extra, let me know, I can help.

# About #
Luis Carlos Hernandez Vega
luiskarlos@gmail.com


