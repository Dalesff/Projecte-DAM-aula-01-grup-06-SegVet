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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import m13_ales_f_server.controller.exceptions.NonexistentEntityException;
import m13_ales_f_server.model.Cita;
import m13_ales_f_server.model.Usuari;

/**
 *
 * @author David Ales Fernandez
 */
public class UsuariJpaController implements Serializable {

    public UsuariJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuari usuari) {
        if (usuari.getMascotaCollection() == null) {
            usuari.setMascotaCollection(new ArrayList<Mascota>());
        }
        if (usuari.getCitaCollection() == null) {
            usuari.setCitaCollection(new ArrayList<Cita>());
        }
        if (usuari.getCitaCollection1() == null) {
            usuari.setCitaCollection1(new ArrayList<Cita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Mascota> attachedMascotaCollection = new ArrayList<Mascota>();
            for (Mascota mascotaCollectionMascotaToAttach : usuari.getMascotaCollection()) {
                mascotaCollectionMascotaToAttach = em.getReference(mascotaCollectionMascotaToAttach.getClass(), mascotaCollectionMascotaToAttach.getId());
                attachedMascotaCollection.add(mascotaCollectionMascotaToAttach);
            }
            usuari.setMascotaCollection(attachedMascotaCollection);
            Collection<Cita> attachedCitaCollection = new ArrayList<Cita>();
            for (Cita citaCollectionCitaToAttach : usuari.getCitaCollection()) {
                citaCollectionCitaToAttach = em.getReference(citaCollectionCitaToAttach.getClass(), citaCollectionCitaToAttach.getId());
                attachedCitaCollection.add(citaCollectionCitaToAttach);
            }
            usuari.setCitaCollection(attachedCitaCollection);
            Collection<Cita> attachedCitaCollection1 = new ArrayList<Cita>();
            for (Cita citaCollection1CitaToAttach : usuari.getCitaCollection1()) {
                citaCollection1CitaToAttach = em.getReference(citaCollection1CitaToAttach.getClass(), citaCollection1CitaToAttach.getId());
                attachedCitaCollection1.add(citaCollection1CitaToAttach);
            }
            usuari.setCitaCollection1(attachedCitaCollection1);
            em.persist(usuari);
            for (Mascota mascotaCollectionMascota : usuari.getMascotaCollection()) {
                Usuari oldIdUsuariOfMascotaCollectionMascota = mascotaCollectionMascota.getIdUsuari();
                mascotaCollectionMascota.setIdUsuari(usuari);
                mascotaCollectionMascota = em.merge(mascotaCollectionMascota);
                if (oldIdUsuariOfMascotaCollectionMascota != null) {
                    oldIdUsuariOfMascotaCollectionMascota.getMascotaCollection().remove(mascotaCollectionMascota);
                    oldIdUsuariOfMascotaCollectionMascota = em.merge(oldIdUsuariOfMascotaCollectionMascota);
                }
            }
            for (Cita citaCollectionCita : usuari.getCitaCollection()) {
                Usuari oldIdDuenoOfCitaCollectionCita = citaCollectionCita.getIdDueno();
                citaCollectionCita.setIdDueno(usuari);
                citaCollectionCita = em.merge(citaCollectionCita);
                if (oldIdDuenoOfCitaCollectionCita != null) {
                    oldIdDuenoOfCitaCollectionCita.getCitaCollection().remove(citaCollectionCita);
                    oldIdDuenoOfCitaCollectionCita = em.merge(oldIdDuenoOfCitaCollectionCita);
                }
            }
            for (Cita citaCollection1Cita : usuari.getCitaCollection1()) {
                Usuari oldIdVeterinariOfCitaCollection1Cita = citaCollection1Cita.getIdVeterinari();
                citaCollection1Cita.setIdVeterinari(usuari);
                citaCollection1Cita = em.merge(citaCollection1Cita);
                if (oldIdVeterinariOfCitaCollection1Cita != null) {
                    oldIdVeterinariOfCitaCollection1Cita.getCitaCollection1().remove(citaCollection1Cita);
                    oldIdVeterinariOfCitaCollection1Cita = em.merge(oldIdVeterinariOfCitaCollection1Cita);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuari usuari) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuari persistentUsuari = em.find(Usuari.class, usuari.getId());
            Collection<Mascota> mascotaCollectionOld = persistentUsuari.getMascotaCollection();
            Collection<Mascota> mascotaCollectionNew = usuari.getMascotaCollection();
            Collection<Cita> citaCollectionOld = persistentUsuari.getCitaCollection();
            Collection<Cita> citaCollectionNew = usuari.getCitaCollection();
            Collection<Cita> citaCollection1Old = persistentUsuari.getCitaCollection1();
            Collection<Cita> citaCollection1New = usuari.getCitaCollection1();
            Collection<Mascota> attachedMascotaCollectionNew = new ArrayList<Mascota>();
            for (Mascota mascotaCollectionNewMascotaToAttach : mascotaCollectionNew) {
                mascotaCollectionNewMascotaToAttach = em.getReference(mascotaCollectionNewMascotaToAttach.getClass(), mascotaCollectionNewMascotaToAttach.getId());
                attachedMascotaCollectionNew.add(mascotaCollectionNewMascotaToAttach);
            }
            mascotaCollectionNew = attachedMascotaCollectionNew;
            usuari.setMascotaCollection(mascotaCollectionNew);
            Collection<Cita> attachedCitaCollectionNew = new ArrayList<Cita>();
            for (Cita citaCollectionNewCitaToAttach : citaCollectionNew) {
                citaCollectionNewCitaToAttach = em.getReference(citaCollectionNewCitaToAttach.getClass(), citaCollectionNewCitaToAttach.getId());
                attachedCitaCollectionNew.add(citaCollectionNewCitaToAttach);
            }
            citaCollectionNew = attachedCitaCollectionNew;
            usuari.setCitaCollection(citaCollectionNew);
            Collection<Cita> attachedCitaCollection1New = new ArrayList<Cita>();
            for (Cita citaCollection1NewCitaToAttach : citaCollection1New) {
                citaCollection1NewCitaToAttach = em.getReference(citaCollection1NewCitaToAttach.getClass(), citaCollection1NewCitaToAttach.getId());
                attachedCitaCollection1New.add(citaCollection1NewCitaToAttach);
            }
            citaCollection1New = attachedCitaCollection1New;
            usuari.setCitaCollection1(citaCollection1New);
            usuari = em.merge(usuari);
            for (Mascota mascotaCollectionOldMascota : mascotaCollectionOld) {
                if (!mascotaCollectionNew.contains(mascotaCollectionOldMascota)) {
                    mascotaCollectionOldMascota.setIdUsuari(null);
                    mascotaCollectionOldMascota = em.merge(mascotaCollectionOldMascota);
                }
            }
            for (Mascota mascotaCollectionNewMascota : mascotaCollectionNew) {
                if (!mascotaCollectionOld.contains(mascotaCollectionNewMascota)) {
                    Usuari oldIdUsuariOfMascotaCollectionNewMascota = mascotaCollectionNewMascota.getIdUsuari();
                    mascotaCollectionNewMascota.setIdUsuari(usuari);
                    mascotaCollectionNewMascota = em.merge(mascotaCollectionNewMascota);
                    if (oldIdUsuariOfMascotaCollectionNewMascota != null && !oldIdUsuariOfMascotaCollectionNewMascota.equals(usuari)) {
                        oldIdUsuariOfMascotaCollectionNewMascota.getMascotaCollection().remove(mascotaCollectionNewMascota);
                        oldIdUsuariOfMascotaCollectionNewMascota = em.merge(oldIdUsuariOfMascotaCollectionNewMascota);
                    }
                }
            }
            for (Cita citaCollectionOldCita : citaCollectionOld) {
                if (!citaCollectionNew.contains(citaCollectionOldCita)) {
                    citaCollectionOldCita.setIdDueno(null);
                    citaCollectionOldCita = em.merge(citaCollectionOldCita);
                }
            }
            for (Cita citaCollectionNewCita : citaCollectionNew) {
                if (!citaCollectionOld.contains(citaCollectionNewCita)) {
                    Usuari oldIdDuenoOfCitaCollectionNewCita = citaCollectionNewCita.getIdDueno();
                    citaCollectionNewCita.setIdDueno(usuari);
                    citaCollectionNewCita = em.merge(citaCollectionNewCita);
                    if (oldIdDuenoOfCitaCollectionNewCita != null && !oldIdDuenoOfCitaCollectionNewCita.equals(usuari)) {
                        oldIdDuenoOfCitaCollectionNewCita.getCitaCollection().remove(citaCollectionNewCita);
                        oldIdDuenoOfCitaCollectionNewCita = em.merge(oldIdDuenoOfCitaCollectionNewCita);
                    }
                }
            }
            for (Cita citaCollection1OldCita : citaCollection1Old) {
                if (!citaCollection1New.contains(citaCollection1OldCita)) {
                    citaCollection1OldCita.setIdVeterinari(null);
                    citaCollection1OldCita = em.merge(citaCollection1OldCita);
                }
            }
            for (Cita citaCollection1NewCita : citaCollection1New) {
                if (!citaCollection1Old.contains(citaCollection1NewCita)) {
                    Usuari oldIdVeterinariOfCitaCollection1NewCita = citaCollection1NewCita.getIdVeterinari();
                    citaCollection1NewCita.setIdVeterinari(usuari);
                    citaCollection1NewCita = em.merge(citaCollection1NewCita);
                    if (oldIdVeterinariOfCitaCollection1NewCita != null && !oldIdVeterinariOfCitaCollection1NewCita.equals(usuari)) {
                        oldIdVeterinariOfCitaCollection1NewCita.getCitaCollection1().remove(citaCollection1NewCita);
                        oldIdVeterinariOfCitaCollection1NewCita = em.merge(oldIdVeterinariOfCitaCollection1NewCita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuari.getId();
                if (findUsuari(id) == null) {
                    throw new NonexistentEntityException("The usuari with id " + id + " no longer exists.");
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
            Usuari usuari;
            try {
                usuari = em.getReference(Usuari.class, id);
                usuari.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuari with id " + id + " no longer exists.", enfe);
            }
            Collection<Mascota> mascotaCollection = usuari.getMascotaCollection();
            for (Mascota mascotaCollectionMascota : mascotaCollection) {
                mascotaCollectionMascota.setIdUsuari(null);
                mascotaCollectionMascota = em.merge(mascotaCollectionMascota);
            }
            Collection<Cita> citaCollection = usuari.getCitaCollection();
            for (Cita citaCollectionCita : citaCollection) {
                citaCollectionCita.setIdDueno(null);
                citaCollectionCita = em.merge(citaCollectionCita);
            }
            Collection<Cita> citaCollection1 = usuari.getCitaCollection1();
            for (Cita citaCollection1Cita : citaCollection1) {
                citaCollection1Cita.setIdVeterinari(null);
                citaCollection1Cita = em.merge(citaCollection1Cita);
            }
            em.remove(usuari);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuari> findUsuariEntities() {
        return findUsuariEntities(true, -1, -1);
    }

    public List<Usuari> findUsuariEntities(int maxResults, int firstResult) {
        return findUsuariEntities(false, maxResults, firstResult);
    }

    private List<Usuari> findUsuariEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuari.class));
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

    public Usuari findUsuari(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuari.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuari> rt = cq.from(Usuari.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
