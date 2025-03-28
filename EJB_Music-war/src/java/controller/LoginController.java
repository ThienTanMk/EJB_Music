/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserFacadeLocal;
import entity.Track;
import entity.User;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author thuat
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login", "/register", "/logout"})
public class LoginController extends HttpServlet {

    @EJB
    private UserFacadeLocal userFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void loginPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin đăng ký
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userFacade.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {

            HttpServletRequest httpRequest = (HttpServletRequest) request;

            HttpSession session = httpRequest.getSession(false);

            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Username or password is incorrect");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    protected void logoutGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        session.removeAttribute("user");
        request.setAttribute("message", "Logout Successfully");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void registerPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userFacade.findByUsername(username);
        if (user != null) {
            request.setAttribute("error", "Username existed");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        userFacade.create(new User(username, password));
        request.setAttribute("message", "Register Successfully");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println(action);
        if (action.equals("/login")) {
            login(request, response);
        } else if ("/register".equals(action)) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else if ("/logout".equals(action)) {
            logoutGet(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        if (action.equals("/login")) {
            loginPost(request, response);
        } else if ("/register".equals(action)) {
            registerPost(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
