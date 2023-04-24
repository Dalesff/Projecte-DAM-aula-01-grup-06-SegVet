package m13_ales_f_server.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author David Ales Fernandez
 */
@Entity
@Table(name = "cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findById", query = "SELECT c FROM Cita c WHERE c.id = :id"),
    @NamedQuery(name = "Cita.findByData", query = "SELECT c FROM Cita c WHERE c.data = :data"),
    @NamedQuery(name = "Cita.findByDescripcio", query = "SELECT c FROM Cita c WHERE c.descripcio = :descripcio")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Size(max = 255)
    @Column(name = "descripcio")
    private String descripcio;
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    @ManyToOne
    private Mascota idMascota;
    @JoinColumn(name = "id_dueno", referencedColumnName = "id")
    @ManyToOne
    private Usuari idDueno;
    @JoinColumn(name = "id_veterinari", referencedColumnName = "id")
    @ManyToOne
    private Usuari idVeterinari;

    public Cita() {
    }

    public Cita(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Mascota getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Mascota idMascota) {
        this.idMascota = idMascota;
    }

    public Usuari getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(Usuari idDueno) {
        this.idDueno = idDueno;
    }

    public Usuari getIdVeterinari() {
        return idVeterinari;
    }

    public void setIdVeterinari(Usuari idVeterinari) {
        this.idVeterinari = idVeterinari;
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
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "m13_ales_f_server.model.Cita[ id=" + id + " ]";
    }
}
