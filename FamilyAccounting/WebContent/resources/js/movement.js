/**
 * 
 */

function deleteMovement(id, sourceId, income, outgoing){
	var form = document.createElement("form");
	form.setAttribute("action", "movements"); 
	form.setAttribute("method","POST"); 
	form.setAttribute("style", "display:none");
	var i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "movId");
	i.setAttribute("value", id);
	form.appendChild(i);
	i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "mode");
	i.setAttribute("value", "2");
	form.appendChild(i);
	i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "sourceId");
	i.setAttribute("value", sourceId);
	form.appendChild(i);
	form.appendChild(i);
	i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "oldIncome");
	i.setAttribute("value", income);
	form.appendChild(i);
	i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "oldOutgoing");
	i.setAttribute("value", outgoing);
	form.appendChild(i);
	document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
}