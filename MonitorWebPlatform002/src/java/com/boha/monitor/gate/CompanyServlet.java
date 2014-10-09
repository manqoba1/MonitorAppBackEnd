/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.gate;

import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import com.boha.monitor.util.DataException;
import com.boha.monitor.util.DataUtil;
import com.boha.monitor.util.ListUtil;
import com.boha.monitor.util.PlatformUtil;
import com.boha.monitor.util.TrafficCop;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author CodeTribe1
 */
@WebServlet(name = "CompanyServlet", urlPatterns = {"/scompany"})
public class CompanyServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    DataUtil dataUtil;
    @EJB
    ListUtil listUtil;
    @EJB
    PlatformUtil platformUtil;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        long start = System.currentTimeMillis();
        ResponseDTO resp = new ResponseDTO();
        Gson gson = new Gson();
        RequestDTO dto = getRequest(request);
        if (dto == null) {
            PrintWriter out = response.getWriter();
            resp.setStatusCode(ResponseDTO.UNKNOWN_REQUEST);
            resp.setMessage("Bad request, ignored");
            out.println(gson.toJson(resp));
            out.close();
            platformUtil.addErrorStore(ResponseDTO.UNKNOWN_REQUEST, "Bad request, ignored", "CompanyServlet");
            return;
        }
        try {
            resp = TrafficCop.processRequest(dto, dataUtil, listUtil);
        } catch (DataException e) {
            resp.setStatusCode(ResponseDTO.SERVER_ERROR);
            resp.setMessage("Error: Something wrong with the server");
            platformUtil.addErrorStore(ResponseDTO.SERVER_ERROR, e.getDescription(), "CompanyServlet");
        } catch (Exception e) {
            resp.setStatusCode(ResponseDTO.SERVER_ERROR);
            resp.setMessage("Error: Something wrong with the server");
            platformUtil.addErrorStore(ResponseDTO.SERVER_ERROR, e.getMessage(), "CompanyServlet");
        } finally {
            String json = gson.toJson(resp);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(json);
            out.close();
            long end = System.currentTimeMillis();
            LOG.log(Level.INFO, "CompanyServlet done, elapsed: {0} seconds", getElapsed(start, end));
        }

    }

    private double getElapsed(long start, long end) {
        BigDecimal d = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return d.doubleValue();
    }

    private RequestDTO getRequest(HttpServletRequest req) {
        Gson gson = new Gson();
        RequestDTO dto = null;
        try {
            dto = gson.fromJson(req.getParameter("JSON"), RequestDTO.class);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "JSON input error", ex);
        }

        return dto;
    }

    private static final Logger LOG = Logger.getLogger(CompanyServlet.class.getSimpleName());

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
