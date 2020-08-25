function loadLecturerData(lecturerData){
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
	var table2 = document.getElementById("table2");
	let table = '<table>'
	table += '<tr><th colspan = "6">Upisani predmeti</th></tr>'
	table += '<tr><th>Predmet</th><th>IDPredmeta</th><th>Godina</th><th>ESPB</th><th>Smer</th></tr>'
    for (let i = 0; i < lecturerSubData.length; i++) {
        const subjectName = lecturerSubData[i]["subjectName"];
        const Id =  lecturerSubData[i]["subjectId"];
		const espb =  lecturerSubData[i]["espb"];
		const year =  lecturerSubData[i]["year"];
		const majorName =  lecturerSubData[i]["majorName"];
        table += `<tr><td>${subjectName}</td><td>${Id}</td><td>${year}</td><td>${espb}</td><td>${majorName}</td></tr>`
    }
    table += '</table>'
    table2.innerHTML = table;
}

function loadExamDeadline(examData){
	var table3 = document.getElementById("table3");
	let table = '<table>'
	table += '<tr><th colspan = "3">Ispitni rokovi</th></tr>'
	table += '<tr><th>Predmet</th><th>IDIspita</th><th>Datum</th></tr>'
    for (let i = 0; i < examData.length; i++) {
        const subjectName = examData[i]["subjectName"];
        const Id =  examData[i]["id"];
		const date =  examData[i]["date"];
        table += `<tr><td>${subjectName}</td><td>${Id}</td><td>${date}</td></tr>`
    }
    table += '</table>'
    table3.innerHTML = table;
}