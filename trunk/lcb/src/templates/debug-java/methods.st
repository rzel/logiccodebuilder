
group methods;

	method(node,procedure) ::= <<
private void <node>()
{
	<procedure>
}
	>>

	call(node) ::= <<
System.out.println("call <node>");
<node>(); >>