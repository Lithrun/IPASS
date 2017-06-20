package ipass.eventapp.entity;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="VRAGEN")
public class Vraag {
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	@Column(name = "vraag")
	private String vraag;
	
	@ManyToMany(
			fetch = FetchType.LAZY,
	        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
	        mappedBy = "vraagList",
	        targetEntity = Enquete.class
	    )
	private List<Enquete> enqueteList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.vraag", cascade = CascadeType.ALL)
	private List<EvenementEnquete> evenementEnquete;
	
	public Vraag(){}
	
	public Vraag(String vraag) {
		this.vraag = vraag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVraag() {
		return vraag;
	}

	public void setVraag(String vraag) {
		this.vraag = vraag;
	}

	public List<EvenementEnquete> getEvenementEnquete() {
		return evenementEnquete;
	}

	public void setEvenementEnquete(List<EvenementEnquete> evenementEnquete) {
		this.evenementEnquete = evenementEnquete;
	}
	
	
	
//	public List<Enquete> getEnqueteList() {
//		return enqueteList;
//	}
//
//	public void setEnqueteList(List<Enquete> enqueteList) {
//		this.enqueteList = enqueteList;
//	}
	
	

}
