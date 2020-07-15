var token = -1;
var index = -1;

function getToken(){
	token = getCookie("token");
}

function getIndex(){
    index = getCookie("index");
}

function setIndex(newIndex){
    var date = new Date();
    date.setHours(date.getHours+6);
    index = newIndex;
    document.cookie = "index="+newIndex+";expires="+date.toUTCString()+";SameSite=Lax";
}

function getCookie(cname) {
  	var name = cname + "=";
  	var ca = document.cookie.split(';');
  	for(var i = 0; i < ca.length; i++) {
    	var c = ca[i];
    	while (c.charAt(0) == ' ') {
      		c = c.substring(1);
    	}
    	if (c.indexOf(name) == 0) {
     	 	return c.substring(name.length, c.length);
    	}
  	}
  	return "";
}

function addTokenToForm(formID){
    var inputEl = document.createElement("input");
    inputEl.name = "token";
    inputEl.value = token;
    inputEl.style.display = "none";
    document.getElementById(formID).appendChild(inputEl);
}

function addTokenToLinks(){
	var links = document.getElementsByClassName("tokenify");
	var len = links.length;
	for(var i=0; i<len; i++){
		links[i].href+="?token="+token;
	}
}