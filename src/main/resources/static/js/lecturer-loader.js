function loadLecturerData(lecturerData){
	console.log(lecturerData);
	var ime = lecturerData["firstName"];
	var prezime = lecturerData["lastName"];
	document.getElementById("Ime").innerHTML = ime;
	document.getElementById("Prezime").innerHTML = prezime;
	document.getElementById("Ime1").innerHTML = ime;
	document.getElementById("Prezime1").innerHTML = prezime;
	var title = lecturerData["title"];
	document.getElementById("Titula").innerHTML = title;
}
function loadLecturerSubjects(lecturerSubData){
	console.log(lecturerSubData);
}