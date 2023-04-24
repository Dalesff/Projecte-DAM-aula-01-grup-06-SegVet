package m13_ales_f_server.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author David Ales Fernandez
 */
@Entity
@Table(name = "mascota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mascota.findAll", query = "SELECT m FROM Mascota m"),
    @NamedQuery(name = "Mascota.findById", query = "SELECT m FROM Mascota m WHERE m.id = :id"),
    @NamedQuery(name = "Mascota.findByEdad", query = "SELECT m FROM Mascota m WHERE m.edad = :edad"),
    @NamedQuery(name = "Mascota.findByEspecie", query = "SELECT m FROM Mascota m WHERE m.especie = :especie"),
    @NamedQuery(name = "Mascota.findByNom", query = "SELECT m FROM Mascota m WHERE m.nom = :nom"),
    @NamedQuery(name = "Mascota.findByRaza", query = "SELECT m FROM Mascota m WHERE m.raza = :raza"),
    @NamedQuery(name = "Mascota.findBySexo", query = "SELECT m FROM Mascota m WHERE m.sexo = :sexo")})
public class Mascota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "edad")
    private Integer edad;
    @Size(max = 255)
    @Column(name = "especie")
    private String especie;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    @Size(max = 255)
    @Column(name = "raza")
    private String raza;
    @Column(name = "sexo")
    private Character sexo;
    @JoinColumn(name = "id_historial", referencedColumnName = "id")
    @OneToOne
    private HistorialMedic idHistorial;
    @JoinColumn(name = "id_usuari", referencedColumnName = "id")
    @ManyToOne
    private Usuari idUsuari;
    @OneToMany(mappedBy = "idMascota")
    private Collection<Cita> citaCollection;

    public Mascota() {
    }

    public Mascota(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public HistorialMedic getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(HistorialMedic idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Usuari getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Usuari idUsuari) {
        this.idUsuari = idUsuari;
    }

    @XmlTransient
    public Collection<Cita> getCitaCollection() {
        return citaCollection;
    }

    public void setCitaCollection(Collection<Cita> citaCollection) {
        this.citaCollection = citaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mascota)) {
            return false;
        }
        Mascota other = (Mascota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "m13_ales_f_server.model.Mascota[ id=" + id + " ]";
    }
}
