var initialized = false;

function redirect(){
	if(initialized){
		var dataStr = document.getElementById("loginframe").contentWindow.document.body.innerHTML;
		//alert(dataStr);

		if(dataStr=="access denied"){
			//Bad login
			var badlogin = document.getElementById("badlogin");
			badlogin.style.display = "block";
			badlogin.classList.remove("shake");
			void badlogin.offsetWidth;
			badlogin.classList.add("shake");
		}
		else {
			var data = dataStr.split(':');
			var role = data[0].toLowerCase();
			var token = data[1];
			var index = data[2];

			var date = new Date();
			date.setHours(date.getHours+6);

			document.cookie = "token="+token+";expires="+date.toUTCString()+";SameSite=Lax";
			document.cookie = "index="+index+";expires="+date.toUTCString()+";SameSite=Lax";
			document.cookie = "role="+role+";expires="+date.toUTCString()+";SameSite=Lax";

			username = document.getElementById("username").value;
			document.cookie = "username="+username+";expires="+date.toUTCString()+";SameSite=Lax";

			window.location.replace("/"+role+"?token="+token);
		}
	}
	initialized = true;
}


window.onload = function(){
	getToken();
	
	if(isNaN(token))
		return;

	if(token != "") {
		makeRequest("/access_allowed","sendframe");
	}
}

var autoInit = false;

function autoRedirect(){
	if(autoInit){
		var response = document.getElementById("sendframe").contentWindow.document.body.children[0].innerHTML;
		if(response == "true"){
			var role = getCookie("role");
			window.location.replace("/"+role+"?token="+token);
		}
	}
	autoInit = true;
}
