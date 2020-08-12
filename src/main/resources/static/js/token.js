var token = -1;
var index = -1;

function getToken(){
	token = getCookie("token");
}

function getIndex(){
    index = getCookie("index");
}

function displayUsername(){
  username = getCookie("username");
  document.getElementById("user-name").innerHTML = username;
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


function makeRequest(requestStr, frameID, params=[]){
  var frame = document.getElementById(frameID);
  var str = requestStr;
  str += "?token="+token;
  var len = params.length;
  for(var i=0; i<len; i++){
    str+="&"+params[i][0]+"="+params[i][1];
  }
  frame.src = str;
}