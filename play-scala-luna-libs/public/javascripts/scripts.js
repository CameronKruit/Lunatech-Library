var loggedIn;
var userID;
var authload = false;
var admin_rights = false;
console.log('------------------------------------------');
//console.log('before loggedin= ' + loggedIn);
checkLogState();
console.log('afted loggedin= ' + loggedIn);
console.log('------------------------------------------');

init();
function init() {
  gapi.load('client:auth2', function(){authload = true; console.log('------------------------------------------'); console.log('loaded auth2'); console.log('------------------------------------------');});
}

function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  var id_token = googleUser.getAuthResponse().id_token;
  userID = profile.getId();
  setCookie("lunatech.lunatech_library.ID", userID, 1);
  setCookie("lunatech.lunatech_library.signState", profile.getId(), 1);
  console.log('------------------------------------------');
  console.log('ID: ' + userID); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
  var d = profile.getEmail().split("@")
  console.log('Domain: ' + d[1]);
  console.log('------------------------------------------');
  if(d[1] != "lunatech.nl") {
    signOut();
  } else {
  //if(!loggedIn) {
    sendAuthRequest(id_token);
    //}
  }
  if(!loggedIn) {
    location.reload();
  }
  checkLogState();
}

function signOut() {
console.log('before loggedin= ' + loggedIn);
console.log('calling sign out');
    if(authload && loggedIn) {
    console.log('call success');
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
    deleteCookie("lunatech.lunatech_library.signState");
    checkLogState();
    console.log('afted loggedin= ' + loggedIn);
    location.reload();
    }
 }

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/;";
}

function setCookie(cname, cvalue) {
    document.cookie = cname + "=" + cvalue + ";path=/;";
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

function deleteCookie(cname, ) {
    document.cookie = cname + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function checkLogState() {
    var lastID = getCookie("lunatech.lunatech_library.ID");
    var y = getCookie("lunatech.lunatech_library.signState");
    console.log('found: ' + y);
    console.log('found: ' + lastID);
    if(y != null && y != "" && y == lastID) {
        loggedIn = true;
        console.log('still logged in');
    } else {
        loggedIn = false;
        console.log('not logged in');
        console.log(window.location.href);
        if(window.location.href != "http://localhost:9000/") {
            window.location.href = "/";
        }
    }
}

function disableVisibility (id, state) {
    console.log('disabling stuff');
    var x = document.getElementsByClassName(id);
    if(!state) {
    for (i = 0; i < x.length; i++) {
        console.log('found: ' + i);
        x[i].style.display = 'none';
        }
    }
}

function sendAuthRequest (id_token) {
    console.log("received token: " + id_token.substring(0, 20) + "...");
     console.log("sending auth request");
    var xhr = new XMLHttpRequest();
    console.log("opening xhr");
    xhr.open('POST', '/auth');
    console.log("setting request header");
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
       console.log('admin rights: ' + xhr.responseText);
       if(xhr.responseText == "true") {
        admin_rights = true;
       }
    };
    xhr.send('idtoken=' + id_token);
}