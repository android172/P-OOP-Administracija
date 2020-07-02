var token = -1;

function getToken(){
	token = getCookie("token");
	//alert(token);
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

function addTokenToLinks(){
	var links = document.getElementsByClassName("tokenify");
	var len = links.length;
	for(var i=0; i<len; i++){
		links[i].href+="?token="+token;
	}
}