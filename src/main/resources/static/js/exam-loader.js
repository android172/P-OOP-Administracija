var oData = [];

function parseDataToTable(tableID, custom){
	var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	if(dataStr!="")
		oData = JSON.parse(dataStr);
	else
		oData = [];
	//alert(dataStr + " | " + oData);

	if(custom)
		fillTableCustom(tableID);
	else
		fillTable(tableID);

}

function fillTable(tableID){
	var data = [];

	var table = document.getElementById(tableID);
	var header = document.createElement("tr");

	data = oData;

    var rows = data.length;
    var heads = Object.keys(data[0]);
    var cols = heads.length;

    var head;
    for(var i=0; i<cols; i++){
		head = document.createElement("th");
		head.innerHTML = heads[i];
		header.appendChild(head);
	}

	table.innerHTML = "";
	table.appendChild(header);

	for(var i=0; i<rows; i++){
		var row = document.createElement("tr");

		var values = Object.values(data[i]);
		var len = values.length;

		var cell;
		for(var j=0; j<len; j++){
		 	cell = row.insertCell();
			cell.innerHTML = values[j];
		}
		table.appendChild(row);
		/*var delButton = document.createElement("div");
		delButton.innerHTML = "delete";
		delButton.className = "button-delete";
	    delButton.id = data[i][0];
		delButton.onclick = function(){
		    //console.log("deleting: "+this.id);
		    deleteRow(this.id);
		};
		row.appendChild(delButton);*/
	}
}

function fillTableCustom(tableID){
	var data = [];

	var table = document.getElementById(tableID);
	var header = document.createElement("tr");

	data = oData;

    var rows = data.length;

	var head = document.createElement("th");
	head.innerHTML = "Ispitni rok:";
	header.appendChild(head);

	table.innerHTML = "";
	table.appendChild(header);

	for(var i=0; i<rows; i++){
		var row = document.createElement("tr");

		var cell = row.insertCell();
		cell.innerHTML = data[i];

		table.appendChild(row);
		/*var delButton = document.createElement("div");
		delButton.innerHTML = "delete";
		delButton.className = "button-delete";
	    delButton.id = data[i][0];
		delButton.onclick = function(){
		    //console.log("deleting: "+this.id);
		    deleteRow(this.id);
		};
		row.appendChild(delButton);*/
	}
}
