group help;

cfgNoApp() ::= <<
Please check configuration file.
Section 'lcb->graph->app' at least one application should be defined.
Example:
\<lcb>
	\<graph>
		\<app class="org.decision.graphs.qsee.QseeApp"
			file="logic.xml" 
			name="Grp_name"
		/>
	...	
>>

appInstantiationException() ::= <<
Please check configuration file.
Section 'lcb->graph->app' class must extend org.decision.graphs.api.Application class
Example:
\<lcb>
	\<graph>
		\<app class="org.decision.graphs.qsee.QseeApp"
			file="logic.xml" 
			name="Grp_name"
		/>
	...	
>>

DoubleMatch(t1,t2,node)  ::= <<
Warning: Two or more templates match the same node. The first template will be used.
Check your templates and look for nodes with the same flow templates.
Template 1
		<t1>
Template 2
		<t2>
>>

defNotFound(stack,node)  ::= <<
Warning: Node template not found. See printed code for additional information.
>>

mainTemplateNotFound()  ::= <<
Error: Please check your configuration file. The main template is missing, check in attribute
CFG Example:
  ...
  \<main-template
		in ="D:/lcb/main-tamplate.java"
		out="D:/lcb/main.java"
	/>
	...
	
The minimun content in the main template are these tags:
	$statements$
	$methods$	
>>


cmdHelp()  ::= <<
Configuration file not found.

You can create lcb-conf.xml file in the current directory:
	java -jar lcb.jar

or specify the configuration file using:
	java -jar lcb.jar "projectfile.xml"

>>
