/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m13_ales_f_server.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import m13_ales_f_server.controller.exceptions.NonexistentEntityException;
import m13_ales_f_server.model.Cita;
import m13_ales_f_server.model.Mascota;
import m13_ales_f_server.model.Usuari;

/**
 *
 * @author David Ales Fernandez
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascota idMascota = cita.getIdMascota();
            if (idMascota != null) {
                idMascota = em.getReference(idMascota.getClass(), idMascota.getId());
                cita.setIdMascota(idMascota);
            }
            Usuari idDueno = cita.getIdDueno();
            if (idDueno != null) {
                idDueno = em.getReference(idDueno.getClass(), idDueno.getId());
                cita.setIdDueno(idDueno);
            }
            Usuari idVeterinari = cita.getIdVeterinari();
            if (idVeterinari != null) {
                idVeterinari = em.getReference(idVeterinari.getClass(), idVeterinari.getId());
                cita.setIdVeterinari(idVeterinari);
            }
            em.persist(cita);
            if (idMascota != null) {
                idMascota.getCitaCollection().add(cita);
                idMascota = em.merge(idMascota);
            }
            if (idDueno != null) {
                idDueno.getCitaCollection().add(cita);
                idDueno = em.merge(idDueno);
            }
            if (idVeterinari != null) {
                idVeterinari.getCitaCollection().add(cita);
                idVeterinari = em.merge(idVeterinari);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cita persistentCita = em.find(Cita.class, cita.getId());
            Mascota idMascotaOld = persistentCita.getIdMascota();
            Mascota idMascotaNew = cita.getIdMascota();
            Usuari idDuenoOld = persistentCita.getIdDueno();
            Usuari idDuenoNew = cita.getIdDueno();
            Usuari idVeterinariOld = persistentCita.getIdVeterinari();
            Usuari idVeterinariNew = cita.getIdVeterinari();
            if (idMascotaNew != null) {
                idMascotaNew = em.getReference(idMascotaNew.getClass(), idMascotaNew.getId());
                cita.setIdMascota(idMascotaNew);
            }
            if (idDuenoNew != null) {
                idDuenoNew = em.getReference(idDuenoNew.getClass(), idDuenoNew.getId());
                cita.setIdDueno(idDuenoNew);
            }
            if (idVeterinariNew != null) {
                idVeterinariNew = em.getReference(idVeterinariNew.getClass(), idVeterinariNew.getId());
                cita.setIdVeterinari(idVeterinariNew);
            }
            cita = em.merge(cita);
            if (idMascotaOld != null && !idMascotaOld.equals(idMascotaNew)) {
                idMascotaOld.getCitaCollection().remove(cita);
                idMascotaOld = em.merge(idMascotaOld);
            }
            if (idMascotaNew != null && !idMascotaNew.equals(idMascotaOld)) {
                idMascotaNew.getCitaCollection().add(cita);
                idMascotaNew = em.merge(idMascotaNew);
            }
            if (idDuenoOld != null && !idDuenoOld.equals(idDuenoNew)) {
                idDuenoOld.getCitaCollection().remove(cita);
                idDuenoOld = em.merge(idDuenoOld);
            }
            if (idDuenoNew != null && !idDuenoNew.equals(idDuenoOld)) {
                idDuenoNew.getCitaCollection().add(cita);
                idDuenoNew = em.merge(idDuenoNew);
            }
            if (idVeterinariOld != null && !idVeterinariOld.equals(idVeterinariNew)) {
                idVeterinariOld.getCitaCollection().remove(cita);
                idVeterinariOld = em.merge(idVeterinariOld);
            }
            if (idVeterinariNew != null && !idVeterinariNew.equals(idVeterinariOld)) {
                idVeterinariNew.getCitaCollection().add(cita);
                idVeterinariNew = em.merge(idVeterinariNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cita.getId();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            Mascota idMascota = cita.getIdMascota();
            if (idMascota != null) {
                idMascota.getCitaCollection().remove(cita);
                idMascota = em.merge(idMascota);
            }
            Usuari idDueno = cita.getIdDueno();
            if (idDueno != null) {
                idDueno.getCitaCollection().remove(cita);
                idDueno = em.merge(idDueno);
            }
            Usuari idVeterinari = cita.getIdVeterinari();
            if (idVeterinari != null) {
                idVeterinari.getCitaCollection().remove(cita);
                idVeterinari = em.merge(idVeterinari);
            }
            em.remove(cita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
