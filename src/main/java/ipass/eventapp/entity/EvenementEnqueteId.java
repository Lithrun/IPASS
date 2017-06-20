package ipass.eventapp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Embeddable
public class EvenementEnqueteId implements Serializable{
	private Evenement evenement;
	private Gebruiker gebruiker;
	private Vraag vraag;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Evenement getEvenement() {
		return evenement;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Gebruiker getGebruiker() {
		return gebruiker;
	}
	
	public void setGebruiker(Gebruiker gebruiker) {
		this.gebruiker = gebruiker;
	}
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Vraag getVraag() {
		return vraag;
	}
	
	public void setVraag(Vraag vraag) {
		this.vraag = vraag;
	}
	

}
