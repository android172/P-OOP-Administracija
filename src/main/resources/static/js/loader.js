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
		try{
			var dataStr = document.getElementById("@dataframe").contentWindow.document.body.childNodes[0].innerHTML;
			oData = JSON.parse(dataStr);
			if(customTable && oData.length == 3)
				oData = [];
		}catch(e){
			console.log("Couldn't parse: " + dataStr);
			oData = [];
		}
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
	if(oData.length==0){
		var table = document.getElementById("data");
		table.innerHTML  = "<tr><th>Nije pronađen nijedan rezultat!</th></tr>";
		return;
	}

	var data = [];

	var table = document.getElementById("data");
	var terms = format(document.getElementById("searchbar").value);
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
					if(format(data[i][j]).search(terms) != -1)
						match = true;
				}
			}else{
				var values = Object.values(data[i]);
				var len = values.length;
				for(var j=0; j<len; j++){
					//console.log(values[j]);
					if(!Array.isArray(values[j]) && (format(""+values[j])).search(terms) != -1)
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
					makeRequest("/get_"+dataType,[[dataIdName, this.id]],function(data){
						elementData = data;
						var form = document.getElementById("updateform");
						fillUpdateForm(form);
						toggleElement(form.parentNode.parentNode);
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
	yesNoDialog("Are you sure you want to delete "+dataType+" " + id + "?", id, function(param){
		makeRequest("/delete_"+dataType,[[dataIdName, param]], function(){
    		submitSearch();
    });});
}

var elementData;

function fillUpdateForm(form){

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
}

function loaderInit(){
	var updateform = document.getElementById("updateform");
	updateform.onsubmit = function(){
		toggleElement(updateform.parentNode.parentNode);
		var sendframe = document.getElementById("@sendframe");
		var refresh = function(){
			submitSearch();
			sendframe.removeEventListener('load', refresh);
		}
		sendframe.addEventListener('load', refresh);
	}

	updateform.onreset = function() {
		setTimeout(	function(){
			fillUpdateForm(updateform);
		}, 1);
	}

	var newform = document.getElementById("newform");
	newform.onsubmit = function(){
		toggleElement(newform.parentNode.parentNode);
		var sendframe = document.getElementById("@sendframe");
		var refresh = function(){
			submitSearch();
			sendframe.removeEventListener('load', refresh);
		}
		sendframe.addEventListener('load', refresh);
	}
}