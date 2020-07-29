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
		oData = JSON.parse(dataStr);
		//alert(oData);
		fillTable();
	}
}

function fillTable(asc=true){
	var data = [];

	var table = document.getElementById("data");
	var header = document.createElement("tr");

	if(customTable){
		if(asc)
			data = oData.slice(1);
		else
			data = oData.slice(1).reverse();

		var headerData = oData[0];
	}else{
		if(asc)
			data = oData;
		else
			data = oData.reverse();

		var headerData = Object.keys(data[0]);
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

				var values = Object.values(data[i]);
				var len = values.length;

				for(var j=0; j<len; j++){
					var cell = row.insertCell();
					cell.innerHTML = values[j];
				}
				row.id = values[0];
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

function addOptionsToFilter(filterID, newOptions, multiselect, numExistingOptions, separate){
	var filterElem = document.getElementById(filterID);
	var i=0;
	var len = newOptions.length;

	while(i<len){
		var optionName, optionValue;
		var id = filterID+"-"+(i+numExistingOptions);
		if(separate){
			optData = newOptions[i].split("-");
			optionName = optData[0];
			optionValue = optData[1];
		}else{
			optionValue = optionName = newOptions[i];
		}

		var newOpt;

		if(multiselect){
			newOpt = document.createElement('div')
			newOpt.className = "flex-row";
			var label = document.createElement('label');

			label.htmlFor = id;
			label.innerHTML = optionName;
			newOpt.appendChild(label);
			var input = document.createElement('input');
			input.type = "checkbox";
			input.id = id;
			input.value = optionValue;
			newOpt.appendChild(input);
		}else {
			newOpt = document.createElement('option');
			newOpt.value = optionValue;
			newOpt.innerHTML = optionName;
		}

		filterElem.appendChild(newOpt);

		i++;
	}
}

function getFilters(frame){
	var filtersStr = frame.contentWindow.document.body.children[0].innerHTML;
	frame.removeEventListener("load", filtersEventListener);
	filters = JSON.parse(filtersStr);
	populateFilters();
}