function showTable2(){
	var x,y;
	x = document.getElementById("table2");
	y = document.getElementById("table3");
	var b = document.getElementsByName("button4");
	switch(b){
		case b:
			x.style.display = "table";
			y.style.display = "none";
			break;
	}
}
function showTable3(){
	var x,y;
	x = document.getElementById("table2");
	y = document.getElementById("table3");
	var b = document.getElementsByName("button5");
	switch(b){
		case b:
			x.style.display = "none";
			y.style.display = "table";
			break;
	}
}
function showTable4(){
	var x,y;
	x = document.getElementById("table4");
	y = document.getElementById("table5");
	var b = document.getElementsByName("button1");
	switch(b){
		case b:
			x.style.display = "table";
			y.style.display = "none";
			break;
	}
}
function showTable5(){
	var x,y;
	x = document.getElementById("table4");
	y = document.getElementById("table5");
	var b = document.getElementsByName("button2");
	switch(b){
		case b:
			y.style.display = "table";
			x.style.display = "none";
			break;
	}
}