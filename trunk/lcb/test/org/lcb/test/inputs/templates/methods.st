
group methods;

	method(node,procedure) ::= <<
private void <node>(<argsdef>)
{				
	<procedure>
}
	>>
	
	call(node,procedure="") ::= <<
<node>(<argsval>); >>
