/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.util;

import com.boha.monitor.data.CompanyStaff;
import com.boha.monitor.data.ProjectDiaryRecord;
import com.boha.monitor.data.ProjectSite;
import com.boha.monitor.data.ProjectSiteStaff;
import com.boha.monitor.data.ProjectSiteTask;
import com.boha.monitor.data.ProjectSiteTaskStatus;
import com.boha.monitor.dto.CompanyStaffDTO;
import com.boha.monitor.dto.ProjectDiaryRecordDTO;
import com.boha.monitor.dto.ProjectSiteDTO;
import com.boha.monitor.dto.ProjectSiteStaffDTO;
import com.boha.monitor.dto.ProjectSiteTaskDTO;
import com.boha.monitor.dto.ProjectSiteTaskStatusDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aubreyM
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ListUtil {

    @PersistenceContext
    EntityManager em;

    public ResponseDTO getCompanyStaff(Integer companyID) throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("CompanyStaff.findByCompany", CompanyStaff.class);
            q.setParameter("companyID", companyID);
            List<CompanyStaff> sList = q.getResultList();
            for (CompanyStaff cs : sList) {
                resp.getCompanyStaffList().add(new CompanyStaffDTO(cs));
            }
             log.log(Level.OFF, "company staff found: {0}", sList.size());       
         } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return resp;
    }

    public ResponseDTO getProjectData(Integer projectID) throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("ProjectSite.findByProject",
                    ProjectSite.class);
            q.setParameter("projectID", projectID);
            List<ProjectSite> pList = q.getResultList();
            ResponseDTO resp1 = getTasksByProject(projectID);          
            resp.setProjectDiaryRecordList(getDiariesByProject(projectID).getProjectDiaryRecordList());

            resp.setProjectSiteStaffList(getStaffByProject(projectID,
                    resp.getProjectDiaryRecordList(), 
                    resp1.getProjectSiteTaskStatusList()).getProjectSiteStaffList());
            
            for (ProjectSite site : pList) {
                ProjectSiteDTO s = new ProjectSiteDTO(site);
                for (ProjectSiteTaskDTO task : resp1.getProjectSiteTaskList()) {
                    if (Objects.equals(task.getProjectSiteID(), s.getProjectSiteID())) {
                        s.getProjectSiteTaskList().add(task);
                    }
                }

            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return resp;
    }

    public ResponseDTO getDiariesByProject(Integer projectID) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("ProjectDiaryRecord.findByProject", ProjectDiaryRecord.class);
            q.setParameter("projectID", projectID);
            List<ProjectDiaryRecord> pstList = q.getResultList();

            for (ProjectDiaryRecord pss : pstList) {
                resp.getProjectDiaryRecordList().add(new ProjectDiaryRecordDTO(pss));
            }
            log.log(Level.INFO, "diaries found: {0}", pstList.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get tasks\n" + getErrorString(e));
        }
        return resp;
    }
    public ResponseDTO getStaffByProject(Integer projectID,
            List<ProjectDiaryRecordDTO> list,
            List<ProjectSiteTaskStatusDTO> sList) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("ProjectSiteStaff.findByProject", ProjectSiteStaff.class);
            q.setParameter("projectID", projectID);
            List<ProjectSiteStaff> pstList = q.getResultList();

            for (ProjectSiteStaff pss : pstList) {
                ProjectSiteStaffDTO dto = new ProjectSiteStaffDTO(pss);
                for (ProjectDiaryRecordDTO diary : list) {
                    if (Objects.equals(diary.getProjectSiteStaff().getProjectSiteStaffID(), dto.getProjectSiteStaffID())) {
                        dto.getProjectDiaryRecordList().add(diary);
                    }
                }
                for (ProjectSiteTaskStatusDTO s : sList) {
                    if (Objects.equals(s.getProjectSiteStaffID(), dto.getProjectSiteStaffID())) {
                        dto.getProjectSiteTaskStatusList().add(s);
                    }
                }
                resp.getProjectSiteStaffList().add(dto);
            }
            log.log(Level.INFO, "Project staff found: {0}", pstList.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get tasks\n" + getErrorString(e));
        }
        return resp;
    }

    public ResponseDTO getTasksByProject(Integer projectID) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("ProjectSiteTask.findByProject", ProjectSiteTask.class);
            q.setParameter("projectID", projectID);
            List<ProjectSiteTask> pstList = q.getResultList();
            log.log(Level.INFO, "tasks found: {0}", pstList.size());
            q = em.createNamedQuery("ProjectSiteTaskStatus.findByProject",
                    ProjectSiteTaskStatus.class);
            List<ProjectSiteTaskStatus> xList = q.getResultList();
            log.log(Level.INFO, "task status found: {0}", xList.size());

            for (ProjectSiteTask projectSiteTask : pstList) {
                ProjectSiteTaskDTO task = new ProjectSiteTaskDTO(projectSiteTask);
                for (ProjectSiteTaskStatus s : xList) {
                    if (Objects.equals(s.getProjectSiteTask().getProjectSiteTaskID(), projectSiteTask.getProjectSiteTaskID())) {
                        task.getProjectSiteTaskStatusList().add(new ProjectSiteTaskStatusDTO(s));
                    }
                }
                resp.getProjectSiteTaskList().add(task);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get tasks\n" + getErrorString(e));
        }
        return resp;
    }

    public ResponseDTO getCompanyData(Integer companyID) throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {

        } catch (Exception e) {

            throw new DataException("Failed");
        }

        return resp;
    }

    public ResponseDTO getSiteData(Integer projectSiteID) throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {

        } catch (Exception e) {

            throw new DataException("Failed");
        }

        return resp;
    }

    public ResponseDTO getTaskStatusList() throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {

        } catch (Exception e) {

            throw new DataException("Failed");
        }

        return resp;
    }

    public String getErrorString(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (e.getMessage() != null) {
            sb.append(e.getMessage()).append("\n\n");
        }
        if (e.toString() != null) {
            sb.append(e.toString()).append("\n\n");
        }
        StackTraceElement[] s = e.getStackTrace();
        if (s.length > 0) {
            StackTraceElement ss = s[0];
            String method = ss.getMethodName();
            String cls = ss.getClassName();
            int line = ss.getLineNumber();
            sb.append("Class: ").append(cls).append("\n");
            sb.append("Method: ").append(method).append("\n");
            sb.append("Line Number: ").append(line).append("\n");
        }

        return sb.toString();
    }
    static final Logger log = Logger.getLogger(ListUtil.class.getSimpleName());
}
