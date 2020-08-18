var oData = [];

function parseDataToTable(tableID, custom, data){
	oData = data;
	if(custom)
		fillTableDeadlines(tableID);
	else
		fillTableExams(tableID);
}

function fillTableExams(tableID){
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
		    makeRequest("/delete_exam","sendframe",[["exam_id", this.id]], function(){
		    	makeRequest('/get_all_exams','dataframe',[],function() {
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

function fillTableDeadlines(tableID){
	var data = [];

	var table = document.getElementById(tableID);
	var header = document.createElement("tr");

	data = oData;

    var rows = data.length;

	var head = document.createElement("th");
	head.innerHTML = "Ispitni rok";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "Početak";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "Kraj";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "Početak prijava";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "Kraj prijava";
	header.appendChild(head);

	table.innerHTML = "";
	table.appendChild(header);

	for(var i=0; i<rows; i++){
		var row = document.createElement("tr");

		var values = Object.values(data[i]);
		var cols = values.length;

		for(var j=0; j<cols; j++){
			var cell = row.insertCell();
			cell.innerHTML = values[j];
		}

		var delButton = document.createElement("div");
		delButton.innerHTML = "delete";
		delButton.className = "button-delete";
	    delButton.id = values[0];
		delButton.onclick = function(){
		    makeRequest("/delete_exam_deadline","sendframe",[["name", this.id]], function(){
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
