
group methods;

method(node,procedure) ::= <<
<if(Description)>/*<Description>*/<endif>
private void <node>(<argsdef>)
{
  <procedure>
}>>

call(node) ::= <<
<node>(<argsval>); >>

