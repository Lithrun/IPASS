var role = '';
var uri = '/restservices/'; // heroku
//var uri = '/eventapp/restservices/'; // localhost


//get

function getTodayDate(){
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	} 

	today = yyyy+'-'+mm+'-'+dd;
	return today;
	
}

function getEnquetes(){
	
		$.ajax({
			url: uri + "enquetes",
			method: "GET",
			dataFilter: function (data, type) {
				return JSON.parse(data);
			},
			dataType: 'json',
			beforeSend: function (xhr) {
				var token = window.sessionStorage.getItem("sessionToken");
				xhr.setRequestHeader('Authorization', 'Bearer ' + token);
			},
			complete: function (data) {
				result = JSON.parse(data.responseText);
				var html;
				$.each(result, function(key,enquete){
					html += "<option value="+enquete.id+">"+enquete.titel+"</option>";	
				});
				$("#enquetes").html(html);
				var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
				if (evenement !== null) {
					$("#enquetes option[value='"+evenement.enqueteid+"']").attr("selected","selected");
				}
			}
			
		});
	
}

function getEnqueteForm(enqueteid, enquetetitel) {
	
	$.ajax({
		url: uri + "enquetes/"+enqueteid+"?vragen=true",
		method: "GET",
		dataFilter: function (data, type) {
			return JSON.parse(data);
		},
		dataType: 'json',
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		complete: function (data) {
			result = JSON.parse(data.responseText);
			var html = "<div class='left'><form id='enqueteform'>"
			$.each(result.vragen, function(key,vraag){
				html += "<label for='"+vraag.id+"'>"+vraag.vraag+"</label>";
				html += "<textarea name='"+vraag.id+"' id='"+vraag.id+"' class='full'  form='enqueteform' maxlength='500'></textarea>";	
			});
			html += "</form>";	
			html += "<button onclick=addEnqueteResult() class='button blue full'>Verzend enquete</button></div>";
			$(".container").html(html);
			$("#titel").html("<h1>Enquete: " + enquetetitel + "</h1>");
		}
		
	});
	
}

function getGebruikers(){
	
	$.ajax({
		url: uri + "gebruikers",
		method: "GET",
		dataFilter: function (data, type) {
			return JSON.parse(data);
		},
		dataType: 'json',
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		complete: function (data) {
			result = JSON.parse(data.responseText);
			var html = "<div class='table'><table id='alle_gebruikers'><caption>Gebruikers</caption><thead><tr><th>Id</th><th>Naam</th></tr></thead><tbody>"
			var naam;
			var i = 0;
			$.each(result, function(key,gebruiker){
				if (gebruiker.type === "gebruiker") {
					if (gebruiker.tussenvoegsel === "") {
						naam = gebruiker.voornaam + " " + gebruiker.achternaam;
					} else {
						naam = gebruiker.voornaam + " " + gebruiker.tussenvoegsel + " " + gebruiker.achternaam;
					}
					html += "<tr id='sour"+i+"'><td>"+gebruiker.id+"</td><td>"+naam+"</td></tr>";
					i++;
				}
			});
			html += "</tbody></table></div>";
			$(".container #right").append(html);
			$(".container #right").append("<button onclick=addEvenement() class='button blue full'>Maak evenement aan</button>");
			clickableRows();
			
		}
		
	});
	
}

function getEvenementen(){
	
	var uri2 = "";
	if (role === "eventmanager") {
		uri2 = "evenementen?enquete=true"
	} else {
		uri2 = "gebruikers/"+window.sessionStorage.getItem("gebruikerid")+"?evenementen=true";
	}
		
		$.ajax({
			url: uri + uri2,
			method: "GET",
			dataFilter: function (data, type) {
//				console.log(data);
//				console.log(type);
				return JSON.parse(data);
			},
			dataType: 'json',
			beforeSend: function (xhr) {
				var token = window.sessionStorage.getItem("sessionToken");
				xhr.setRequestHeader('Authorization', 'Bearer ' + token);
			},
			succes: function (response) {
				console.log("SUCCES!");
				
			},
			complete: function (data) {
				$("#calendar").fullCalendar('removeEvents');
				//$.each(JSON.parse(data.responseText), function(key,gebruiker){
					//console.log(gebruiker);
					var result; 
					if (role === "eventmanager") {
						result = JSON.parse(data.responseText);
					} else {
						result = JSON.parse(data.responseText).evenementen;
					}
				
					$.each(result, function(key,evenement){
						
						var begindatum = evenement.begintijd.substring(0,10);
						begindatum += "T"+evenement.begintijd.substring(11,19);
						
						var einddatum = evenement.eindtijd.substring(0,10);
						einddatum += "T"+evenement.eindtijd.substring(11,19);
						
						var enquetedatum = evenement.enquetegeldigtot.substring(0,10);
						enquetedatum += "T"+evenement.enquetegeldigtot.substring(11,19);
						
						var date = new Date();
						if (date > new Date(einddatum) && evenement.status !== "geweest" && evenement.status !== "verlopen") {
							console.log("Evenement: " + evenement.id + " ligt in het verleden...");
							updateEvenementStatus(evenement.id,"geweest");
						}
						
						if (date > new Date(enquetedatum) && evenement.status === "geweest" ) {
							console.log("Evenement: " + evenement.id + " zijn enquete is verlopen..");
							updateEvenementStatus(evenement.id,"verlopen");
						}


						addCalendarEvent(evenement.id, evenement.naam, begindatum, einddatum, evenement.status, evenement.gebruikerstatus);
						
					});
					
					
				//});
			}
			
		});
		
	
}

function getEvenement(eventid, status, gebruikerstatus) {
	
	var uri2;
	if (role === "eventmanager") {
		uri2 = "evenementen/"+eventid+"?gebruikers=true&enquete=true";
	} else {
		uri2 = "evenementen/"+eventid+"?enquete=true";
	}
	$.ajax({
		url: uri + uri2,
		method: "GET",
		dataFilter: function (data, type) {
			return JSON.parse(data);
		},
		dataType: 'json',
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		succes: function (response) {
			console.log("SUCCES!");
			
		},
		complete: function (data) {
			window.sessionStorage.setItem("evenement", JSON.stringify(JSON.parse(data.responseText)));
//				storage = JSON.parse(window.localStorage.getItem("evenement"));
//				console.log(storage);
			showEvenement(eventid, status, gebruikerstatus);
			}
			
		});
}

//insert

function addCalendarEvent(id, titel, starttijd, eindtijd, status, gebruikerstatus) {
	//if (role === "gebruiker") {
		color = "";
		if (status === "geannuleerd") {
			color = "#cc5151";	
		} else if(gebruikerstatus === "ingevuld") {
			color = "#aecaf0";
		} else if (status === "verlopen") {
			color = "#bfbfbf";
		} else if (status === "geweest") {
			color = "#4a4aaa";
		} else {	
			if (gebruikerstatus === "uitgenodigd") {
				color = "#ffaa00";
			} else if(gebruikerstatus === "ingeschreven") {
				color = "#379810";
			} else if(gebruikerstatus === "geannuleerd") {
				color = "#e2211d";
			} else {
				color = "#4a4aaa";
			}
		}
		var event = {id:id, title:titel,start: starttijd, end: eindtijd,backgroundColor: color , borderColor: "#000000", textColor: "#000000", status: status, gebruikerstatus: gebruikerstatus };
		$('#calendar').fullCalendar( 'renderEvent', event, true);
	//}
}

function addEnqueteResult(){
	
	var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
	var gebruikerid = window.sessionStorage.getItem("gebruikerid");
	var evenementid = evenement.id;
	
	var data = {
			"gebruikerid" : parseInt(gebruikerid),
			"evenementid" : evenementid,
			vragen : []
	};
	$.each($("#enqueteform").serializeArray(), function() {
		data.vragen.push({
			"id" : parseInt(this.name),
			"antwoord" : this.value
		});
	});
	console.log(data);
	var JSONdata = JSON.stringify(data);
	
	$.ajax({
		url: uri + "evenementen/enqueteresult",
		method: "POST",
		data: JSONdata,
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		succes: function (response) {
			console.log("SUCCES!");
		},
		complete: function (response) {
			if (response.status === 200) {
				alert("Enquete succesvol ingevuld!");
				updateGebruikerStatus(evenement.id,"ingevuld");
				showCalendar();
			} else {
				alert("Er is iets misgegaan...");
			}
		}
	});
	
}

function addEvenement(){
	var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
	var method = "POST";
	var gebruikerids = [];
	$("#gebruiker_uitgenodigd tbody tr").each(function(){
	    gebruikerids.push(parseInt($(this).find("td:first").text())); //put elements into array
	});
	var data = {};
	$.each($("#evenementform").serializeArray(), function() {
		data[this.name] = this.value;
	});
	var enqueteID = parseInt($("#enquetes").val());
	data["gebruikers"] = gebruikerids;
	data["enqueteid"] = enqueteID;
	if (evenement !== null) {
		data["id"] = evenement.id;
		method = "PUT"
	}
	console.log(data);
	var JSONdata = JSON.stringify(data);
	
	$.ajax({
		url: uri + "evenementen",
		method: method,
		data: JSONdata,
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		succes: function (response) {
			console.log("SUCCES!");
		},
		complete: function (response) {
			if (response.status === 200) {
				alert("Evenement succesvol toegevoegd!");
				showCalendar();
			} else {
				alert("Er is iets misgegaan...");
			}
		}
	});
	
}

// update

function updateGebruikerStatus(eventid, gebruikerstatus) {
	var gebruikerid = window.sessionStorage.getItem("gebruikerid");
	$.ajax({
		url: uri + "gebruikers/gebruikerstatus?gebruikerid="+gebruikerid+"&evenementid="+eventid+"&gebruikerstatus="+gebruikerstatus,
		method: "PUT",
		dataType: "json",
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		succes: function (data) {
			console.log("SUCCES!");
			
		},
		complete: function (data) {
			if (data.status === 200) {
				alert("U heeft zich succesvol: "+ gebruikerstatus);
				showCalendar();
			} else {
				alert("Er is iets misgegaan, probeer het later opnieuw..");
			}
			
		}
		
	});
}

function updateEvenementStatus(eventid, status) {
	$.ajax({
		url: uri + "evenementen/status?evenementid="+eventid+"&status="+status,
		method: "PUT",
		dataType: "json",
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		succes: function (data) {
			console.log("SUCCES!");
			
		},
		complete: function (data) {
			if (data.status === 200) {
				if (status === "gepland" || status === "geannuleerd") {
					alert("Het evenement is succesvol gewijzigd naar: "+ status);
				}
				showCalendar();
			} else {
				alert("Er is iets misgegaan, probeer het later opnieuw..");
			}
			
		}
		
	});
}

// show functions

function showCalendar(){
	$(".container").html("");
	$("#titel").html("<h1>Evenement Overzicht</h1>");
	$("#calendar").show();
	getEvenementen();
}

function showEvenement(eventid, status, gebruikerstatus) {
		
		//getEvenement(eventid);
		evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
		console.log(evenement);
		begindatum = evenement.begintijd.substring(0,10);
		einddatum = evenement.eindtijd.substring(0,10);
		begintijd = evenement.begintijd.substring(11,16);
		eindtijd = evenement.eindtijd.substring(11,16);
		tijd = "<p><b>Tijd:</b></p>" + begintijd + " - " + eindtijd;
		button = "";
		bericht = "";
		gebruikerlijst = "";
		if (role == "eventmanager") {
			bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>Dit evenement is <strong> " + status + "</strong></div>";
			gebruikerlijst += "<div class='table'><table id='gebruikers'><tr id='firstRow'><th>Id</th><th>Naam</th><th>Status</th></tr>";
			var naam;
			$.each(evenement.gebruikers, function(key,gebruiker){
				if (gebruiker.tussenvoegsel === "") {
					naam = gebruiker.voornaam + " " + gebruiker.achternaam;
				} else {
					naam = gebruiker.voornaam + " " + gebruiker.tussenvoegsel + " " + gebruiker.achternaam;
				}
				
				gebruikerlijst += "<tr><td>"+gebruiker.id+"</td><td>"+naam+"</td><td>"+gebruiker.gebruikerstatus+"</td></tr>";
			});
			
			gebruikerlijst += "</table></div>";
			
			if (status === "geannuleerd") {
				button = "<button id='ongedaan' class='button grey'>Annulering ongedaan maken</button>";
			} else if (status === "verlopen"){
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>De enquete kan niet meer worden ingevuld door gebruikers. De ingevulde resultaten zijn nog wel in te zien.</div>";
				button = "<button id='enqueteoverzicht' class='button blue'>Enquete overzicht</button>";
			} else if (status === "geweest") {
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>Dit evenement ligt in het verleden, U kunt het niet meer aanpassen</div>";
				button = "<button id='enqueteoverzicht' class='button blue'>Enquete overzicht</button>";
			} else if (status === "gepland") {
				button = "<button id='aanpassen' class='button blue'>Aanpassen</button>" +
						"<button id='annuleren' class='button red'>Annuleer</button>";
			}
				
			
		} else {
			
			if (status === "geannuleerd") {
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>Dit evenement is geannuleerd, U kunt zich niet meer inschrijven/annuleren</div>";
			} else if(gebruikerstatus === "ingevuld") {
				status = "U heeft de enquete voor dit evenement al ingevuld.";
				bericht = "<div class='alert info'><span class='closebtn'>&times;</span><strong>" + gebruikerstatus + " !</strong> " + status + "</div>";
			} else if (status === "geweest" && gebruikerstatus === "ingeschreven") {
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>Dit evenement is geweest, U kunt nog een enquete invullen</div>";
				button = "<button id='enqueteinvullen' class='button blue'>Invullen enquete</button>";
			} else if (status === "geweest") {
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>Dit evenement is geweest, maar U heeft zich niet ingeschreven</div>";
			} else if (status === "verlopen") {
				bericht = "<div class='alert warning'><span class='closebtn'>&times;</span>De enquete van dit evenement is verlopen</div>";
			} 
			
			else {
				if (gebruikerstatus === "uitgenodigd") {
					button = "<button id='inschrijven' class='button green'>Inschrijven</button> " +
							"<button id='annuleren' class='button red'>Annuleer</button>";
					status = "U bent uitgenodigd voor dit evenement. Klik op inschrijven als U zicht wilt inschrijven voor dit evenement. Klik of annuleren als U niet wilt deelnemen aan het evenement.";
				} else if(gebruikerstatus === "ingeschreven") {
					button = "<button id='annuleren' class='button red'>Annuleer</button>";
					status = "U heeft zich ingeschreven voor dit evenement. Klik op annuleren om uw inschrijving ongedaan te maken.";
				} else if(gebruikerstatus === "geannuleerd") {
					button = "<button id='inschrijven' class='button green'>Inschrijven</button> ";
					status = "U heeft de uitnodiging voor dit evenement geannuleerd. Klik op inschrijven als U toch nog wilt deelnemen aan dit evenement.";
				} 
				bericht = "<div class='alert info'><span class='closebtn'>&times;</span><strong>" + gebruikerstatus + " !</strong> " + status + "</div>";
			}
		}
		datum = "";
		if (begindatum === einddatum) {
			datum = begindatum;
		} else {
			datum = begindatum + " tot " + einddatum;
			tijd = "<p><b>Tijd:</b></p> " + "<b>" + begindatum + "</b>: " + begintijd + " tot <b>" + einddatum + "</b>: " + eindtijd; 
		}
		var html = bericht
			+ "<p><b>Informatie:</b></p>" + evenement.informatie
			+ "<p><b>Locatie:</b></p>" + evenement.plaats + ", " + evenement.straat + " " + evenement.huisnummer + " " + evenement.postcode
			+ tijd
			+ "<p><b>Enquete geldig tot:</b>" + evenement.enquetegeldigtot + " </p> "
			+ gebruikerlijst
			+ "<br>" + button;
		$(".container").html(html);
		$("#titel").html("<h1>" + evenement.naam + "   " + datum + "</h1>");
		closeAlert();
		evenementButton(eventid, gebruikerstatus);
}

// check time
function setMinDate(){
	$('#datetimepicker2').datetimepicker({minDate: $('#datetimepicker').val()});
}

// check time
function setMaxDate(){
	$('#datetimepicker').datetimepicker({maxDate: $('#datetimepicker2').val()});
}

function showEvenementForm(type) {
	var html = "<div class='left'></div><div id='right' class='right'></div>";
	var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
	$(".container").html(html);
		
	html = "<form id='evenementform'>"
		+ "<input type='text' id='naam' class='full' name='naam' placeholder='Evenementnaam...' /><br>"
		+ "<select class='full'  id='enquetes'></select>"
		+ "<input type='text' id='enquetegeldigtot' class='full'  value='' name='enquetegeldigtot' placeholder='Enquete geldig tot...' />"
		+ "<textarea name='informatie' id='informatie' class='full'  form='evenementform' maxlength='500'>Informatie...</textarea><br>"
		+ "<input type='text' id='begintijd' class='full'  value='' name='begintijd' placeholder='Begin tijd...' onchange='setMinDate()' /><br>"
		+ "<input type='text' id='eindtijd' class='full'  value='' name='eindtijd' placeholder='Eind tijd...' onchange='setMaxDate()' /><br>"
		+ "<input type='text' id='straatnaam' class='full'  name='straatnaam' placeholder='Straatnaam...' /><br>"
		+ "<input type='text' id='huisnummer' class='full'  name='huisnummer' placeholder='Huisnummer...' /><br>"
		+ "<input type='text' id='postcode' class='full'  name='postcode' placeholder='Postcode...' /><br>"
		+ "<input type='text' id='plaats'class='full'  name='plaats' placeholder='Plaats...' /><br>"
		+ "<input type='text' id='land' class='full'  name='land' placeholder='Land...' /><br>"
		+ "<textarea name='bijzonderheden' id='bijzonderheden' class='full'  form='evenementform' maxlength='500'>Bijzonderheden...</textarea><br>"
		+ "</form></div>";
		
	$(".left").html(html);
	
	html = "<form method='POST' action=''>"
	+ "<div class='table'><table id='gebruiker_uitgenodigd'><caption>Uitgenodigde gebruikers</caption><thead><tr><th>Id</th><th>Naam</th></tr></thead><tbody></tbody></table></div></form><br>"
	+ "<div><input type='text' class='full' id='searchGebruiker' onkeyup='searchGebruikerList()' placeholder='Zoek voor ..'></div>"
	+ "<div><select id='searchOptions' class='full'>"
		+ "<option value=0>Id</option>"
		+ "<option value=1>Naam</option></select></div><br>";
	
	$(".container #right").html(html);
	
	getEnquetes();
	getGebruikers();
	
	if (type === "edit") {
		$("#naam").val(evenement.naam);
		$("#informatie").val(evenement.informatie);
		$("#begintijd").val(evenement.begintijd);
		$("#eindtijd").val(evenement.eindtijd);
		$("#straatnaam").val(evenement.straat);
		$("#huisnummer").val(evenement.huisnummer);
		$("#postcode").val(evenement.postcode);
		$("#plaats").val(evenement.plaats);
		$("#land").val(evenement.land);
		$("#bijzonderheden").val(evenement.bijzonderheden);
		
		$.each(evenement.gebruikers, function(key,gebruiker){
			if (gebruiker.type === "gebruiker") {
				if (gebruiker.tussenvoegsel === "") {
					naam = gebruiker.voornaam + " " + gebruiker.achternaam;
				} else {
					naam = gebruiker.voornaam + " " + gebruiker.tussenvoegsel + " " + gebruiker.achternaam;
				}
				$("#gebruiker_uitgenodigd tbody").append("<tr><td>"+gebruiker.id+"</td><td>"+naam+"</td></tr>");
			}
		});
		$("#enquetegeldigtot").val(evenement.enquetegeldigtot);
		
	} else {
		sessionStorage.removeItem('evenement');
	}
	
	$.datetimepicker.setLocale('nl');


	$('#begintijd').datetimepicker({
		dayOfWeekStart : 1,
		lang:'nl',
		format: "Y-m-d H:i:s",
		//disabledDates:['1986/01/08','1986/01/09','1986/01/10'],
		//startDate:	'1986/01/05',
		minDate: new Date(),
		step:10
	});
	
	$('#eindtijd').datetimepicker({
		dayOfWeekStart : 1,
		lang:'nl',
		format: "Y-m-d H:i:s",
		//disabledDates:['1986/01/08','1986/01/09','1986/01/10'],
		//startDate:	'1986/01/05',
		step:10
	});
	
	$('#enquetegeldigtot').datetimepicker({
		dayOfWeekStart : 1,
		lang:'nl',
		format: "Y-m-d H:i:s",
		//disabledDates:['1986/01/08','1986/01/09','1986/01/10'],
		//startDate:	'1986/01/05',
		minDate: new Date(),
		step:10
	});
		
	$("#titel").html("<h1>Evenement Toevoegen</h1>");
	$("#calendar").hide();
}

function showLogin(){
	var html = 	"<div id= 'errorLogin'></div>" +
			"<div id = 'loginPagina'>"
			+	"<p>Welkom bij het evenement systeem</p>"
			+	"<p>Vul uw emailadres en wachtwoord in om verder te gaan</p>"
			+	"<form id='loginform'> "
					+	"<input type='text' value='nickwindt@hotmail.nl' name='emailadres' placeholder='Emailadres...' /><br>"
					+	"<input type='password' value='wachtwoord' name='password' placeholder='Wachtwoord...'/><br>"
			+	"</form></div>";
	$(".container").html(html);
	html = "<h1>Evenement Systeem</h1>";
	$("#titel").html(html);
	$("#calendar").hide();
	$("#sidebar").hide();
	$("#sidebarbutton").hide();
	sessionStorage.removeItem('sessionToken');	
	sessionStorage.removeItem('role');	
	sessionStorage.removeItem('gebruikerid');
	sessionStorage.removeItem('evenement');
	loginButton();
	closeSidebar();
	
}

function showEnquete(){
	//to do
	//http://localhost:4711/eventapp/restservices/evenementen/1?enquete=true&gebruikers=false&enqueteantwoorden=true
	var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
	
	$.ajax({
		url: uri + "evenementen/" + evenement.id + "?enquete=true&enqueteantwoorden=true",
		method: "GET",
		dataFilter: function (data, type) {
			return JSON.parse(data);
		},
		dataType: 'json',
		beforeSend: function (xhr) {
			var token = window.sessionStorage.getItem("sessionToken");
			xhr.setRequestHeader('Authorization', 'Bearer ' + token);
		},
		complete: function (response) {
			if (response.status === 200) {
				var result = JSON.parse(response.responseText);
				var html = "";//"<div class='left'>";
				$.each(result.vragen, function(key,vraag){ // laad de vragen
					html += "<div class='enquete'><table style='width:100%' id='"+vraag.vraagid+"'><caption>"+vraag.vraag+"</caption><thead><tr><th>Antwoord</th></tr></thead><tbody></tbody></table></div><br>";	
				});
				//html += "</div>";
				$(".container").html(html);
				$("#titel").html("<h1>"+result.enquetetitel+"</h1>");
				$.each(result.antwoorden, function(key,antwoord){ // laad de vragen
					$("#"+antwoord.vraagid+" tbody").append("<tr><td>"+antwoord.antwoord+"</td></tr>");	
				});
				
				alert("Enquete overzicht");
				
				
			} else {
				alert("Er is iets misgegaan...");
				showCalendar();
			}
			
			
		}
		
	});
	
}

function showEnqueteForm(){
	var evenement = JSON.parse(window.sessionStorage.getItem("evenement"));
	console.log(evenement);
	getEnqueteForm(evenement.enqueteid, evenement.enquetetitel);
}

// Buttons
function loginButton() {
	$("#loginPagina").append("<button id='login' class='button black'>Login</button>")
	$("#login").click(function(event) {
		var data = $("#loginform").serialize();
		
		$.post(uri + "authentication", data, function(response) {
			console.log("Login succes!");
			var storage = JSON.parse(response);
			window.sessionStorage.setItem("sessionToken", storage.token);
			window.sessionStorage.setItem("role", storage.role);
			window.sessionStorage.setItem("gebruikerid", storage.gebruikerid);
			role = window.sessionStorage.getItem("role");	
			if (role === "eventmanager") {
				$("#addEventButton").show();
			} else {
				$("#addEventButton").hide();
			}
			$("#sidebar").show();
			$("#sidebarbutton").show();
			showCalendar();
			closeSidebar();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			$("#errorLogin").html("<div class='alert'><span class='closebtn'>&times;</span>Ingevulde gegevens zijn onjuist</div>");
			closeAlert();
			console.log("Login failed!");
			console.log(textStatus);
			console.log(errorThrown);
		});
	});
}

function evenementButton(eventid, gebruikerstatus) {
	$("#annuleren").click(function(event){
		if (role === "eventmanager") {
			updateEvenementStatus(eventid,"geannuleerd")
		} else {
		updateGebruikerStatus(eventid,"geannuleerd");
		}
	});
	
	$("#inschrijven").click(function(event){
		updateGebruikerStatus(eventid,"ingeschreven");
	});
	
	$("#enqueteoverzicht").click(function(event){
		showEnquete();
	});
	
	$("#enqueteinvullen").click(function(event){
		showEnqueteForm();
	});
	
	$("#aanpassen").click(function(event){
		showEvenementForm("edit");
	});
	
	$("#ongedaan").click(function(event){
		updateEvenementStatus(eventid,"gepland");
	});
}

//sidebar

function openSidebar() {
	  document.getElementById("main").style.marginLeft = "150px";//"25%";
	  document.getElementById("sidebar").style.width = "150px";//"25%";
	  document.getElementById("sidebar").style.display = "block";
	  //document.getElementById("openNav").style.display = 'none';
	}

function closeSidebar() {
	  document.getElementById("main").style.marginLeft = "0px";//"0%";
	  document.getElementById("sidebar").style.display = "none";
	  //document.getElementById("openNav").style.display = "inline-block";
}

//misc

function closeAlert(){
	var close = document.getElementsByClassName("closebtn");
	var i;

	for (i = 0; i < close.length; i++) {
	    close[i].onclick = function(){
	        var div = this.parentElement;
	        div.style.opacity = "0";
	        setTimeout(function(){ div.style.display = "none"; }, 600);
	    }
	}
}

function searchGebruikerList() {
	  // Declare variables
	  var input, filter, table, tr, td, i;
	  input = document.getElementById("searchGebruiker");
	  filter = input.value.toUpperCase();
	  table = document.getElementById("alle_gebruikers");
	  tr = table.getElementsByTagName("tr");

	  var e = document.getElementById("searchOptions")
	  e = e.options[e.selectedIndex].value;
	  // Loop through all table rows, and hide those who don't match the search query
	  for (i = 0; i < tr.length; i++) {
	    td = tr[i].getElementsByTagName("td")[e];
	    if (td) {
	      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
	        tr[i].style.display = "";
	      } else {
	        tr[i].style.display = "none";
	      }
	    }
	  }
}

function clickableRows(){
	var addedrows = new Array();
	 
	$(document).ready(function() {
	    $( "#alle_gebruikers tbody tr" ).on( "click", function( event ) {
	   
	    var ok = 0;
	    var theid = $( this ).attr('id').replace("sour","");    
	 
	    var newaddedrows = new Array();
	     
	    for (index = 0; index < addedrows.length; ++index) {
	 
	        // if already selected then remove
	        if (addedrows[index] == theid) {
	                
	            $( this ).css( "background-color", "" );
	             
	            // remove from second table :
	            var tr = $( "#dest" + theid );
	            tr.css("background-color","#FF3700");
	            tr.fadeOut(400, function(){
	                tr.remove();
	            });
	             
	            //addedrows.splice(theid, 1);   
	             
	            //the boolean
	            ok = 1;
	        } else {
	         
	            newaddedrows.push(addedrows[index]);
	        } 
	    }   
	     
	    addedrows = newaddedrows;
	     
	    // if no match found then add the row :
	    if (!ok) {
	        // retrieve the id of the element to match the id of the new row :
	         
	         
	        addedrows.push( theid);
	         
	        $( this ).css( "background-color", "#cacaca" );
	                 
	        $('#gebruiker_uitgenodigd tbody').append('<tr id="dest' + theid + '"><td>'
	                                       + $(this).find("td").eq(0).html() + '</td><td>'
	                                       + $(this).find("td").eq(1).html() + '</td></tr>');         
	         
	    }
	 
	     
	    });
	}); 
		 
	$(document).ready(function() {
	    $( "#gebruiker_uitgenodigd tbody tr" ).on( "click", function( event ) {
	   
	    	$( this ).remove();
	 
	    });
	});
}

//Problem refresh the list
function clickableRows2(){
	
	var addedrows = new Array();
	 
	$(document).ready(function() {
	    $( "#alle_gebruikers tbody tr" ).on( "click", function( event ) {
	   
	    var ok = 0;
	    var theid = $( this ).attr('id').replace("sour","");    
	 
	    var newaddedrows = new Array();
	     
	    for (index = 0; index < addedrows.length; ++index) {
	 
	        // if already selected then remove
	        if (addedrows[index] == theid) {
	                
	            $( this ).css( "background-color", "" );
	             
	            // remove from second table :
	            var tr = $( "#dest" + theid );
	            tr.css("background-color","#FF3700");
	            tr.fadeOut(400, function(){
	                tr.remove();
	            });
	             
	            //addedrows.splice(theid, 1);   
	             
	            //the boolean
	            ok = 1;
	        } else {
	         
	            newaddedrows.push(addedrows[index]);
	        } 
	    }   
	     
	    addedrows = newaddedrows;
	     
	    // if no match found then add the row :
	    if (!ok) {
	        // retrieve the id of the element to match the id of the new row :
	         
	         
	        addedrows.push( theid);
	         
	        $( this ).css( "background-color", "#cacaca" );
	                 
	        $('#gebruiker_uitgenodigd tbody').append('<tr id="dest' + theid + '"><td>'
	                                       + $(this).find("td").eq(0).html() + '</td><td>'
	                                       + $(this).find("td").eq(1).html() + '</td></tr>');         
	         
	    }
	 
	     
	    });
	}); 
	
	var addedrows2 = new Array();
	 
	
	$(document).ready(function() {
	    $( "#gebruiker_uitgenodigd tbody tr" ).on( "click", function( event ) {
	   
	    var ok = 0;
	    var theid = $( this ).attr('id').replace("dest","");    
	 
	    var newaddedrows2 = new Array();
	     
	    for (index = 0; index < addedrows2.length; ++index) {
	 
	        // if already selected then remove
	        if (addedrows2[index] == theid) {
	                
	            $( this ).css( "background-color", "" );
	             
	            // remove from second table :
	            var tr = $( "#sour" + theid );
	            tr.css("background-color","#FF3700");
	            tr.fadeOut(400, function(){
	                tr.remove();
	            });
	             
	            //addedrows.splice(theid, 1);   
	             
	            //the boolean
	            ok = 1;
	        } else {
	         
	            newaddedrows2.push(addedrows2[index]);
	        } 
	    }   
	     
	    addedrows2 = newaddedrows2;
	     
	    // if no match found then add the row :
	    if (!ok) {
	        // retrieve the id of the element to match the id of the new row :
	         
	         
	        addedrows2.push( theid);
	         
	        $( this ).css( "background-color", "#cacaca" );
	                 
	        $('#alle_gebruikers tbody').append('<tr id="sour' + theid + '"><td>'
	                                       + $(this).find("td").eq(0).html() + '</td><td>'
	                                       + $(this).find("td").eq(1).html() + '</td></tr>');         
	         
	    }
	 
	     
	    });
	}); 
	
}
//start the page

function initPage() {
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,listMonth'
		},
		defaultView: 'month',
		defaultDate: getTodayDate(),
		eventClick: function(calEvent, jsEvent, view) {

			getEvenement(calEvent.id, calEvent.status, calEvent.gebruikerstatus);
			$("#calendar").hide();

		},
		height: $(window).height()*0.83,
		navLinks: true, // can click day/week names to navigate views
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: [
		]
	});
	showLogin();
	

}

initPage();