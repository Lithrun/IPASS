package ipass.eventapp.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "GEBRUIKERS")
public class Gebruiker {
	
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "emailadres")
	private String emailadres;
	
	@Column(name = "wachtwoord")
	private String wachtwoord;
	
	@Column(name = "voornaam")
	private String voornaam;
	
	@Column(name = "tussenvoegsel")
	private String tussenvoegsel;
	
	@Column(name = "achternaam")
	private String achternaam;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "geslacht")
	private String geslacht;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.gebruiker", cascade = CascadeType.ALL)
	private List<GebruikerEvenement> gebruikerEvenementen;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.gebruiker", cascade = CascadeType.ALL)
	private List<EvenementEnquete> evenementEnquete;
	
	

	public Gebruiker(String emailadres, String wachtwoord, String voornaam, String tussenvoegsel, String achternaam,
			String type, String geslacht) {
		super();
		this.emailadres = emailadres;
		this.wachtwoord = wachtwoord;
		this.voornaam = voornaam;
		this.tussenvoegsel = tussenvoegsel;
		this.achternaam = achternaam;
		this.type = type;
		this.geslacht = geslacht;
	}
	
	public Gebruiker(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailadres() {
		return emailadres;
	}

	public void setEmailadres(String emailadres) {
		this.emailadres = emailadres;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public void setTussenvoegsel(String tussenvoegsel) {
		this.tussenvoegsel = tussenvoegsel;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGeslacht() {
		return geslacht;
	}

	public void setGeslacht(String geslacht) {
		this.geslacht = geslacht;
	}

	public List<GebruikerEvenement> getGebruikerEvenementen() {
		return gebruikerEvenementen;
	}

	public void setGebruikerEvenementen(List<GebruikerEvenement> gebruikerEvenementen) {
		this.gebruikerEvenementen = gebruikerEvenementen;
	}

	public List<EvenementEnquete> getEvenementEnquete() {
		return evenementEnquete;
	}

	public void setEvenementEnquete(List<EvenementEnquete> evenementEnquete) {
		this.evenementEnquete = evenementEnquete;
	}

	
	
	
	
	
	
}
