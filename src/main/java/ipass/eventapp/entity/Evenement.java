package ipass.eventapp.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "EVENEMENT")
public class Evenement {
	
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//@Column(name = "enquete_ID")
	@ManyToOne(cascade = CascadeType.ALL)
	private Enquete enquete;
	
	@Column(name = "enquete_geldig")
	private Date enqueteGeldig;
	
	@Column(name = "naam")
	private String naam;
	
	@Column(name = "informatie")
	private String informatie;
	
	@Column(name = "bijzonderheden")
	private String bijzonderheden;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "begintijd")
	private Date begintijd;
	
	@Column(name = "eindtijd")
	private Date eindtijd;
	
	@Column(name = "plaats")
	private String plaats;
	
	@Column(name = "straat")
	private String straat;
	
	@Column(name = "huisnummer")
	private String huisnummer;
	
	@Column(name = "postcode")
	private String postcode;
	
	@Column(name = "land")
	private String land;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.evenement", cascade = CascadeType.ALL)
	private List<GebruikerEvenement> gebruikerEvenementen;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.evenement", cascade = CascadeType.ALL)
	private List<EvenementEnquete> evenementEnquete;

	public Evenement(Enquete enquete, Date enqueteGeldig, String naam, String informatie, String bijzonderheden, String status,
			Date begintijd, Date eindtijd, String plaats, String straat, String huisnummer, String postcode,
			String land) {
		super();
		this.enquete = enquete;
		this.enqueteGeldig = enqueteGeldig;
		this.naam = naam;
		this.informatie = informatie;
		this.bijzonderheden = bijzonderheden;
		this.status = status;
		this.begintijd = begintijd;
		this.eindtijd = eindtijd;
		this.plaats = plaats;
		this.straat = straat;
		this.huisnummer = huisnummer;
		this.postcode = postcode;
		this.land = land;
	}
	
	public Evenement(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Enquete getEnquete() {
		return enquete;
	}

	public void setEnquete(Enquete enquete) {
		this.enquete = enquete;
	}
	
	public Date getEnqueteGeldig() {
		return enqueteGeldig;
	}

	public void setEnqueteGeldig(Date enqueteGeldig) {
		this.enqueteGeldig = enqueteGeldig;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getInformatie() {
		return informatie;
	}

	public void setInformatie(String informatie) {
		this.informatie = informatie;
	}

	public String getBijzonderheden() {
		return bijzonderheden;
	}

	public void setBijzonderheden(String bijzonderheden) {
		this.bijzonderheden = bijzonderheden;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBegintijd() {
		return begintijd;
	}

	public void setBegintijd(Date begintijd) {
		this.begintijd = begintijd;
	}

	public Date getEindtijd() {
		return eindtijd;
	}

	public void setEindtijd(Date eindtijd) {
		this.eindtijd = eindtijd;
	}

	public String getPlaats() {
		return plaats;
	}

	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	public String getStraat() {
		return straat;
	}

	public void setStraat(String straat) {
		this.straat = straat;
	}

	public String getHuisnummer() {
		return huisnummer;
	}

	public void setHuisnummer(String huisnummer) {
		this.huisnummer = huisnummer;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
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
