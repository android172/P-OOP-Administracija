function toggleElement(id){
	var el = document.getElementById(id);

	if(el.style.display != "initial"){
	    el.style.display  = "initial";
	}else
	{
		el.style.display = "none";
	}
}