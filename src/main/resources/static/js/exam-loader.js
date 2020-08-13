var oData = [];

function parseDataToTable(tableID){
	var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
	if(dataStr!="")
		oData = JSON.parse(dataStr);
	else
		oData = [];

	fillTable(tableID);

}

function fillTable(tableID){
	var data = [];

	var table = document.getElementById(tableID);
	var header = document.createElement("tr");

	data = oData;

    var rows = data.length;

	var head = document.createElement("th");
	head.innerHTML = "Ispit";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "ID";
	header.appendChild(head);

	table.innerHTML = "";
	table.appendChild(header);

	for(var i=0; i<rows; i++){
		var row = document.createElement("tr");

		var values = Object.values(data[i]);
		var len = values.length;

		var cell;
		for(var j=0; j<len; j++)
		 	cell = row.insertCell();
			cell.innerHTML = values[j];
	}

	alert("7");

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

/*
    Salje zahtev za brisanje reda
*/
function deleteRow(id){
    document.getElementById("sendframe").src = "/delete_exam?token="+token+"&id="+id;
    document.getElementById("search").submit();
}
