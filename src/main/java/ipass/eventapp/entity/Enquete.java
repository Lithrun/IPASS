package ipass.eventapp.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "ENQUETE")
public class Enquete {
	
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "titel")
	private String titel;
		
	@ManyToMany(
			fetch = FetchType.LAZY,
	        targetEntity=Vraag.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	@JoinTable(
	        name="ENQUETE_VRAAG",
	        joinColumns=@JoinColumn(name="ENQUETE_ID"),
	        inverseJoinColumns=@JoinColumn(name="VRAAG_ID")
	    )
	private List<Vraag> vraagList;

	public Enquete(String titel) {
		this.titel = titel;
	}
	
	public Enquete(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public List<Vraag> getVraagList() {
		return vraagList;
	}

	public void setVraagList(List<Vraag> vraagList) {
		this.vraagList = vraagList;
	}
	
	
		

}
