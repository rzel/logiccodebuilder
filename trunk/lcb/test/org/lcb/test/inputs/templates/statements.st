group maincode;

	if(node,yes,no) ::= <<
if(<node>)
{
	<yes>
}
else
{
	<no>
}>>

	ifyes(node,yes) ::= <<
if(<node>)
{
	<yes>
}
>>

	ifno(node,no) ::= <<
if(!(<node>))
{
	<no>
}
>>


	statement(node,n) ::= <<
<node>
<n> >>

	statement2(node) ::= <<
<node> >>
	
	loop(node, body, exit) ::= <<
for(;<node>;)
{
	<body>
}
<exit> >>

	for(node, init, count, body, exit) ::= <<
for(<init>; <node>; <count>)
{
	<body>
}
<exit> >>

	error(node,flows) ::= <<
Template not found. Check flows!!
	Node: <node>			
	<flows>
	>>
	
	error_flow(flow_id,flow) ::= <<
Flow: "<flow_id>"
	<flow>

	>>
