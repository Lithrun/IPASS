package ipass.eventapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "GEBRUIKER_EVENEMENT")
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.gebruiker",
        joinColumns = @JoinColumn(name = "gebruiker_ID")),
    @AssociationOverride(name = "primaryKey.evenement",
        joinColumns = @JoinColumn(name = "evenement_ID")) })
public class GebruikerEvenement {
	
	private GebruikerEvenementId primaryKey = new GebruikerEvenementId();
	
	@Column(name = "status")
	private String status;

	@EmbeddedId
	public GebruikerEvenementId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(GebruikerEvenementId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Transient
	public Gebruiker getGebruiker(){
		return getPrimaryKey().getGebruiker();
	}
	
	public void setGebruiker(Gebruiker gebruiker){
		getPrimaryKey().setGebruiker(gebruiker);
	}
	
	@Transient
	public Evenement getEvenement(){
		return getPrimaryKey().getEvenement();
	}
	
	public void setEvenement(Evenement evenement){
		getPrimaryKey().setEvenement(evenement);
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
