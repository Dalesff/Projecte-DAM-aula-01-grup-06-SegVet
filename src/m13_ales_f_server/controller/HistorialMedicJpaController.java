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
import m13_ales_f_server.model.Mascota;
import m13_ales_f_server.model.EntradaHistorial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import m13_ales_f_server.controller.exceptions.NonexistentEntityException;
import m13_ales_f_server.model.HistorialMedic;

/**
 *
 * @author David Ales Fernandez
 */
public class HistorialMedicJpaController implements Serializable {

    public HistorialMedicJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialMedic historialMedic) {
        if (historialMedic.getEntradaHistorialCollection() == null) {
            historialMedic.setEntradaHistorialCollection(new ArrayList<EntradaHistorial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascota idMascota = historialMedic.getIdMascota();
            if (idMascota != null) {
                idMascota = em.getReference(idMascota.getClass(), idMascota.getId());
                historialMedic.setIdMascota(idMascota);
            }
            Collection<EntradaHistorial> attachedEntradaHistorialCollection = new ArrayList<EntradaHistorial>();
            for (EntradaHistorial entradaHistorialCollectionEntradaHistorialToAttach : historialMedic.getEntradaHistorialCollection()) {
                entradaHistorialCollectionEntradaHistorialToAttach = em.getReference(entradaHistorialCollectionEntradaHistorialToAttach.getClass(), entradaHistorialCollectionEntradaHistorialToAttach.getId());
                attachedEntradaHistorialCollection.add(entradaHistorialCollectionEntradaHistorialToAttach);
            }
            historialMedic.setEntradaHistorialCollection(attachedEntradaHistorialCollection);
            em.persist(historialMedic);
            if (idMascota != null) {
                HistorialMedic oldIdHistorialOfIdMascota = idMascota.getIdHistorial();
                if (oldIdHistorialOfIdMascota != null) {
                    oldIdHistorialOfIdMascota.setIdMascota(null);
                    oldIdHistorialOfIdMascota = em.merge(oldIdHistorialOfIdMascota);
                }
                idMascota.setIdHistorial(historialMedic);
                idMascota = em.merge(idMascota);
            }
            for (EntradaHistorial entradaHistorialCollectionEntradaHistorial : historialMedic.getEntradaHistorialCollection()) {
                HistorialMedic oldIdHistorialOfEntradaHistorialCollectionEntradaHistorial = entradaHistorialCollectionEntradaHistorial.getIdHistorial();
                entradaHistorialCollectionEntradaHistorial.setIdHistorial(historialMedic);
                entradaHistorialCollectionEntradaHistorial = em.merge(entradaHistorialCollectionEntradaHistorial);
                if (oldIdHistorialOfEntradaHistorialCollectionEntradaHistorial != null) {
                    oldIdHistorialOfEntradaHistorialCollectionEntradaHistorial.getEntradaHistorialCollection().remove(entradaHistorialCollectionEntradaHistorial);
                    oldIdHistorialOfEntradaHistorialCollectionEntradaHistorial = em.merge(oldIdHistorialOfEntradaHistorialCollectionEntradaHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialMedic historialMedic) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialMedic persistentHistorialMedic = em.find(HistorialMedic.class, historialMedic.getId());
            Mascota idMascotaOld = persistentHistorialMedic.getIdMascota();
            Mascota idMascotaNew = historialMedic.getIdMascota();
            Collection<EntradaHistorial> entradaHistorialCollectionOld = persistentHistorialMedic.getEntradaHistorialCollection();
            Collection<EntradaHistorial> entradaHistorialCollectionNew = historialMedic.getEntradaHistorialCollection();
            if (idMascotaNew != null) {
                idMascotaNew = em.getReference(idMascotaNew.getClass(), idMascotaNew.getId());
                historialMedic.setIdMascota(idMascotaNew);
            }
            Collection<EntradaHistorial> attachedEntradaHistorialCollectionNew = new ArrayList<EntradaHistorial>();
            for (EntradaHistorial entradaHistorialCollectionNewEntradaHistorialToAttach : entradaHistorialCollectionNew) {
                entradaHistorialCollectionNewEntradaHistorialToAttach = em.getReference(entradaHistorialCollectionNewEntradaHistorialToAttach.getClass(), entradaHistorialCollectionNewEntradaHistorialToAttach.getId());
                attachedEntradaHistorialCollectionNew.add(entradaHistorialCollectionNewEntradaHistorialToAttach);
            }
            entradaHistorialCollectionNew = attachedEntradaHistorialCollectionNew;
            historialMedic.setEntradaHistorialCollection(entradaHistorialCollectionNew);
            historialMedic = em.merge(historialMedic);
            if (idMascotaOld != null && !idMascotaOld.equals(idMascotaNew)) {
                idMascotaOld.setIdHistorial(null);
                idMascotaOld = em.merge(idMascotaOld);
            }
            if (idMascotaNew != null && !idMascotaNew.equals(idMascotaOld)) {
                HistorialMedic oldIdHistorialOfIdMascota = idMascotaNew.getIdHistorial();
                if (oldIdHistorialOfIdMascota != null) {
                    oldIdHistorialOfIdMascota.setIdMascota(null);
                    oldIdHistorialOfIdMascota = em.merge(oldIdHistorialOfIdMascota);
                }
                idMascotaNew.setIdHistorial(historialMedic);
                idMascotaNew = em.merge(idMascotaNew);
            }
            for (EntradaHistorial entradaHistorialCollectionOldEntradaHistorial : entradaHistorialCollectionOld) {
                if (!entradaHistorialCollectionNew.contains(entradaHistorialCollectionOldEntradaHistorial)) {
                    entradaHistorialCollectionOldEntradaHistorial.setIdHistorial(null);
                    entradaHistorialCollectionOldEntradaHistorial = em.merge(entradaHistorialCollectionOldEntradaHistorial);
                }
            }
            for (EntradaHistorial entradaHistorialCollectionNewEntradaHistorial : entradaHistorialCollectionNew) {
                if (!entradaHistorialCollectionOld.contains(entradaHistorialCollectionNewEntradaHistorial)) {
                    HistorialMedic oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial = entradaHistorialCollectionNewEntradaHistorial.getIdHistorial();
                    entradaHistorialCollectionNewEntradaHistorial.setIdHistorial(historialMedic);
                    entradaHistorialCollectionNewEntradaHistorial = em.merge(entradaHistorialCollectionNewEntradaHistorial);
                    if (oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial != null && !oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial.equals(historialMedic)) {
                        oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial.getEntradaHistorialCollection().remove(entradaHistorialCollectionNewEntradaHistorial);
                        oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial = em.merge(oldIdHistorialOfEntradaHistorialCollectionNewEntradaHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialMedic.getId();
                if (findHistorialMedic(id) == null) {
                    throw new NonexistentEntityException("The historialMedic with id " + id + " no longer exists.");
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
            HistorialMedic historialMedic;
            try {
                historialMedic = em.getReference(HistorialMedic.class, id);
                historialMedic.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialMedic with id " + id + " no longer exists.", enfe);
            }
            Mascota idMascota = historialMedic.getIdMascota();
            if (idMascota != null) {
                idMascota.setIdHistorial(null);
                idMascota = em.merge(idMascota);
            }
            Collection<EntradaHistorial> entradaHistorialCollection = historialMedic.getEntradaHistorialCollection();
            for (EntradaHistorial entradaHistorialCollectionEntradaHistorial : entradaHistorialCollection) {
                entradaHistorialCollectionEntradaHistorial.setIdHistorial(null);
                entradaHistorialCollectionEntradaHistorial = em.merge(entradaHistorialCollectionEntradaHistorial);
            }
            em.remove(historialMedic);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialMedic> findHistorialMedicEntities() {
        return findHistorialMedicEntities(true, -1, -1);
    }

    public List<HistorialMedic> findHistorialMedicEntities(int maxResults, int firstResult) {
        return findHistorialMedicEntities(false, maxResults, firstResult);
    }

    private List<HistorialMedic> findHistorialMedicEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialMedic.class));
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

    public HistorialMedic findHistorialMedic(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialMedic.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialMedicCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialMedic> rt = cq.from(HistorialMedic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
