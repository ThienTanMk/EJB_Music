/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package jav.music.sb;

import jakarta.ejb.Local;

@Local
public interface NewSessionBeanLocal {

    void say();

    void sayHello(String name);
    
}
