/**
 * Sending the form to set a moderator user
 */
function setModUser(checkbox) {
	window.alert($(checkbox).prop('checked'));
	$.ajax({
		type : 'post',
		url : 'SetModUser',
		data : {
			user_id : $(checkbox).val(),
			is_mod : $(checkbox).prop('checked') == true ? "y" : "n"
		},
		success : function(responseText) {
			window.alert("es un exito");
		}
	});
}