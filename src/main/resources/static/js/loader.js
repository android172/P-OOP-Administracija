//var th = [];
//var hasHeaders = false;
var hasData = false;

/*function setHeaders(){
	var table = document.getElementById("data");

	var header = table.rows[0].cells;
	var len = header.length;
	//alert(len);

	for(var i=0; i<len; i++){
		//alert(header[i].innerHTML);
		th.push(header[i].innerHTML);
	}
	hasHeaders = true;
}*/

function loadDataToTable(){
	if(!hasData){
		document.getElementById("search").submit();
		hasData = true;
	}else {
		fillTable();
	}
}

function fillTable(){
	if(!hasData)
		return;

	var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	//alert(data);
	var table = document.getElementById("data");
	var header = document.createElement("tr");

	var data = JSON.parse(dataStr);
    var rows = data.length;
    var cols = data[0].length;

	for(var i=0; i<cols; i++){
		var head = document.createElement("th");
		head.innerHTML = data[0][i];
		header.appendChild(head);
	}

	/*var lastColumn = document.createElement("th");
	lastColumn.innerHTML = document.getElementById("last_col").value;
	header.appendChild(lastColumn);*/

	table.innerHTML = "";

	if(rows>0){
	    table.appendChild(header);
		for(var i=1; i<rows; i++){
			var row = table.insertRow();
			for(var j=0; j<cols; j++){
				var cell = row.insertCell();
				cell.innerHTML = data[i][j];
			}
		}
	}
}

let expanded = false;

function initializeMultiSelect(){
	var multiSelect = document.getElementById("multiselect-1-area");
	multiSelect.addEventListener('click', function(e) {
		const checkboxes = document.getElementById("checkboxes");
		if (!expanded) {
			checkboxes.style.display = "block";
			expanded = true;
		} else {
			checkboxes.style.display = "none";
			expanded = false;
		}
		e.stopPropagation();
	}, true)

	var checkboxes = document.getElementById("checkboxes");
	checkboxes.addEventListener('click', function(e){
		e.stopPropagation();
	}, true)
}

document.addEventListener('click', function(e){
	if (expanded) {
		checkboxes.style.display = "none";
		expanded = false;
	}
}, false)

function showCheckboxes() {
  var checkboxes = document.getElementById("checkboxes");
  if (!expanded) {
    checkboxes.style.display = "block";
    expanded = true;
  } else {
    checkboxes.style.display = "none";
    expanded = false;
  }
}

function updateCheckboxValue(){
	var element=document.getElementById("multiselect-1");
	//alert(element.innerHTML)
	var checkboxes = document.getElementById("checkboxes");
	var options = checkboxes.children;
  	var len = options.length;
  	var val = "";
  	for(var i=0; i<len; i++){
  		if(options[i].children[0].checked){
  			val+=options[i].children[0].id;
  			if(i<len-1)
  				val+="+";
  		}
	}
	if(val[val.length-1]=="+")
		val=val.substr(0,val.length-1);

	//element.value = val;
	var selected = document.createElement('option');
	selected.value = val;
	if(val=="")
		val = "none";
	selected.innerHTML = val;
	element.innerHTML="";
	element.appendChild(selected);
	element.form.submit();
	fillTable();
	//alert(val);
}