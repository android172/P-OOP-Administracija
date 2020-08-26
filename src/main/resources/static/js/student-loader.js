var subject_id = {};

function loadStudentData(studentData){
	var ime = studentData["firstName"];
	var prezime = studentData["lastName"];
	document.getElementById("Ime").innerHTML = ime;
	document.getElementById("Prezime").innerHTML = prezime;
	document.getElementById("Ime1").innerHTML = ime;
	document.getElementById("Prezime1").innerHTML = prezime;
	var jmbg = studentData["jmbg"];
	document.getElementById("JMBG").innerHTML = jmbg;
	var broj_indeksa = studentData.index["number"];
	document.getElementById("Broj_indeksa").innerHTML = broj_indeksa;
	//Name,lastname,index number and JMBG filled
	

    var smer =  studentData["majorname"];
	document.getElementById("Smer").innerHTML = smer;
	//Majorname filled
 	
	var godina_upisa = studentData.index["year"];
	var godina_studija = new Date();
	document.getElementById("Godina_studija").innerHTML = godina_studija.getFullYear();
	document.getElementById("Godina_upisa").innerHTML = godina_upisa;
	//Years filled
	
}

function statusBudget(budzetData){
	if(budzetData){
		document.getElementById("Status").innerHTML = 'Budzet';
	}
	else{
		document.getElementById("Status").innerHTML = 'Samofinansiranje';
	}
	//Status filled
}

function loadStudentSubjects(subjectData){
	var table4 = document.getElementById("table4");
	let table = '<table>'
	table += '<tr><th colspan = "6">Upisani predmeti</th></tr>'
	table += '<tr><th>Predmet</th><th>ID</th><th>Godina</th><th>ESPB</th><th>Profesor</th><th>Smer</th></tr>'
    for (let i = 0; i < subjectData.length; i++) {
        const subjectName = subjectData[i]["subjectName"];
        const Id =  subjectData[i]["subjectId"];
		const espb =  subjectData[i]["espb"];
		const year =  subjectData[i]["year"];
		const lecName =  subjectData[i]["lectName"];
		const majorName =  subjectData[i]["majorName"];
		subject_id[Id] = espb;
        table += `<tr><td>${subjectName}</td><td>${Id}</td><td>${year}</td><td>${espb}</td><td>${lecName}</td><td>${majorName}</td></tr>`
    }
    table += '</table>'
    table4.innerHTML = table;
}

function loadGrades(gradeData){
	var Prosek = 0;
	var ESPB = 0;
	for(let i = 0; i < gradeData.length; i++){
		if(gradeData[i]["grades"] > 5)
			Prosek += gradeData[i]["grade"];
		for(let j = 0; j < subject_id.length; j++)
			if(gradeData[i]["id_subject"] === subject_id[j])
				ESPB += subject_id[j].espb;
			
	}
	document.getElementById("Prosek").innerHTML = Prosek;
	document.getElementById("ESPB").innerHTML = ESPB;
}

function loadExams(examsData){
	var table5 = document.getElementById("table5");
	let table = '<table>'
	table += '<tr><th colspan = "3">Prijava ispita</th></tr>'
	table += '<tr><th>Ispitni rok</th><th>Rok za prijavu</th><th>Prijava</th></tr>'
    for (let i = 0; i < examsData.length; i++) {
        const name = examsData[i]["name"];
        const ap_end =  examsData[i]["application_end"];
        table += `<tr><td>${name}</td><td>${ap_end}</td><td><a onclick = "window.location.replace('/registrations')" id = "examsReg">Prijavi</a></td></tr>`
    }
    table += '</table>'
    table5.innerHTML = table;
}

function init(){
	makeRequest('/get_student',[['index',index]],function(studentData){loadStudentData(studentData);});
	makeRequest('/budget',[['index',index]],function(budzetData){statusBudget(budzetData);});
	makeRequest('/get_subjects_by_student',[['index',index]],function(subjectData){loadStudentSubjects(subjectData);});
	makeRequest('/get_attempts_info_for_index',[['index',index]],function(gradeData){loadGrades(gradeData);});
	makeRequest('/get_exam_deadlines',[['student',index]],function(examsData){loadExams(examsData);});
}