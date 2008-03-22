group maincode;

	if(node,yes,no) ::= <<
System.out.println("<node>:"+(<node>));
if(<node>)
{
	<yes>
}
else
{
	<no>
}
>>

	statement(node,n) ::= <<
<node><if(Description)><Description><endif>
<n> >>

	statement2(node) ::= <<
<node><if(Description)><Description><endif> >>

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

	error(node,errors_flow) ::= <<
Template not found!!
<node>
<errors_flow>
	>>

	error_flow(flow_id,flow) ::= <<
ERROR: Check flow "<flow_id>"
{
	<flow>
}
	>>
