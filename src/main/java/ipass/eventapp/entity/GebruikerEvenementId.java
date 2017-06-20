package ipass.eventapp.entity;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class GebruikerEvenementId implements Serializable{
	private Gebruiker gebruiker;
	private Evenement evenement;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Gebruiker getGebruiker() {
		return gebruiker;
	}
	public void setGebruiker(Gebruiker gebruiker) {
		this.gebruiker = gebruiker;
	}
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Evenement getEvenement() {
		return evenement;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	
	
	
	
	
}
