function init(){
	makeRequest('/get_subjects_by_student',[['index',index]],function(subjectData){loadStudentSubjects(subjectData);});
	makeRequest('/get_attempts_info_for_index',[['index',index]],function(gradeData){loadGrades(gradeData);});
	makeRequest('/get_student',[['index',index]],function(studentData){loadStudentData(studentData);});
	makeRequest('/get_exams_for',[['student',index]],function(examData){getExamId(examData);});

}

var subjects_id = [];
let examSet = new Set();
var subject = {};
var exam_id = [];

function loadStudentSubjects(subjectData){
	var table4 = document.getElementById("tableExams");
	var subjectName;
	var lecName;
	var year;
	var espb;
	var Id;
	let table = '<table>'
	table += '<tr><th colspan = "6">Prijava ispita</th></tr>'
	table += '<tr><th>Predmet</th><th>ID</th><th>Godina</th><th>ESPB</th><th>Profesor</th><th>Prijavi ispit</th></tr>'
    for (let i = 0; i < subjectData.length; i++) {
		if(subjectData[i]["subjectId"] === subjects_id[i]){
			subjectName = subjectData[i]["subjectName"];
			Id =  subjectData[i]["subjectId"];
			subject[Id] = subjectName;
			espb =  subjectData[i]["espb"];
			year =  subjectData[i]["year"];
			lecName =  subjectData[i]["lectName"];
		}
        table += `<tr><td>${subjectName}</td><td>${Id}</td><td>${year}</td><td>${espb}</td><td>${lecName}</td><td><input type="checkbox" onchange="examRegister('${Id}','${i}')" value="Prijavi"></td></tr>`
    }
    table += '</table>'
    table4.innerHTML = table;
}

function getExamId(examData){
	console.log(examData);
	/*for(let i = 0; i < examData.length; i++)
		exam_id[i] = examData[i][""];*/
}

function loadGrades(gradeData){
	for(let i = 0; i < gradeData.length; i++){
		var subject_id = gradeData[i]["id_subject"];
		if(gradeData[i]["grade"] <= 5)
			subjects_id[i] = subject_id;
	}
}

function loadStudentData(studentData){
	var ime = studentData["firstName"];
	var prezime = studentData["lastName"];
	document.getElementById("Ime").innerHTML = ime;
	document.getElementById("Prezime").innerHTML = prezime;
}

function examRegister(Id,i){
    if(! examSet.has(Id)){
        examSet.add(Id);
    }
    else{
        examSet.delete(Id);
    }

    showExamSet();
}

function showExamSet(){
	let tableReg = document.getElementById('tableRegistered');
	var subject_name;
    let table = '<table>'
	table += '<tr><th>Predmet</th><th>ID</th></tr>'
    for (const item of examSet) {
		for(let i = 0; i < subject.length; i++)
			if(subject[i] === item)
				subject_name = subject[i].subjectName;
        table += `<tr><td>${subject_name}</td><td>${item}</td></tr>`
    }
    table += '</table>';
    if(examSet.size === 0)
        table = '';
    tableReg.innerHTML = table;
}

/*function applyForExam(){
for(let i = 0; i < exam_id.length; i++){
  var examId = exam_id[i];
  makeRequest("/apply_for_exam",[['student',index],['exam', examId],['payed', 0]]);
}
}*/