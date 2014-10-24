/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.gate;

import com.boha.monitor.dto.transfer.PhotoUploadDTO;
import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import com.boha.monitor.util.MonitorProperties;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

/**
 * This servlet accepts image files uploaded from CourseMaker devices and saves
 * them on disk according to the requestor's role.
 *
 * @author aubreyM
 */
@WebServlet(name = "PhotoServlet", urlPatterns = {"/photo"})
public class PhotoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        long start = System.currentTimeMillis();

        ResponseDTO ur = new ResponseDTO();
        String json;
        Gson gson = new Gson();
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                ur = downloadPhotos(request);
            } else {
                RequestDTO dto = getRequest(gson, request);
                switch (dto.getRequestType()) {
                    case RequestDTO.GET_SITE_IMAGE_FILENAMES:

                        break;
                    case RequestDTO.GET_TASK_IMAGE_FILENAMES:

                        break;
                    case RequestDTO.GET_STAFF_IMAGE_FILENAMES:

                        break;
                }

            }

        } catch (FileUploadException ex) {
            logger.log(Level.SEVERE, "File upload fucked", ex);
            ur.setStatusCode(111);
            ur.setMessage("Error. Unable to download file(s) sent. Contact Support");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Servlet file upload fucked", e);
            ur.setStatusCode(113);
            ur.setMessage("Error. Generic server exception");

        } finally {
            json = gson.toJson(ur);
            out.println(json);
            out.close();
            long end = System.currentTimeMillis();
            logger.log(Level.INFO, "PhotoServlet done, elapsed: {0} seconds", getElapsed(start, end));
        }
    }

    private ResponseDTO downloadPhotos(HttpServletRequest request) throws FileUploadException {
        logger.log(Level.INFO, "######### starting PHOTO DOWNLOAD process\n\n");
        ResponseDTO resp = new ResponseDTO();
        InputStream stream = null;
        File rootDir;
        try {
            rootDir = MonitorProperties.getImageDir();
            logger.log(Level.INFO, "rootDir - {0}", rootDir.getAbsolutePath());
            if (!rootDir.exists()) {
                rootDir.mkdir();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Properties file problem", ex);
            resp.setMessage("Server file unavailable. Please try later");
            resp.setStatusCode(114);

            return resp;
        }

        PhotoUploadDTO dto = null;
        Gson gson = new Gson();
        File companyDir = null, siteStaffDir = null, projectDir = null, projectSiteDir = null,
                projectSiteTaskDir = null;
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                stream = item.openStream();
                if (item.isFormField()) {
                    if (name.equalsIgnoreCase("JSON")) {
                        String json = Streams.asString(stream);
                        if (json != null) {
                            logger.log(Level.INFO, "picture with associated json: {0}", json);
                            dto = gson.fromJson(json, PhotoUploadDTO.class);
                            if (dto != null) {
                                companyDir = createCompanyDirectory(rootDir, companyDir, dto.getCompanyID());

                                if (dto.getProjectSiteStaffID() > 0) {
                                    siteStaffDir = createProjectSiteStaffDirectory(companyDir, siteStaffDir, dto.getProjectSiteStaffID());
                                }
                                if (dto.getProjectID() > 0) {
                                    projectDir = createProjectDirectory(companyDir, projectDir, dto.getProjectID());
                                }
                                if (dto.getProjectSiteID() > 0) {
                                    projectSiteDir = createProjectSiteDirectory(companyDir, projectSiteDir, dto.getProjectSiteID());
                                }
                                
                                if (dto.getProjectSiteTaskID() > 0) {
                                    projectSiteTaskDir = createProjectSiteTaskDirectory(
                                            projectSiteDir, projectSiteTaskDir, dto.getProjectSiteTaskID());
                                }

                            }
                        } else {
                            logger.log(Level.WARNING, "JSON input seems fucked up! is NULL..");
                        }
                    }
                } else {
                    logger.log(Level.OFF, "name of item to be processed into file: {0}", name);
                    File imageFile = null;
                    if (dto == null) {
                        continue;
                    }
                    DateTime dt = new DateTime();
                    String suffix = "" + dto.getProjectSiteStaffID() + ".jpg";

                    switch (dto.getPictureType()) {
                        case PhotoUploadDTO.SITE_IMAGE:
                            imageFile = new File(projectSiteDir, suffix);
                            break;
                        case PhotoUploadDTO.TASK_IMAGE:
                            imageFile = new File(projectSiteTaskDir, suffix);
                            break;
                        case PhotoUploadDTO.SITE_STAFF_IMAGE:
                            imageFile = new File(siteStaffDir, suffix);
                            break;
                    }
                    logger.log(Level.WARNING, "Photo downloaded - {0}", stream.available() + "\n" + siteStaffDir.getAbsolutePath());
                    writeFile(stream, imageFile);
                    resp.setMessage("Photo downloaded from mobile app ");
                    resp.setStatusCode(0);
                }
            }

        } catch (FileUploadException | IOException | JsonSyntaxException ex) {
            logger.log(Level.SEVERE, "Servlet failed on IOException, images NOT uploaded-{0}", ex);
            throw new FileUploadException();
        }

        return resp;
    }

    private File createProjectSiteTaskDirectory(File projectSiteDir, File taskDir, int id) throws IOException {
        logger.log(Level.INFO, "task photo to be downloaded");
        taskDir = new File(projectSiteDir, RequestDTO.TASK_DIR + id);
        if (!taskDir.exists()) {
            taskDir.mkdir();
            FileUtils.forceMkdir(taskDir);
            logger.log(Level.INFO, "task  directory created - {0}",
                    taskDir.getAbsolutePath());

        }
        return taskDir;
    }

    private File createProjectSiteDirectory(File projectDir, File projectSiteDir, int id) throws IOException {
        logger.log(Level.INFO, "projectSite photo to be downloaded");
        projectSiteDir = new File(projectDir, RequestDTO.PROJECT_SITE_DIR + id);
        if (!projectSiteDir.exists()) {
            projectSiteDir.mkdir();
            FileUtils.forceMkdir(projectSiteDir);
            logger.log(Level.INFO, "project site  directory created - {0}",
                    projectSiteDir.getAbsolutePath());

        }
        return projectSiteDir;
    }

    private File createProjectDirectory(File companyDir, File projectDir, int id) throws IOException {
        projectDir = new File(companyDir, RequestDTO.PROJECT_DIR + id);
        logger.log(Level.INFO, "just after new {0}", projectDir);
        if (!projectDir.exists()) {
            projectDir.mkdir();
            FileUtils.forceMkdir(projectDir);
            logger.log(Level.INFO, "projectDir created - {0}",
                    projectDir.getAbsolutePath());

        }
        return projectDir;
    }

    private File createCompanyDirectory(File rootDir, File companyDir, int id) throws IOException {
        companyDir = new File(rootDir, RequestDTO.COMPANY_DIR + id);
        if (!companyDir.exists()) {
            companyDir.mkdir();
            FileUtils.forceMkdir(companyDir);
            logger.log(Level.INFO, "company directory created - {0}",
                    companyDir.getAbsolutePath());
        }
        return companyDir;
    }

    private File createProjectSiteStaffDirectory(File companyDir, File siteStaffDir, int id) throws IOException {
        siteStaffDir = new File(companyDir, RequestDTO.SITE_STAFF_DIR);
        if (!siteStaffDir.exists()) {
            siteStaffDir.mkdir();
            FileUtils.forceMkdir(siteStaffDir);
            logger.log(Level.INFO, "Project Site Staff directory created - {0}",
                    siteStaffDir.getAbsolutePath());
        }
        return siteStaffDir;
    }

    private void writeFile(InputStream stream, File imageFile) throws FileNotFoundException, IOException {

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            int read;
            byte[] bytes = new byte[2048];
            while ((read = stream.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
            stream.close();
            fos.flush();
        }

        logger.log(Level.INFO, "\n### File downloaded: {0} size: {1}",
                new Object[]{imageFile.getAbsolutePath(), imageFile.length()});
    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }
    static final Logger logger = Logger.getLogger("PhotoServlet");

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

    private RequestDTO getRequest(Gson gson, HttpServletRequest req) {

        String json = req.getParameter("JSON");
        RequestDTO cr = gson.fromJson(json, RequestDTO.class);

        if (cr == null) {
            cr = new RequestDTO();
        }

        return cr;
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
