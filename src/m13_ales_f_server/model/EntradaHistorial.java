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
@Table(name = "entrada_historial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradaHistorial.findAll", query = "SELECT e FROM EntradaHistorial e"),
    @NamedQuery(name = "EntradaHistorial.findById", query = "SELECT e FROM EntradaHistorial e WHERE e.id = :id"),
    @NamedQuery(name = "EntradaHistorial.findByData", query = "SELECT e FROM EntradaHistorial e WHERE e.data = :data"),
    @NamedQuery(name = "EntradaHistorial.findByDescripcio", query = "SELECT e FROM EntradaHistorial e WHERE e.descripcio = :descripcio")})
public class EntradaHistorial implements Serializable {

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
    @JoinColumn(name = "id_historial", referencedColumnName = "id")
    @ManyToOne
    private HistorialMedic idHistorial;

    public EntradaHistorial() {
    }

    public EntradaHistorial(Integer id) {
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

    public HistorialMedic getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(HistorialMedic idHistorial) {
        this.idHistorial = idHistorial;
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
        if (!(object instanceof EntradaHistorial)) {
            return false;
        }
        EntradaHistorial other = (EntradaHistorial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "m13_ales_f_server.model.EntradaHistorial[ id=" + id + " ]";
    }
}
