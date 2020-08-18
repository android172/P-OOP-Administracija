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
var dataIdName = "index";
var dataColumnNum = 0;
var objIdOrder = 2;

function setDataType(type, idName, columnNum = 0, newObjIdOrder = 0){
	dataType = type;
	dataIdName = idName;
	dataColumnNum = columnNum;
	objIdOrder = newObjIdOrder;
	console.log(dataType + ", " + dataIdName + ", " + dataColumnNum + ", " + objIdOrder);
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
					row.id = data[i][dataColumnNum];
				}else{
					for(var j=0; j<len; j++){
						var cell = row.insertCell();
						cell.innerHTML = values[j];
					}
					row.id = values[dataColumnNum];
				}

				var delButton = document.createElement("div");
				delButton.innerHTML = "delete";
				delButton.className = "button-delete";
				if(customTable)
			    	delButton.id = data[i][dataColumnNum];
			    else
			    	delButton.id = values[dataColumnNum];
				delButton.addEventListener('click', function(e){
				    //console.log("deleting: "+this.id);
				    deleteRow(this.id);
				    e.stopPropagation();
				});
				row.appendChild(delButton);

				row.addEventListener('click', function(){
					makeRequest("/get_"+dataType,"sendframe",[[dataIdName, this.id]],function(){
						var elementDataStr = document.getElementById("sendframe").contentWindow.document.body.childNodes[0].innerHTML;
						console.log(elementDataStr);
						if(elementDataStr && elementDataStr!=""){
							var elementData = JSON.parse(elementDataStr);
							fillUpdateForm(elementData);
						}
					});
				});
			}
		}
	}
}

/*
    Salje zahtev za brisanje reda
*/
function deleteRow(id){
    makeRequest("/delete_"+dataType,"sendframe",[[dataIdName, id]], function(){
    	submitSearch();
    });
}

function fillUpdateForm(elementData){
	var form = document.getElementById("updateform");

	var keys = Object.keys(elementData);
	var values = Object.values(elementData);

	if(dataIdName == "index"){
		var index = Object.values(values[objIdOrder]);
		form.elements["new-index"].value = index[0];
		form.elements["year"].value = index[1];
		values[objIdOrder] = index[0]+"/"+index[1];
	}

	var len = values.length;
	for(var i=0; i<len; i++){
		
		if(i==objIdOrder && form.elements["new-"+keys[i]]){
			console.log(form.elements["new-"+keys[i]].name + "<- " + keys[i] + ":" + values[i]);
			form.elements["new-"+keys[i]].value = values[i];
		}
		else if(form.elements[keys[i]]){
			console.log(form.elements[keys[i]].name + "<- " + keys[i] + ":" + values[i]);
			form.elements[keys[i]].value = values[i];
		}
	}

	var inputId = document.getElementById("updated_id");
	inputId.name = dataIdName;
	inputId.value = values[objIdOrder];
	inputId.style.display = "none";

	toggleElement(form.parentNode.parentNode);
}
