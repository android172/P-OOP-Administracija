function toggleElementById(id){
	var el = document.getElementById(id);

	if(el.style.display != "initial"){
	    el.style.display  = "initial";
	}else
	{
		el.style.display = "none";
	}
}

var dds = [];

function initDropDowns(){
	dds = document.getElementsByClassName("dropdown");
	document.body.addEventListener('click', clearDropDowns, true);
	clearDropDowns();

	var i=0;
	var len = dds.length;
	while(i<len){
		let opts = dds[i].children[1];
		dds[i].addEventListener('click', function(){
			toggleElement(opts);
		}, true);

		let input = dds[i].children[0];

		var j=0;
		let len2 = opts.children.length;
		if(dds[i].classList.contains("multiselect")){
			while(j<len2){
				let clicked = j;
				opts.children[j].children[1].addEventListener('change', function(){
					var str = "";
					var l=0;
					
					var all = opts.children[0].children[1].checked;
					if(clicked==0){
						if(all) 
							str="all+";
						else 
							str="none+";
						for(l=1; l<len2; l++)
							opts.children[l].children[1].checked = all;
					}

					for(l=1; l<len2; l++){
						if(opts.children[l].children[1].checked && clicked!=0)
							str+=opts.children[l].children[1].value+"+";
					}

					if(clicked!=0)
						opts.children[0].children[1].checked = false;

					str=str.substr(0,str.length-1);
					input.value = str;
					input.form.submit();
				}, true);
				j++;
			}
		}else {
			if(!dds[i].classList.contains("no-auto-submit")){
				while(j<len2){
					opts.children[j].addEventListener('change', function(){
						input.value = this.value;
						input.form.submit();
						toggleElement(opts);
					}, true)
					opts.children[j].addEventListener('click', function(){
						input.value = this.value;
						input.form.submit();
						toggleElement(opts);
					}, true)
					j++;
				}
			}else{
				while(j<len2){
					opts.children[j].addEventListener('change', function(){
						input.value = this.value;
						toggleElement(opts);
					}, true)
					opts.children[j].addEventListener('click', function(){
						input.value = this.value;
						toggleElement(opts);
					}, true)
					j++;
				}
			}
		}
		i++;
	}
}

function clearDropDowns(){
	var i=0;
	var len = dds.length;
	for(i=0; i<len; i++){
			dds[i].children[1].style.display = "none";
	}
}

function toggleElement(element){
	if (element.style.display!="initial"){
		element.style.display="initial";
	}
	else{
		element.style.display="none";
	}
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