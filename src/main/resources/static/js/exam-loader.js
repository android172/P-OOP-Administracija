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
    if(rows>0){
    	var heads = Object.keys(data[0]);
	    var cols = heads.length;

	    var head;
	    for(var i=0; i<cols; i++){
			head = document.createElement("th");
			head.innerHTML = getDisplayName(heads[i]);
			header.appendChild(head);
		}
	}else{
		var head = document.createElement("th");
		head.innerHTML = "Tabela je prazna.";
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

		var delButton = document.createElement("div");
		delButton.innerHTML = "delete";
		delButton.className = "button-delete";
	    delButton.id = values[0];
		delButton.onclick = function(){
		    makeRequest("/delete_deadline","sendframe",[["exam_id", this.id]], function(){
		    	makeRequest('/get_exams','dataframe',[],function() {
					parseDataToTable('exams', false);
				});
		    });
		};
		row.appendChild(delButton);
		table.appendChild(row);
	}

	var lastRow = document.createElement("tr");
	var cell = lastRow.insertCell();
	cell.innerHTML = "+";
	cell.colSpan = 4;
	cell.className = "add-row";
	cell.onclick = function(){
		toggleElementById("new-exam");
	}
	table.appendChild(lastRow);
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

		var delButton = document.createElement("div");
		delButton.innerHTML = "delete";
		delButton.className = "button-delete";
	    delButton.id = data[i];
		delButton.onclick = function(){
		    makeRequest("/delete_deadline","sendframe",[["name", this.id]], function(){
		    	makeRequest('/get_exam_deadlines','dataframe',[],function() {
					parseDataToTable('exam-deadlines', true);
				});
		    });
		};
		row.appendChild(delButton);

		table.appendChild(row);
	}

	var lastRow = document.createElement("tr");
	var cell = lastRow.insertCell();
	cell.innerHTML = "+";
	cell.colSpan = 5;
	cell.className = "add-row";
	cell.onclick = function(){
		toggleElementById("new-deadline");
	}
	table.appendChild(lastRow);
}
