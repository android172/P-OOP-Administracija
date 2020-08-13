var studentData = [];

function parseData(){
	var dataSt = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	studentData = JSON.parse(dataSt);
	//var examSt = document.getElementById("examsframe").contentWindow.document.body.childNodes[0].innerHTML;
	//examsData = JSON.parse(examSt);
	fillTables();
	//fillTable4();
}

function fillTables(){
	
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
	//const prosek =  giveMe(examsData,""); dok ne unesem jos ejdan ifream
	//Majorname and GPA filled
 	
	var godina_upisa = studentData.index["year"];
	var godina_studija = new Date();
	document.getElementById("Godina_studija").innerHTML = godina_studija.getFullYear();
	document.getElementById("Godina_upisa").innerHTML = godina_upisa;
	//const ESPB =  giveMe(examsData,""); dok ne unesem jos jedan ifream
	//Years and ESPB filled
	
}

function fillTable4(){
	
}
