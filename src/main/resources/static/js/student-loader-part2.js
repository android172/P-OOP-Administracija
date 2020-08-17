function parseDataExams(){
	var examSt = document.getElementById("examsframe").contentWindow.document.body.childNodes[0].innerHTML;
	console.log(examsSt);
	if(examSt!=''){
	examsData = JSON.parse(examSt);
	fillTable();//filling table 4, ESPB and GPA
	}
}

function fillTable(){
	var Prosek = 0;
	var ESPB = 0;
}