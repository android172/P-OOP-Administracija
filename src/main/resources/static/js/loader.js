//var th = [];
//var hasHeaders = false;
var hasData = false;

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

	table.innerHTML = "";

	if(rows>0){
	    table.appendChild(header);
		for(var i=1; i<rows; i++){
			var row = table.insertRow();
			for(var j=0; j<cols; j++){
				var cell = row.insertCell();
				cell.innerHTML = data[i][j];
			}
			var delButton = row.insertCell();
			delButton.innerHTML = "Delete";
			delButton.className = "button-delete";
		    delButton.id = data[i][0];
			delButton.onclick = function(){
			    //console.log("deleting: "+this.id);
			    deleteRow(this.id);
			};
		}
	}
}

/*
    Salje zahtev
*/
function deleteRow(id){
    document.getElementById("sendframe").src = "/delete_student?token="+token+"&index_num="+id;
    document.getElementById("search").submit();
}

function deleteRow(id){
    document.getElementById("sendframe").src = "/delete_student?token="+token+"&index_num="+id;
    document.getElementById("search").submit();
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
		val = "all";
	selected.innerHTML = val;
	element.innerHTML="";
	element.appendChild(selected);
	element.form.submit();
	fillTable();
	//alert(val);
}