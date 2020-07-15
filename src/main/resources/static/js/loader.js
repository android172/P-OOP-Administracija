//var th = [];
//var hasHeaders = false;
var hasData = false;
var initFill = false;

function loadDataToTable(){
	if(!hasData){
		document.getElementById("search").submit();
		hasData = true;
	}else {
		fillTable();
	}
}

function fillTable(){
	if(!hasData || !initFill){
		initFill = true;
		return;
	}

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
    Salje zahtev za brisanje reda
*/
function deleteRow(id){
    document.getElementById("sendframe").src = "/delete_student?token="+token+"&index_num="+id;
    document.getElementById("search").submit();
}

var filters = [];

var filtersEventListener;

function initGetFilters(frameID){
	var frame = document.getElementById(frameID);
	filtersEventListener = frame.addEventListener("load", function(){
		getFilters(this);
	});
}

function getFilters(frame){
	var filtersStr = frame.contentWindow.document.body.children[0].innerHTML;
	frame.removeEventListener("load", filtersEventListener);
	filters = JSON.parse(filtersStr);
	populateFilters();
}