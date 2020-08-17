var hasData = false;

function submitSearch(){
	document.getElementById("search").submit();
	hasData = true;
}

var customTable = false;
var oData = [];

function setCustomTable(value){
	customTable = value;
}

function parseData(){
	if(hasData){
		var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
		if(dataStr!=""){
			oData = JSON.parse(dataStr);
		}else{
			oData = [];
		}
		//alert(oData);
		fillTable();
	}
}

var asc = true;

function changeOrder(newAsc){
	asc = newAsc;
	fillTable();
}

var dataType = "student";
var dataIdName = "index_num";
var dataColumnNum = 0;

function setDataType(type, idName, columnNum = 0){
	dataType = type;
	dataIdName = idName;
	dataColumnNum = columnNum;
	console.log(dataType + ", " + dataIdName + ", " + dataColumnNum);
}

function fillTable(){
	var data = [];

	var table = document.getElementById("data");
	var terms = document.getElementById("searchbar").value.toLowerCase();
	var header = document.createElement("tr");

	var headerData;
	if(customTable){
		if(asc)
			data = oData.slice(1);
		else{
			data = oData.slice(1).reverse();
		}

		headerData = oData[0];
	}else{
		if(asc)
			data = oData;
		else{
			data = [...oData.reverse()];
			oData.reverse();
		}

		if(data.length > 0){
			headerData = Object.keys(data[0]);
			var len = headerData.length;
			for(var i=0; i<len; i++)
				headerData[i] = getDisplayName(headerData[i]);
		}
		else
			headerData = [];
	}

    var rows = data.length;
    var cols = headerData.length;

	for(var i=0; i<cols; i++){
		var head = document.createElement("th");
		head.innerHTML = headerData[i];
		header.appendChild(head);
	}

	table.innerHTML = "";

	if(rows>0){
	    table.appendChild(header);
		for(var i=0; i<rows; i++){

			var match = false;
			if(customTable){
				for(var j=0; j<cols; j++){
					if(data[i][j].toLowerCase().search(terms) != -1)
						match = true;
				}
			}else{
				var values = Object.values(data[i]);
				var len = values.length;
				for(var j=0; j<len; j++){
					//console.log(values[j]);
					if(!Array.isArray(values[j]) && (""+values[j]).toLowerCase().search(terms) != -1)
						match = true;
				}
			}
			if(match){
				var row = table.insertRow();

				if(customTable){
					for(var j=0; j<cols; j++){
						var cell = row.insertCell();
						cell.innerHTML = data[i][j];
					}
					row.id = data[i][0];
					/*row.onclick = function(){
						setCookie("index", this.id);
						window.location.replace("/edit_student?token="+token+"&index="+this.id);
					};*/
				}else{
					for(var j=0; j<len; j++){
						var cell = row.insertCell();
						cell.innerHTML = values[j];
					}
					row.id = values[0];
				}

				var delButton = document.createElement("div");
				delButton.innerHTML = "delete";
				delButton.className = "button-delete";
				if(customTable)
			    	delButton.id = data[i][dataColumnNum];
			    else
			    	delButton.id = values[dataColumnNum];
				delButton.onclick = function(){
				    //console.log("deleting: "+this.id);
				    deleteRow(this.id);
				};
				row.appendChild(delButton);
			}
		}
	}
}

/*
    Salje zahtev za brisanje reda
*/
function deleteRow(id){
	/*var requestStr = "/delete_"+dataType+"?token="+token+"&"+dataIdName+"="+id;
	console.log(requestStr)*/
    //document.getElementById("sendframe").src = requestStr;
    makeRequest("/delete_"+dataType,"sendframe",[[dataIdName, id]], function(){
    	submitSearch();
    });
}