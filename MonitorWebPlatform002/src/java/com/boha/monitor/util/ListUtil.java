/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.util;

import com.boha.monitor.data.Company;
import com.boha.monitor.data.CompanyStaff;
import com.boha.monitor.data.CompanyStaffType;
import com.boha.monitor.data.ErrorStore;
import com.boha.monitor.data.ErrorStoreAndroid;
import com.boha.monitor.data.Project;
import com.boha.monitor.data.ProjectDiaryRecord;
import com.boha.monitor.data.ProjectSite;
import com.boha.monitor.data.ProjectSiteStaff;
import com.boha.monitor.data.ProjectSiteTask;
import com.boha.monitor.data.ProjectSiteTaskStatus;
import com.boha.monitor.data.ProjectStatusType;
import com.boha.monitor.data.TaskStatus;
import com.boha.monitor.dto.CompanyDTO;
import com.boha.monitor.dto.CompanyStaffDTO;
import com.boha.monitor.dto.CompanyStaffTypeDTO;
import com.boha.monitor.dto.ErrorStoreAndroidDTO;
import com.boha.monitor.dto.ErrorStoreDTO;
import com.boha.monitor.dto.ProjectDTO;
import com.boha.monitor.dto.ProjectDiaryRecordDTO;
import com.boha.monitor.dto.ProjectSiteDTO;
import com.boha.monitor.dto.ProjectSiteStaffDTO;
import com.boha.monitor.dto.ProjectSiteTaskDTO;
import com.boha.monitor.dto.ProjectSiteTaskStatusDTO;
import com.boha.monitor.dto.ProjectStatusTypeDTO;
import com.boha.monitor.dto.TaskStatusDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public ResponseDTO getCompanyStaffTypeList() throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("CompanyStaffType.findAll", CompanyStaffType.class);
            List<CompanyStaffType> sList = q.getResultList();
            for (CompanyStaffType cs : sList) {
                resp.getCompanyStaffTypeList().add(new CompanyStaffTypeDTO(cs));
            }
            log.log(Level.OFF, "company staff types found: {0}", sList.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return resp;
    }

    public ResponseDTO getTaskStatusList() throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("TaskStatus.findAll", TaskStatus.class);
            List<TaskStatus> sList = q.getResultList();
            for (TaskStatus cs : sList) {
                resp.getTaskStatusList().add(new TaskStatusDTO(cs));
            }
            log.log(Level.OFF, "task status types found: {0}", sList.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return resp;
    }

    public ResponseDTO getProjectStatusList() throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("ProjectStatusType.findAll", ProjectStatusType.class);
            List<ProjectStatusType> sList = q.getResultList();
            for (ProjectStatusType cs : sList) {
                resp.getProjectStatusTypeList().add(new ProjectStatusTypeDTO(cs));
            }
            log.log(Level.OFF, "project status types found: {0}", sList.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project status list\n" + getErrorString(e));
        }

        return resp;
    }

    public ResponseDTO getAndroidErrorList(int day) throws DataException {
        ResponseDTO resp = new ResponseDTO();

        try {
            Query q = em.createNamedQuery("ErrorStoreAndroid.findByPeriod", ErrorStoreAndroid.class);
            q.setParameter("from", new Date(getSimpleDate(new Date(), day)));
            q.setParameter("to", new Date(getSimpleDate(new Date(), 0)));
            List<ErrorStoreAndroid> sList = q.getResultList();
            for (ErrorStoreAndroid cs : sList) {
                resp.getErrorStoreAndroidList().add(new ErrorStoreAndroidDTO(cs));
            }
            List<ErrorStoreDTO> list = getErrorStoreList(day);
            resp.setErrorStoreList(list);

            log.log(Level.OFF, "Error Store Android found: {0}", sList.size());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project status list\n" + getErrorString(e));
        }

        return resp;
    }

    public List<ErrorStoreDTO> getErrorStoreList(int day) throws DataException {
        List<ErrorStoreDTO> resp = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("ErrorStore.findByPeriod");
            q.setParameter("startDate", new Date(getSimpleDate(new Date(), day)));
            q.setParameter("endDate", new Date(getSimpleDate(new Date(), 0)));
            List<ErrorStore> sList = q.getResultList();
            for (ErrorStore cs : sList) {
                resp.add(new ErrorStoreDTO(cs));
            }
            log.log(Level.OFF, "Error Store Android found: {0}", sList.size());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project status list\n" + getErrorString(e));
        }

        return resp;
    }

    public static long getSimpleDate(Date date, int day) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        if (day > 0) {
            cal.set(GregorianCalendar.DAY_OF_MONTH, cal.get(GregorianCalendar.DAY_OF_MONTH) - day);
        }
        return cal.getTimeInMillis();
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
        CompanyDTO c = new CompanyDTO(em.find(Company.class, companyID));

        c.setCompanyStaffList(getCompanyStaff(companyID).getCompanyStaffList());
        c.setProjectStatusTypeList(getProjectStatusList().getProjectStatusTypeList());
        c.setTaskStatusList(getTaskStatusList().getTaskStatusList());
        c.setProjectList(getProjectsByCompany(companyID));
        resp.setCompany(c);

        return resp;
    }

    private List<ProjectDTO> getProjectsByCompany(Integer companyID) throws DataException {
        List<ProjectDTO> resp = new ArrayList<>();

        try {
            Query q = em.createNamedQuery("Project.findByCompany", Project.class);
            q.setParameter("companyID", companyID);
            List<Project> pList = q.getResultList();
            List<ProjectSiteDTO> psList = getSitesByCompany(companyID);
            for (Project project : pList) {
                ProjectDTO dto = new ProjectDTO(project);
                for (ProjectSiteDTO ps : psList) {
                    if (Objects.equals(ps.getProjectID(), dto.getProjectID())) {
                        dto.getProjectSiteList().add(ps);
                    }
                }
                resp.add(dto);
            }
            log.log(Level.INFO, "company projects found: {0}", resp.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get company projects\n" + getErrorString(e));
        }

        return resp;
    }

    private List<ProjectSiteDTO> getSitesByCompany(Integer companyID) throws DataException {
        List<ProjectSiteDTO> list = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("ProjectSite.findByCompany", ProjectSite.class);
            q.setParameter("companyID", companyID);
            List<ProjectSite> pList = q.getResultList();
            List<ProjectSiteTaskDTO> pstList = getSiteTasksByCompany(companyID);
            List<ProjectSiteStaffDTO> staffList = getSiteStaffByCompany(companyID);
            for (ProjectSite s : pList) {
                ProjectSiteDTO dto = new ProjectSiteDTO(s);
                for (ProjectSiteTaskDTO pst : pstList) {
                    if (Objects.equals(pst.getProjectSiteID(), dto.getProjectSiteID())) {
                        dto.getProjectSiteTaskList().add(pst);
                    }
                }
                for (ProjectSiteStaffDTO st : staffList) {
                    if (Objects.equals(st.getProjectSiteID(), dto.getProjectSiteID())) {
                        for (ProjectSiteTaskDTO pst : pstList) {
                            if (Objects.equals(pst.getProjectSiteID(), st.getProjectSiteID())) {
                                for (ProjectSiteTaskStatusDTO x : pst.getProjectSiteTaskStatusList()) {
                                    if (Objects.equals(x.getProjectSiteStaffID(), st.getProjectSiteStaffID())) {
                                        st.getProjectSiteTaskStatusList().add(x);
                                        log.log(Level.INFO, "status loaded {0} {1}", new Object[]{x.getStaffName(), x.getTaskStatus().getTaskStatusName()});
                                    }
                                }
                            }
                        }

                        dto.getProjectSiteStaffList().add(st);
                    }
                }
                list.add(dto);
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return list;
    }

    private List<ProjectSiteTaskDTO> getSiteTasksByCompany(Integer companyID) throws DataException {
        List<ProjectSiteTaskDTO> list = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("ProjectSiteTask.findByCompany", ProjectSiteTask.class);
            q.setParameter("companyID", companyID);
            List<ProjectSiteTask> pList = q.getResultList();
            List<ProjectSiteTaskStatusDTO> pstList = getTaskStatusByCompany(companyID);
            for (ProjectSiteTask s : pList) {
                ProjectSiteTaskDTO dto = new ProjectSiteTaskDTO(s);
                for (ProjectSiteTaskStatusDTO pst : pstList) {
                    if (Objects.equals(pst.getProjectSiteTaskID(), dto.getProjectSiteTaskID())) {
                        dto.getProjectSiteTaskStatusList().add(pst);
                    }
                }
                list.add(dto);
            }
            log.log(Level.OFF, "#### Company site tasks: {0}", list.size());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get project data\n" + getErrorString(e));
        }

        return list;
    }

    private List<ProjectSiteTaskStatusDTO> getTaskStatusByCompany(Integer companyID) throws DataException {
        List<ProjectSiteTaskStatusDTO> list = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("ProjectSiteTaskStatus.findByCompany", ProjectSiteTaskStatus.class);
            q.setParameter("companyID", companyID);
            List<ProjectSiteTaskStatus> pList = q.getResultList();
            for (ProjectSiteTaskStatus s : pList) {
                list.add(new ProjectSiteTaskStatusDTO(s));
            }

            log.log(Level.OFF, "Company task status: {0}", list.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get company task status data\n" + getErrorString(e));
        }

        return list;
    }

    private List<ProjectSiteStaffDTO> getSiteStaffByCompany(Integer companyID) throws DataException {
        List<ProjectSiteStaffDTO> list = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("ProjectSiteStaff.findByCompany", ProjectSiteStaff.class);
            q.setParameter("companyID", companyID);
            List<ProjectSiteStaff> pList = q.getResultList();
            for (ProjectSiteStaff s : pList) {
                list.add(new ProjectSiteStaffDTO(s));
            }

            log.log(Level.OFF, "Company site staff: {0}", list.size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed to get company task status data\n" + getErrorString(e));
        }

        return list;
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
