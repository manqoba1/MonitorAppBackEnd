/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.util;

/**
 *
 * @author aubreymalabie
 */
import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.gate.PhotoServlet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class FileUtility {

    private static final Logger logger = Logger.getLogger(FileUtility.class.getName());

    public static File getFile(String data) {
        Random rand = new Random(System.currentTimeMillis());
        //TODO restore after zip testing
        File dir = MonitorProperties.getTemporaryDir();
        File file = new File(dir, "x" + System.currentTimeMillis() + "_" + rand.nextInt(999999999) + ".data");
        try {
            FileUtils.writeStringToFile(file, data);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }
    
    public static List<String> getImageFilesTask(int companyID, int projectID, int projectSiteID,
            int projectSiteTaskID) throws Exception {
        List<String> list = new ArrayList<>();
        File rootDir = MonitorProperties.getImageDir();
        File ggRoot = new File(rootDir,RequestDTO.COMPANY_DIR + companyID);
        File dir = null;

        dir = new File(ggRoot, RequestDTO.PROJECT_DIR + projectID);
        if (!dir.exists()) {
            return list;
        }

        File site = new File(dir, RequestDTO.PROJECT_SITE_DIR + projectSiteID);
        if (!site.exists()) {
            return list;
        }
        File target = new File(site, RequestDTO.TASK_DIR + projectSiteTaskID);
        if (!target.exists()) {
            return list;
        }
        File[] files = target.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            list.add(file.getName());
        }

        Collections.reverse(list);
        logger.log(Level.OFF, "site image dir: {0}", dir.getAbsolutePath());

        return list;
    }

    public static List<String> getImageFilesSite(int companyID, int projectID, int projectSiteID) throws Exception {
        List<String> list = new ArrayList<>();
        File rootDir = MonitorProperties.getImageDir();
        File ggRoot = new File(RequestDTO.COMPANY_DIR + companyID);
        File dir = null;

        dir = new File(ggRoot, RequestDTO.PROJECT_DIR + projectID);
        if (!dir.exists()) {
            return list;
        }

        File target = new File(dir, RequestDTO.PROJECT_SITE_DIR + projectSiteID);
        if (!target.exists()) {
            return list;
        }
        File[] files = target.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            list.add(file.getName());
        }

        Collections.reverse(list);
        logger.log(Level.OFF, "site image dir: {0}", dir.getAbsolutePath());

        return list;
    }


}
