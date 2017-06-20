package ipass.eventapp.entity;

import javax.persistence.*;


@Entity
@Table(name = "EVENEMENT_ENQUETE")
@AssociationOverrides({
    @AssociationOverride(name = "pk.evenement",
        joinColumns = @JoinColumn(name = "evenement_ID")),
    @AssociationOverride(name = "pk.gebruiker",
    	joinColumns = @JoinColumn(name = "gebruiker_ID")),
    @AssociationOverride(name = "pk.vragen",
        joinColumns = @JoinColumn(name = "vraag_ID")) })
public class EvenementEnquete {
	
	private EvenementEnqueteId primaryKey = new EvenementEnqueteId();
	
	@Column(name = "antwoord")
	private String antwoord;
	
	@EmbeddedId
	public EvenementEnqueteId getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(EvenementEnqueteId primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Transient
	public Evenement getEvenement(){
		return getPrimaryKey().getEvenement();
	}
	
	public void setEvenement(Evenement evenement){
		getPrimaryKey().setEvenement(evenement);
	}
	
	@Transient
	public Gebruiker getGebruiker(){
		return getPrimaryKey().getGebruiker();
	}
	
	public void setGebruiker(Gebruiker gebruiker){
		getPrimaryKey().setGebruiker(gebruiker);
	}
	
	@Transient
	public Vraag getVraag(){
		return getPrimaryKey().getVraag();
	}
	
	public void setVraag(Vraag vraag){
		getPrimaryKey().setVraag(vraag);
	}

	public String getAntwoord() {
		return antwoord;
	}

	public void setAntwoord(String antwoord) {
		this.antwoord = antwoord;
	}
	

}
