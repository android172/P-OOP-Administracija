function parseDatapt2(){
	var examSt = document.getElementById("examsframe").contentWindow.document.body.childNodes[0].innerHTML;
	examsData = JSON.parse(examSt);
	console.log(examsData);
	fillTable();//filling table 4, ESPB and GPA
}

function fillTable(){
	var Prosek = 0;
	var ESPB = 0;
}