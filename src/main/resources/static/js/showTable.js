function showTable2(){
	var x,y;
	x = document.getElementById("table2");
	y = document.getElementById("table3");
	var b = document.getElementsByName("button4");
	switch(b){
		case b:
			x.style.visibility = "visible";
			y.style.visibility = "hidden";
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
			x.style.visibility = "hidden";
			y.style.visibility = "visible";
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
			x.style.visibility = "visible";
			y.style.visibility = "hidden";
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
			y.style.visibility = "visible";
			x.style.visibility = "hidden";
			break;
	}
}