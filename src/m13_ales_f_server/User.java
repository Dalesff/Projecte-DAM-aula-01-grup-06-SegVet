package m13_ales_f_server;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author David Ales Fernandez
 */

/**
 Exemple de consulta para añadir usuaris:
 INSERT INTO usuaris (email, password, nom, cognom1, cognom2, direccio, telefon, rol)
 VALUES ('usuario@example.com', 'contraseña', 'Nombre', 'Apellido1', 'Apellido2', 'Dirección', 'Teléfono', 'USUARI');
*/

@Entity
@Table(name = "usuaris")
public class User {
    
    public enum Rol {
        USUARI,
        ADMIN,
        VETERINARI
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String nom;
    private String cognom1;
    private String cognom2;
    private String direccio;
    private String telefon;
    
    //TODO: private List<Mascota> mascotas = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    
    public User() {
        // constructor vacío requerido por JPA
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the cognom1
     */
    public String getCognom1() {
        return cognom1;
    }

    /**
     * @param cognom1 the cognom1 to set
     */
    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    /**
     * @return the cognom2
     */
    public String getCognom2() {
        return cognom2;
    }

    /**
     * @param cognom2 the cognom2 to set
     */
    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    /**
     * @return the direccio
     */
    public String getDireccio() {
        return direccio;
    }

    /**
     * @param direccio the direccio to set
     */
    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    /**
     * @return the telefon
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * @param telefon the telefon to set
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * @return the rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    // TODO: Mascotas
    /*
    @OneToMany(mappedBy = "usuari")
    public List<Mascota> getMascotas() {
        return mascotas;
    }
    
    public void setMascotas(List<Mascota> llistat) {
        this.mascotas=llistat;
    } 
    */
    
}
