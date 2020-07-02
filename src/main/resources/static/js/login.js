var initialized = false;

function redirect(){
	if(initialized){
		var dataStr = document.getElementById("loginframe").contentWindow.document.body.innerHTML;
		//alert(dataStr);
		if(dataStr=="<pre></pre>"){
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

			var date = new Date();
			date.setDate(date.getDate()+7);
			document.cookie = "token="+token+";expires="+date.toUTCString()+";SameSite=Lax";

			window.location.replace("/"+role+"?token="+token);	
		}
	}
	initialized = true;
}