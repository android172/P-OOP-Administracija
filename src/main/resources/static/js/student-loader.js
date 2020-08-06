var studentData;
var table1 = document.getElementById("table1");
var table2 = document.getElementById("table2");
var table3 = document.getElementById("table3");
var table4 = document.getElementById("table4");

function parseData(){
	var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	studentData = JSON.parse(dataStr);
}

function giveMe(studentData,tagName){
    return studentData.getElementsByTagName(tagName)[0].innerHTML;
}

function fillTables(){
	let table1_1 = '<table>'
	const indeks = giveMe(studentData,"index");
    const jmbg =  giveMe(studentData,"jmbg");
    table1_1 += `<tr><td>${indeks}</td><td>${jmbg}</td></tr>`
    table1_1 += '</table>'
    table1.innerHTML = table1_1;
	//Table 1 filled
	
	let table2_2 = '<table>'
	const studije = giveMe(studentData,"");
    const smer =  giveMe(studentData,"");
	const centar =  giveMe(studentData,"");
	const prosek =  giveMe(studentData,"");
    table2_2 += `<tr><td>${studije}</td><td>${smer}</td><td>${centar}</td><td>${prosek}</td></tr>`
    table2_2 += '</table>'
    table2.innerHTML = table2_2;
	//Table 2 filled
	
	let table3_3 = '<table>'
	const godina_upisa = giveMe(studentData,"");
    const godina_studija =  giveMe(studentData,"");
	const ESPB =  giveMe(studentData,"");
    table3_3 += `<tr><td>${godina_upisa}</td><td>${godina_studija}</td><td>${ESPB}</td>tr>`
    table3_3 += '</table>'
    table3.innerHTML = table3_3;
	//Table3 filled
	
	//Table4 filled
}