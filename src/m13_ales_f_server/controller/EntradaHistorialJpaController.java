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
import m13_ales_f_server.model.EntradaHistorial;
import m13_ales_f_server.model.HistorialMedic;

/**
 *
 * @author David Ales Fernandez
 */
public class EntradaHistorialJpaController implements Serializable {

    public EntradaHistorialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EntradaHistorial entradaHistorial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialMedic idHistorial = entradaHistorial.getIdHistorial();
            if (idHistorial != null) {
                idHistorial = em.getReference(idHistorial.getClass(), idHistorial.getId());
                entradaHistorial.setIdHistorial(idHistorial);
            }
            em.persist(entradaHistorial);
            if (idHistorial != null) {
                idHistorial.getEntradaHistorialCollection().add(entradaHistorial);
                idHistorial = em.merge(idHistorial);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EntradaHistorial entradaHistorial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntradaHistorial persistentEntradaHistorial = em.find(EntradaHistorial.class, entradaHistorial.getId());
            HistorialMedic idHistorialOld = persistentEntradaHistorial.getIdHistorial();
            HistorialMedic idHistorialNew = entradaHistorial.getIdHistorial();
            if (idHistorialNew != null) {
                idHistorialNew = em.getReference(idHistorialNew.getClass(), idHistorialNew.getId());
                entradaHistorial.setIdHistorial(idHistorialNew);
            }
            entradaHistorial = em.merge(entradaHistorial);
            if (idHistorialOld != null && !idHistorialOld.equals(idHistorialNew)) {
                idHistorialOld.getEntradaHistorialCollection().remove(entradaHistorial);
                idHistorialOld = em.merge(idHistorialOld);
            }
            if (idHistorialNew != null && !idHistorialNew.equals(idHistorialOld)) {
                idHistorialNew.getEntradaHistorialCollection().add(entradaHistorial);
                idHistorialNew = em.merge(idHistorialNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entradaHistorial.getId();
                if (findEntradaHistorial(id) == null) {
                    throw new NonexistentEntityException("The entradaHistorial with id " + id + " no longer exists.");
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
            EntradaHistorial entradaHistorial;
            try {
                entradaHistorial = em.getReference(EntradaHistorial.class, id);
                entradaHistorial.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entradaHistorial with id " + id + " no longer exists.", enfe);
            }
            HistorialMedic idHistorial = entradaHistorial.getIdHistorial();
            if (idHistorial != null) {
                idHistorial.getEntradaHistorialCollection().remove(entradaHistorial);
                idHistorial = em.merge(idHistorial);
            }
            em.remove(entradaHistorial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EntradaHistorial> findEntradaHistorialEntities() {
        return findEntradaHistorialEntities(true, -1, -1);
    }

    public List<EntradaHistorial> findEntradaHistorialEntities(int maxResults, int firstResult) {
        return findEntradaHistorialEntities(false, maxResults, firstResult);
    }

    private List<EntradaHistorial> findEntradaHistorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EntradaHistorial.class));
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

    public EntradaHistorial findEntradaHistorial(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntradaHistorial.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntradaHistorialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntradaHistorial> rt = cq.from(EntradaHistorial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
