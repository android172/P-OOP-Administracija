function init(){
	//makeRequest('/get_exams',[['student',index],['exam_deadline','Avgustovski']],function(examData){getExamId(examData);});
	//makeRequest('/get_subjects_by_student',[['index',index]],function(subjectData){loadStudentSubjects(subjectData);});
	makeRequest('/get_student',[['index',index]],function(studentData){loadStudentData(studentData);});
}
/*
let examSet = new Set();
var subject = {};
var exam_id = [];
var subject_to_show = [];
var exam_id_to_show = [];
var subject_id = [];

function loadStudentSubjects(subjectData){
	console.log("subject id");
	console.log(subject_id);
	var table4 = document.getElementById("tableExams");
	var subjectName;
	var lecName;
	var year;
	var espb;
	let table = '<table>'
	table += '<tr><th colspan = "6">Prijava ispita</th></tr>'
	table += '<tr><th>Predmet</th><th>ID</th><th>Godina</th><th>ESPB</th><th>Profesor</th><th>Prijavi ispit</th></tr>'
	for(let j = 0; j < subject_id.length; j++){
		for (let i = 0; i < subjectData.length; i++){
			let sub_id = subjectData[i]["subjectId"];
			console.log("id predmeta u foru")
			console.log(Id)
			if(sub_id === subject_id[j]){
				subjectName = subjectData[i]["subjectName"];
				subject[Id] = subjectName;
				espb =  subjectData[i]["espb"];
				year =  subjectData[i]["year"];
				lecName =  subjectData[i]["lectName"];
				table += `<tr><td>${subjectName}</td><td>${sub_id}</td><td>${year}</td><td>${espb}</td><td>${lecName}</td><td><input type="checkbox" onchange="examRegister('${Id}','${i}')" value="Prijavi"></td></tr>`
			}
		}
    }
    table += '</table>'
    table4.innerHTML = table;
}

function getExamId(examData){
	for(let i = 0; i < examData.length; i++){
		const Id = examData[i]["id"];
		const sub_id = examData[i]["subject_id"];
		exam_id[i] = Id;
		subject_id[i] = sub_id;
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

function applyForExam(){
for(let i = 0; i < exam_id.length; i++){
  var examId = exam_id[i];
  makeRequest("/apply_for_exam",[['student',index],['exam', examId],['payed', 0]]);
}
}
*/