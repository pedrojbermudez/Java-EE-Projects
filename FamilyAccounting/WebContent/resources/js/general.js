/**
 * 
 */

function showEdit() {
	var editDivs = document.getElementsByClassName("div_edit");
	var deleteDivs = document.getElementsByClassName("div_delete");

	for (i = 0; i < editDivs.length; i++) {
		if (editDivs[i].style.display == "inline") {
			editDivs[i].style.display = "none";
			deleteDivs[i].style.display = "none";
		} else {
			editDivs[i].style.display = "inline";
			deleteDivs[i].style.display = "none";
		}
	}
}

function showDelete() {
	var editDivs = document.getElementsByClassName("div_edit");
	var deleteDivs = document.getElementsByClassName("div_delete");

	for (i = 0; i < deleteDivs.length; i++) {
		if (deleteDivs[i].style.display == "inline") {
			editDivs[i].style.display = "none";
			deleteDivs[i].style.display = "none";
		} else {
			deleteDivs[i].style.display = "inline";
			editDivs[i].style.display = "none";
		}
	}
}

function isNumber(element) {
	console.log(element.value);
	var regNumber = /^-?\d+\.?\d{0,2}?$/;
	var m = regNumber.exec(element.value);
	console.log(m);
	if (element.value.length > 0 && element.value != "-") {
		if (m == null) {
			window.alert("Incorrect number format.");
			document.getElementById("buttonSE").setAttribute('disabled',
					'disabled');
		} else if (element.value == "") {
			document.getElementById("buttonSE").setAttribute('disabled',
					'disabled');
		} else {
			document.getElementById("buttonSE").removeAttribute('disabled');
		}
	} else{
		document.getElementById("buttonSE").setAttribute('disabled',
		'disabled');
	}

}