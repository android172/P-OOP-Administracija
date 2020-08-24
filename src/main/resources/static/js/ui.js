function toggleElementById(id){
	var el = document.getElementById(id);
	toggleElement(el);
}

function format(str){
	var newStr = str.toLowerCase().replace(/ć/,'c').replace(/č/,'c').replace(/š/,'s').replace(/đ/,'dj').replace(/ž/,'z');
	//console.log(str+" -> "+newStr);
	return newStr;
}

var dds = [];

function initDropDowns(){
	dds = document.getElementsByClassName("dropdown");
	document.body.addEventListener('click', clearDropDowns, true);
	clearDropDowns();

	var i=0;
	var len = dds.length;
	while(i<len){
		let opts = dds[i].children[2];
		dds[i].addEventListener('click', function(){
			toggleElement(opts);
		}, true);

		let input = dds[i].children[0];
		let displayInput = dds[i].children[1];
		if(opts.children[0].children[0])
			displayInput.innerHTML = opts.children[0].children[0].innerHTML;
		else
			displayInput.innerHTML = opts.children[0].innerHTML;

		var j=0;
		let len2 = opts.children.length;
		if(dds[i].classList.contains("multiselect")){
			while(j<len2){
				let clicked = j;
				opts.children[j].children[1].checked = true;
				opts.children[j].children[1].addEventListener('change', function(){
					var str = "";
					var displayStr = "";
					var l=0;
					
					var all = opts.children[0].children[1].checked;
					if(clicked==0){
						if(all) {
							str="all+";
							displayStr="Svi, ";
						}
						else {
							str="+";
							displayStr="Nijedan, ";
						}
						for(l=1; l<len2; l++)
							opts.children[l].children[1].checked = all;
					}

					for(l=1; l<len2; l++){
						if(opts.children[l].children[1].checked && clicked!=0){
							str+=opts.children[l].children[1].value+"+";
							displayStr+=opts.children[l].children[0].innerHTML+", ";
						}
					}

					if(clicked!=0)
						opts.children[0].children[1].checked = false;

					str=str.substr(0,str.length-1);
					displayStr=displayStr.substr(0,displayStr.length-2);
					/*if(str=="")
						str="";*/
					if(displayStr=="")
						displayStr="Nijedan";
					input.value = str;
					displayInput.innerHTML = displayStr;
					input.form.submit();
				}, true);
				j++;
			}
		}else {
			if(!dds[i].classList.contains("no-auto-submit")){
				while(j<len2){
					opts.children[j].addEventListener('change', function(){
						input.value = this.value;
						displayInput.innerHTML = this.innerHTML;
						input.form.submit();
						toggleElement(opts);
					}, true)
					opts.children[j].addEventListener('click', function(){
						input.value = this.value;
						displayInput.innerHTML = this.innerHTML;
						input.form.submit();
						toggleElement(opts);
					}, true)
					j++;
				}
			}else{
				while(j<len2){
					opts.children[j].addEventListener('change', function(){
						input.value = this.value; 
						displayInput.innerHTML = this.innerHTML;
						toggleElement(opts);
					}, true)
					opts.children[j].addEventListener('click', function(){
						input.value = this.value;
						displayInput.innerHTML = this.innerHTML;
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
			dds[i].children[2].style.display = "none";
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

function getDisplayName(input){
	switch(input.toLowerCase()){
		case "firstname": case "name": case "subjectname": return "Ime";
		case "lastname": return "Prezime";
		case "title": return "Zvanje";
		case "lectid": case "lect_id": return "ID Predavača";
		case "id": return "ID";
		case "subjectid": case "subject_id": return "ID Predmeta";
		case "espb": return "ESPB";
		case "year": return "Godina";
		case "majorid": return "ID Smera";
		case "lectname": return "Predavač";
		case "majorname": return "Smer";
		case "points_required": return "Broj poena";
		case "date": return "Datum";
	}
	return input;
}

function createPopup(width, height){
	var div = document.createElement('div');
	div.className = 'popup';
	div.style = "width:"+width+"px; height:"+height+"px; margin-left:"+(-width/2)+"px; margin-top:"+(-height/2)+"px;";
	document.body.appendChild(div);
	return div;
}

function yesNoDialog(msg, param, yes=function(){}){
	var div = createPopup(250, 95);
	var text = document.createElement('p');
	text.innerHTML = msg;
	div.appendChild(text);

	var buttons = document.createElement('div');
	buttons.className = "dialog-buttons";
	var button = document.createElement('button');
	button.className = "dialog-button";
	button.innerHTML = "Yes";
	button.onclick = function(){
		div.style.display = "none";
		yes(param);
		setTimeout(function(){
			document.body.removeChild(div);
		}, 3000);
	}
	buttons.appendChild(button);
	button = document.createElement('button');
	button.className = "dialog-button";
	button.innerHTML = "No";
	button.onclick = function(){
		document.body.removeChild(div);
	}
	buttons.appendChild(button);
	div.appendChild(buttons);
}