Frequently Asked Questions
====

* ### How do I make MDroid work with our Moodle website?
  You can check the requirements here: [MDroid requirements](index.md#Requirements) and setup instruction here: [Moodle setup](moodle-setup.md)
  
  <br/>
  
* ### What is Paranoid login?
  Paranoid login is a unique way of login which helps you to link your Moodle account with MDroid without entering your <b>precious password</b>.
  
  <br/>
  
* ### How do I make use of Paranoid login?
  Paranoid login uses a unique token generated for your account by your Moodle site. Fill the form below and open generated token url.
  
  <script>
    function genTokenUrl() {
	    var username = document.getElementById("username").value;
	    var password = document.getElementById("password").value;
	    var url = document.getElementById("url").value;
	    var service = document.getElementById("service").value;
	    document.getElementById("tokenurl").innerHTML = url + "/login/token.php?" + "username=" + username + "&password=" + password + "&service=" + service;
    }

    function openTokenUrl(){
	    var url = document.getElementById("tokenurl").textContent;
	    var win = window.open(url, '_blank');
	    win.focus();
    }
  </script>
  
  <div class="alert alert-success col-sm-11"><b>Tip:</b> If you are truely paranoid, Please fill form with test data and build your url from that.</div>
    
  <form class="form-horizontal" role="form">
     <div class="form-group">
        <label for="username" class="col-sm-2 control-label">Username</label>
        <div class="col-sm-3">
           <input type="text" class="form-control" id="username" 
              placeholder="Enter Account Username">
        </div>
     </div>
     <div class="form-group">
        <label for="password" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-3">
           <input type="text" class="form-control" id="password" 
              placeholder="Enter Account Password">
        </div>
     </div>
     <div class="form-group">
        <label for="url" class="col-sm-2 control-label">Moodle url</label>
        <div class="col-sm-3">
           <input type="text" class="form-control" id="url" 
              placeholder="Enter Moodle site url">
        </div>
     </div>
     <div class="form-group">
        <label for="service" class="col-sm-2 control-label">Service name</label>
        <div class="col-sm-3">
		      <select class="form-control" id="service">
		         <option>mdroid_service</option>
		         <option>moody_service</option>
		         <option>moodle_mobile_app</option>
		      </select>
		    </div>
     </div>
        
     <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10" onclick="genTokenUrl()">
           <button class="btn btn-default" type="button">Generate token url</button>
        </div>
     </div>
  </form>
  
  <a href="#"><code id="tokenurl" class="col-sm-offset-2" onclick="openTokenUrl()">Your token url appears here</code></a>
  <div class="col-sm-11">Token is alpha-numberic and looks like: <code>075734723faf1827e4279e4c46f08c01</code></div>
  
  <br/><br/>
  
* ### Can I add multiple accounts?
  Yes! You may add as many accounts as you want. There is no limit or performance effect by having multiple accounts.
