/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model;

import entity.Track;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author admin
 */
@Local
public interface TrackFacadeLocal {

    void create(Track track);

    void edit(Track track);

    void remove(Track track);

    Track find(Object id);

    List<Track> findAll();

    List<Track> findRange(int[] range);

    int count();
    
}
