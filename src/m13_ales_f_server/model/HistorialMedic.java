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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author David Ales Fernandez
 */
@Entity
@Table(name = "historial_medic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistorialMedic.findAll", query = "SELECT h FROM HistorialMedic h"),
    @NamedQuery(name = "HistorialMedic.findById", query = "SELECT h FROM HistorialMedic h WHERE h.id = :id")})
public class HistorialMedic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    @OneToOne
    private Mascota idMascota;
    @OneToMany(mappedBy = "idHistorial")
    private Collection<EntradaHistorial> entradaHistorialCollection;

    public HistorialMedic() {
    }

    public HistorialMedic(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mascota getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Mascota idMascota) {
        this.idMascota = idMascota;
    }

    @XmlTransient
    public Collection<EntradaHistorial> getEntradaHistorialCollection() {
        return entradaHistorialCollection;
    }

    public void setEntradaHistorialCollection(Collection<EntradaHistorial> entradaHistorialCollection) {
        this.entradaHistorialCollection = entradaHistorialCollection;
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
        if (!(object instanceof HistorialMedic)) {
            return false;
        }
        HistorialMedic other = (HistorialMedic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "m13_ales_f_server.model.HistorialMedic[ id=" + id + " ]";
    }
}
