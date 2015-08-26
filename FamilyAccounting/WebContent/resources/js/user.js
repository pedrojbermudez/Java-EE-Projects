/**
 * 
 */

function checkPassword() {
	var pass = document.getElementById("password").value;
	var rePass = document.getElementById("repeatPassword").value;
	var button = document.getElementById("buttonSE");
	if (document.getElementById("oldPassword") == null) {
		var md5Pass = CryptoJS.MD5(pass);
		var md5RePass = CryptoJS.MD5(rePass);
		if (CryptoJS.MD5(pass).toString() == CryptoJS.MD5(rePass).toString() && pass.value != ""
				&& rePass.value != "") {
			button.removeAttribute('disabled');
		} else {
			button.setAttribute('disabled', 'disabled');
		}
	} else {
		var oldPass = document.getElementById("oldPassword").value;
		var oldPassInput = document.getElementById("oldPasswordInput").value;
		if (CryptoJS.MD5(oldPass) == CryptoJS.MD5(oldPassInput)
				&& CryptoJS.MD5(pass) == CryptoJS.MD5(rePass)
				&& pass.value != "" && rePass.value != "") {
			button.removeAttribute('disabled');
		} else {
			button.setAttribute('disabled', 'disabled');
		}
	}
}

function deleteUser(id){
	var form = document.createElement("form");
	form.setAttribute("action", "user"); 
	form.setAttribute("method","POST"); 
	form.setAttribute("style", "display:none");
	var i = document.createElement("input");
	i.setAttribute("type", "hidden");
	i.setAttribute("name", "mode");
	i.setAttribute("value", "2");
	form.appendChild(i);
	document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
}