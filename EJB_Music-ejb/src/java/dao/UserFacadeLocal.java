/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import entity.User;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author admin
 */
@Local
public interface UserFacadeLocal {

    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(Object id);    
    
    User findByUsername(String username);


    List<User> findAll();

    List<User> findRange(int[] range);

    int count();
    
}
