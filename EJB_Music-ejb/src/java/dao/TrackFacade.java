/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Track;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author admin
 */
@Stateless
public class TrackFacade extends AbstractFacade<Track> implements TrackFacadeLocal {

    @PersistenceContext(unitName = "EJB_Music-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrackFacade() {
        super(Track.class);
    }
    @Override
    public List<Track> findByUserId(String userId) {
        return em.createNamedQuery("Track.findByUserId", Track.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }
}
