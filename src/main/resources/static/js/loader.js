var hasData = false;

function loadDataToTable(){
	if(!hasData){
		document.getElementById("search").submit();
		hasData = true;
	}else {
		parseData();
	}
}

var oData = [];

function parseData(){
	var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	oData = JSON.parse(dataStr);
	fillTable();
}

function fillTable(asc=true){
	var data = [];
	if(asc)
		data = oData.slice(1);
	else
		data = oData.slice(1).reverse();

	var table = document.getElementById("data");
	var header = document.createElement("tr");

    var rows = data.length;
    var cols = data[0].length;

	for(var i=0; i<cols; i++){
		var head = document.createElement("th");
		head.innerHTML = oData[0][i];
		header.appendChild(head);
	}

	table.innerHTML = "";

	if(rows>0){
	    table.appendChild(header);
		for(var i=0; i<rows; i++){
			var row = table.insertRow();
			for(var j=0; j<cols; j++){
				var cell = row.insertCell();
				cell.innerHTML = data[i][j];
			}
			var delButton = document.createElement("div");
			delButton.innerHTML = "delete";
			delButton.className = "button-delete";
		    delButton.id = data[i][0];
			delButton.onclick = function(){
			    //console.log("deleting: "+this.id);
			    deleteRow(this.id);
			};
			row.appendChild(delButton);
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