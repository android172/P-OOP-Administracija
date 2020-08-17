var hasData = false;

function submitSearch(){
	document.getElementById("search").submit();
	hasData = true;
}

var oData = [];

function parseData(){
	if(hasData){
		var dataStr = document.getElementById("dataframe").contentWindow.document.body.childNodes[0].innerHTML;
		oData = JSON.parse(dataStr);
		//alert(oData);
		fillTables();
	}
}

var asc = true;

function changeOrder(newAsc){
	asc = newAsc;
	fillTables();
}

function fillTables(){
	var data = [];

	var adminTable = document.getElementById("data-admin");
	var lectTable = document.getElementById("data-lecturer");
	var studentTable = document.getElementById("data-student");
	var terms = document.getElementById("searchbar").value.toLowerCase();
	var header = document.createElement("tr");

	if(asc)
			data = oData;
		else{
			data = [...oData.reverse()];
			oData.reverse();
		}

    var rows = data.length;

	var head = document.createElement("th");
	head.innerHTML = "Korisnik";
	header.appendChild(head);
	head = document.createElement("th");
	head.innerHTML = "ID";
	header.appendChild(head);

	adminTable.innerHTML = "";
	lectTable.innerHTML = "";
	studentTable.innerHTML = "";

	adminTable.appendChild(header.cloneNode(true));
	lectTable.appendChild(header.cloneNode(true));
	studentTable.appendChild(header);

	if(rows>0){
		for(var i=0; i<rows; i++){
			var match = false;
			var values = Object.values(data[i]);
			var len = values.length;

			for(var j=0; j<len; j++){
				if(values[j] && values[j].toLowerCase().search(terms) != -1)
					match = true;
			}
			if(match){
				var row = document.createElement("tr");

				var cell = row.insertCell();
				cell.innerHTML = values[0];
				cell = row.insertCell();
				cell.innerHTML = values[3];

				var role = values[2];
				switch(role){
					case "Admin":
					adminTable.appendChild(row);
					break;
					case "Lecturer":
					lectTable.appendChild(row);
					break;
					case "Student":
					studentTable.appendChild(row);
					break;
					default:
					console.log("[user-loader.js] Role not found. " + "("+role+") "+ values[0]);
					break;
				}
			}
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
	if(newOptions == null)
		return;
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