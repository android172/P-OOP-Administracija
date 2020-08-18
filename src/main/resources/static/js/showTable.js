function showTable4(){
	var x,y,z;
	x = document.getElementById("table4");
	y = document.getElementById("table5");
	z = document.getElementById("table6");
	var b = document.getElementsByName("button1");
	switch(b){
		case b:
			x.style.visibility = "visible";
			y.style.visibility = "hidden";
			z.style.visibility = "hidden";
			break;
	}
}
function showTable5(){
	var x,y,z;
	x = document.getElementById("table4");
	y = document.getElementById("table5");
	z = document.getElementById("table6");
	var b = document.getElementsByName("button2");
	switch(b){
		case b:
			y.style.visibility = "visible";
			x.style.visibility = "hidden";
			z.style.visibility = "hidden";
			break;
	}
}
function showTable6(){
	var x,y,z;
	x = document.getElementById("table4");
	y = document.getElementById("table5");
	z = document.getElementById("table6");
	var b = document.getElementsByName("button3");
	switch(b){
		case b:
			z.style.visibility = "visible";
			y.style.visibility = "hidden";
			x.style.visibility = "hidden";
			break;
	}
}
