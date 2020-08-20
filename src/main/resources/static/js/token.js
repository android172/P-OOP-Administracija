var token = -1;
var index = -1;

function getToken(){
	token = getCookie("token");
}

function getIndex(){
    index = getCookie("index");
}

function displayUsername(id="user-name"){
  username = getCookie("username");
  var elem = document.getElementById(id);
  elem.innerHTML = username;
  elem.value = username;
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

/*function addTokenToLinks(){
    var links = document.getElementsByClassName("tokenify");
    var len = links.length;
    for(var i=0; i<len; i++){
    	links[i].href+="?token="+token;
    }
}*/


/*function makeRequest(requestStr, frameID, params=[], oncomplete=function(){}){
  var frame = document.getElementById(frameID);
  var str = requestStr;
  str += "?token="+token;
  var len = params.length;
  for(var i=0; i<len; i++)
    str+="&"+params[i][0]+"="+params[i][1];
  console.log(str);
  //alert(oncomplete);
  var eventListener = function(){
    oncomplete();
    this.removeEventListener('load', eventListener);
  };
  frame.addEventListener('load', eventListener);
  frame.src = str;
}*/

function makeRequest(requestStr, params=[], oncomplete=function(){}){
  var xhr = new XMLHttpRequest();
  var str = requestStr;
  str += "?token="+token;
  var len = params.length;
  for(var i=0; i<len; i++)
    str+="&"+params[i][0]+"="+params[i][1];
  console.log("[query]: "+str);
  //alert(oncomplete);
  xhr.open("POST", str, true);
  //xhr.setRequestHeader('Content-Type', 'application/json');
  var eventListener = function(){
    var dataStr = this.responseText;
    var parsed = false;
    var data;
    try {
      data = JSON.parse(dataStr);
      parsed = true;
    } catch(e) { 
      console.log("Couldn't parse: \n"+dataStr);
    }
    if(parsed) 
      oncomplete(data);
    else 
      oncomplete([]);
    console.log("[response]: "+dataStr);
    this.removeEventListener('load', eventListener);
  };
  xhr.addEventListener('load', eventListener);
  xhr.send();
}
