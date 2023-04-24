/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m13_ales_f_server.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import m13_ales_f_server.model.HistorialMedic;
import m13_ales_f_server.model.Usuari;
import m13_ales_f_server.model.Cita;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import m13_ales_f_server.controller.exceptions.NonexistentEntityException;
import m13_ales_f_server.model.Mascota;

/**
 *
 * @author David Ales Fernandez
 */
public class MascotaJpaController implements Serializable {

    public MascotaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mascota mascota) {
        if (mascota.getCitaCollection() == null) {
            mascota.setCitaCollection(new ArrayList<Cita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialMedic idHistorial = mascota.getIdHistorial();
            if (idHistorial != null) {
                idHistorial = em.getReference(idHistorial.getClass(), idHistorial.getId());
                mascota.setIdHistorial(idHistorial);
            }
            Usuari idUsuari = mascota.getIdUsuari();
            if (idUsuari != null) {
                idUsuari = em.getReference(idUsuari.getClass(), idUsuari.getId());
                mascota.setIdUsuari(idUsuari);
            }
            Collection<Cita> attachedCitaCollection = new ArrayList<Cita>();
            for (Cita citaCollectionCitaToAttach : mascota.getCitaCollection()) {
                citaCollectionCitaToAttach = em.getReference(citaCollectionCitaToAttach.getClass(), citaCollectionCitaToAttach.getId());
                attachedCitaCollection.add(citaCollectionCitaToAttach);
            }
            mascota.setCitaCollection(attachedCitaCollection);
            em.persist(mascota);
            if (idHistorial != null) {
                Mascota oldIdMascotaOfIdHistorial = idHistorial.getIdMascota();
                if (oldIdMascotaOfIdHistorial != null) {
                    oldIdMascotaOfIdHistorial.setIdHistorial(null);
                    oldIdMascotaOfIdHistorial = em.merge(oldIdMascotaOfIdHistorial);
                }
                idHistorial.setIdMascota(mascota);
                idHistorial = em.merge(idHistorial);
            }
            if (idUsuari != null) {
                idUsuari.getMascotaCollection().add(mascota);
                idUsuari = em.merge(idUsuari);
            }
            for (Cita citaCollectionCita : mascota.getCitaCollection()) {
                Mascota oldIdMascotaOfCitaCollectionCita = citaCollectionCita.getIdMascota();
                citaCollectionCita.setIdMascota(mascota);
                citaCollectionCita = em.merge(citaCollectionCita);
                if (oldIdMascotaOfCitaCollectionCita != null) {
                    oldIdMascotaOfCitaCollectionCita.getCitaCollection().remove(citaCollectionCita);
                    oldIdMascotaOfCitaCollectionCita = em.merge(oldIdMascotaOfCitaCollectionCita);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mascota mascota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascota persistentMascota = em.find(Mascota.class, mascota.getId());
            HistorialMedic idHistorialOld = persistentMascota.getIdHistorial();
            HistorialMedic idHistorialNew = mascota.getIdHistorial();
            Usuari idUsuariOld = persistentMascota.getIdUsuari();
            Usuari idUsuariNew = mascota.getIdUsuari();
            Collection<Cita> citaCollectionOld = persistentMascota.getCitaCollection();
            Collection<Cita> citaCollectionNew = mascota.getCitaCollection();
            if (idHistorialNew != null) {
                idHistorialNew = em.getReference(idHistorialNew.getClass(), idHistorialNew.getId());
                mascota.setIdHistorial(idHistorialNew);
            }
            if (idUsuariNew != null) {
                idUsuariNew = em.getReference(idUsuariNew.getClass(), idUsuariNew.getId());
                mascota.setIdUsuari(idUsuariNew);
            }
            Collection<Cita> attachedCitaCollectionNew = new ArrayList<Cita>();
            for (Cita citaCollectionNewCitaToAttach : citaCollectionNew) {
                citaCollectionNewCitaToAttach = em.getReference(citaCollectionNewCitaToAttach.getClass(), citaCollectionNewCitaToAttach.getId());
                attachedCitaCollectionNew.add(citaCollectionNewCitaToAttach);
            }
            citaCollectionNew = attachedCitaCollectionNew;
            mascota.setCitaCollection(citaCollectionNew);
            mascota = em.merge(mascota);
            if (idHistorialOld != null && !idHistorialOld.equals(idHistorialNew)) {
                idHistorialOld.setIdMascota(null);
                idHistorialOld = em.merge(idHistorialOld);
            }
            if (idHistorialNew != null && !idHistorialNew.equals(idHistorialOld)) {
                Mascota oldIdMascotaOfIdHistorial = idHistorialNew.getIdMascota();
                if (oldIdMascotaOfIdHistorial != null) {
                    oldIdMascotaOfIdHistorial.setIdHistorial(null);
                    oldIdMascotaOfIdHistorial = em.merge(oldIdMascotaOfIdHistorial);
                }
                idHistorialNew.setIdMascota(mascota);
                idHistorialNew = em.merge(idHistorialNew);
            }
            if (idUsuariOld != null && !idUsuariOld.equals(idUsuariNew)) {
                idUsuariOld.getMascotaCollection().remove(mascota);
                idUsuariOld = em.merge(idUsuariOld);
            }
            if (idUsuariNew != null && !idUsuariNew.equals(idUsuariOld)) {
                idUsuariNew.getMascotaCollection().add(mascota);
                idUsuariNew = em.merge(idUsuariNew);
            }
            for (Cita citaCollectionOldCita : citaCollectionOld) {
                if (!citaCollectionNew.contains(citaCollectionOldCita)) {
                    citaCollectionOldCita.setIdMascota(null);
                    citaCollectionOldCita = em.merge(citaCollectionOldCita);
                }
            }
            for (Cita citaCollectionNewCita : citaCollectionNew) {
                if (!citaCollectionOld.contains(citaCollectionNewCita)) {
                    Mascota oldIdMascotaOfCitaCollectionNewCita = citaCollectionNewCita.getIdMascota();
                    citaCollectionNewCita.setIdMascota(mascota);
                    citaCollectionNewCita = em.merge(citaCollectionNewCita);
                    if (oldIdMascotaOfCitaCollectionNewCita != null && !oldIdMascotaOfCitaCollectionNewCita.equals(mascota)) {
                        oldIdMascotaOfCitaCollectionNewCita.getCitaCollection().remove(citaCollectionNewCita);
                        oldIdMascotaOfCitaCollectionNewCita = em.merge(oldIdMascotaOfCitaCollectionNewCita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mascota.getId();
                if (findMascota(id) == null) {
                    throw new NonexistentEntityException("The mascota with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascota mascota;
            try {
                mascota = em.getReference(Mascota.class, id);
                mascota.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mascota with id " + id + " no longer exists.", enfe);
            }
            HistorialMedic idHistorial = mascota.getIdHistorial();
            if (idHistorial != null) {
                idHistorial.setIdMascota(null);
                idHistorial = em.merge(idHistorial);
            }
            Usuari idUsuari = mascota.getIdUsuari();
            if (idUsuari != null) {
                idUsuari.getMascotaCollection().remove(mascota);
                idUsuari = em.merge(idUsuari);
            }
            Collection<Cita> citaCollection = mascota.getCitaCollection();
            for (Cita citaCollectionCita : citaCollection) {
                citaCollectionCita.setIdMascota(null);
                citaCollectionCita = em.merge(citaCollectionCita);
            }
            em.remove(mascota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mascota> findMascotaEntities() {
        return findMascotaEntities(true, -1, -1);
    }

    public List<Mascota> findMascotaEntities(int maxResults, int firstResult) {
        return findMascotaEntities(false, maxResults, firstResult);
    }

    private List<Mascota> findMascotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mascota.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Mascota findMascota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mascota.class, id);
        } finally {
            em.close();
        }
    }

    public int getMascotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mascota> rt = cq.from(Mascota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
