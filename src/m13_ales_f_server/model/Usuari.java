package m13_ales_f_server.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author David Ales Fernandez
 * 
 */
@Entity
@Table(name = "usuari")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuari.findAll", query = "SELECT u FROM Usuari u"),
    @NamedQuery(name = "Usuari.validateCredencials", query = "SELECT u FROM Usuari u WHERE u.email = :email AND u.password = :password"),
    @NamedQuery(name = "Usuari.findById", query = "SELECT u FROM Usuari u WHERE u.id = :id"),
    @NamedQuery(name = "Usuari.findByCognom1", query = "SELECT u FROM Usuari u WHERE u.cognom1 = :cognom1"),
    @NamedQuery(name = "Usuari.findByCognom2", query = "SELECT u FROM Usuari u WHERE u.cognom2 = :cognom2"),
    @NamedQuery(name = "Usuari.findByDireccio", query = "SELECT u FROM Usuari u WHERE u.direccio = :direccio"),
    @NamedQuery(name = "Usuari.findByEmail", query = "SELECT u FROM Usuari u WHERE u.email = :email"),
    @NamedQuery(name = "Usuari.findByNom", query = "SELECT u FROM Usuari u WHERE u.nom = :nom"),
    @NamedQuery(name = "Usuari.findByPassword", query = "SELECT u FROM Usuari u WHERE u.password = :password"),
    @NamedQuery(name = "Usuari.findByRol", query = "SELECT u FROM Usuari u WHERE u.rol = :rol"),
    @NamedQuery(name = "Usuari.findByTelefon", query = "SELECT u FROM Usuari u WHERE u.telefon = :telefon")})
public class Usuari implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "cognom1")
    private String cognom1;
    @Size(max = 255)
    @Column(name = "cognom2")
    private String cognom2;
    @Size(max = 255)
    @Column(name = "direccio")
    private String direccio;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "rol")
    private String rol;
    @Size(max = 255)
    @Column(name = "telefon")
    private String telefon;
    @OneToMany(mappedBy = "idUsuari")
    private Collection<Mascota> mascotaCollection;
    @OneToMany(mappedBy = "idDueno")
    private Collection<Cita> citaCollection;
    @OneToMany(mappedBy = "idVeterinari")
    private Collection<Cita> citaCollection1;

    public Usuari() {
    }

    public Usuari(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCognom1() {
        return cognom1;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @XmlTransient
    public Collection<Mascota> getMascotaCollection() {
        return mascotaCollection;
    }

    public void setMascotaCollection(Collection<Mascota> mascotaCollection) {
        this.mascotaCollection = mascotaCollection;
    }

    @XmlTransient
    public Collection<Cita> getCitaCollection() {
        return citaCollection;
    }

    public void setCitaCollection(Collection<Cita> citaCollection) {
        this.citaCollection = citaCollection;
    }

    @XmlTransient
    public Collection<Cita> getCitaCollection1() {
        return citaCollection1;
    }

    public void setCitaCollection1(Collection<Cita> citaCollection1) {
        this.citaCollection1 = citaCollection1;
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
        if (!(object instanceof Usuari)) {
            return false;
        }
        Usuari other = (Usuari) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "m13_ales_f_server.model.Usuari[ id=" + id + " ]";
    }

}