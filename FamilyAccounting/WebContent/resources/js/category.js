/**
 * 
 */

function deleteCategory(id){
	var form = document.createElement("form");
	form.setAttribute("action", "categories"); 
	form.setAttribute("method","POST"); 
	form.setAttribute("style", "display:none");
	var i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "categoryId");
	i.setAttribute("value", id);
	form.appendChild(i);
	i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "mode");
	i.setAttribute("value", "2");
	form.appendChild(i);
	document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
}