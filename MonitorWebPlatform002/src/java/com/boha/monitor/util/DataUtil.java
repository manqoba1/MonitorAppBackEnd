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
import com.boha.monitor.data.Executive;
import com.boha.monitor.data.GcmDevice;
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
import com.boha.monitor.dto.ErrorStoreDTO;
import com.boha.monitor.dto.GcmDeviceDTO;
import com.boha.monitor.dto.ProjectDTO;
import com.boha.monitor.dto.ProjectDiaryRecordDTO;
import com.boha.monitor.dto.ProjectSiteDTO;
import com.boha.monitor.dto.ProjectSiteStaffDTO;
import com.boha.monitor.dto.ProjectSiteTaskDTO;
import com.boha.monitor.dto.ProjectSiteTaskStatusDTO;
import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;

/**
 *
 * @author aubreyM
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DataUtil {

    @PersistenceContext
    EntityManager em;
    public ResponseDTO login(GcmDeviceDTO device, String email,
            String pin, Integer loginType, ListUtil listUtil) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        Query q = null;
        try {
            switch (loginType) {
                case RequestDTO.LOGIN_OFFICE_ADMIN:
                    q = em.createNamedQuery("CompanyStaff.login", CompanyStaff.class);
                    q.setParameter("email", email);
                    q.setParameter("pin", pin);
                    q.setMaxResults(1);
                    CompanyStaff cs = (CompanyStaff) q.getSingleResult();
                    resp.getCompanyStaffList().add(new CompanyStaffDTO(cs));
                    resp.setProjectList(listUtil.getCompanyData(cs.getCompany()
                            .getCompanyID()).getProjectList());
                    device.setCompanyID(cs.getCompany().getCompanyID());
                    device.setCompanyStaffID(cs.getCompanyStaffID());

                    addDevice(device);
                    break;
                case RequestDTO.LOGIN_PROJECT_STAFF:
                    q = em.createNamedQuery("ProjectSiteStaff.login", ProjectSiteStaff.class);
                    q.setParameter("email", email);
                    q.setParameter("pin", pin);
                    q.setMaxResults(1);
                    ProjectSiteStaff pss = (ProjectSiteStaff) q.getSingleResult();
                    resp.getProjectSiteStaffList().add(new ProjectSiteStaffDTO(pss));
                    resp.getCompanyStaffList().add(new CompanyStaffDTO(pss.getCompanyStaff()));
                    resp.setTaskStatusList(listUtil.getTaskStatusList().getTaskStatusList());
                    resp.setProjectStatusTypeList(listUtil.getProjectStatusList().getProjectStatusTypeList());

                    device.setCompanyID(pss.getCompanyStaff().getCompany().getCompanyID());
                    device.setCompanyStaffID(pss.getCompanyStaff().getCompanyStaffID());

                    addDevice(device);
                    break;
                case RequestDTO.LOGIN_EXECUTIVE:
                    q = em.createNamedQuery("Executive.login", Executive.class);
                    q.setParameter("email", email);
                    q.setParameter("pin", pin);
                    q.setMaxResults(1);
                    Executive ex = (Executive) q.getSingleResult();
                    break;
            }
            resp.setCompanyStaffTypeList(listUtil.getCompanyStaffTypeList()
                    .getCompanyStaffTypeList());
            resp.setTaskStatusList(listUtil
                    .getTaskStatusList().getTaskStatusList());

        } catch (NoResultException e) {
            resp.setStatusCode(301);
            resp.setMessage("Email address or PIN are invalid. Please try again.");
        }
        return resp;
    }

    public void addAndroidError(ErrorStoreAndroid err) throws DataException {
        try {
            em.persist(err);
            log.log(Level.INFO, "Android error added");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to add Android Error", e);
            throw new DataException("Failed to add Android Error\n"
                    + getErrorString(e));
        }
    }

    public ResponseDTO getServerErrors(
            long startDate, long endDate) throws DataException {
        ResponseDTO r = new ResponseDTO();
        if (startDate == 0) {
            DateTime ed = new DateTime();
            DateTime sd = ed.minusMonths(3);
            startDate = sd.getMillis();
            endDate = ed.getMillis();
        }
        try {
            Query q = em.createNamedQuery("ErrorStore.findByPeriod", ErrorStore.class);
            q.setParameter("startDate", new Date(startDate));
            q.setParameter("endDate", new Date(endDate));
            List<ErrorStore> list = q.getResultList();
            List<ErrorStoreDTO> dList = new ArrayList();
            for (ErrorStore e : list) {
                dList.add(new ErrorStoreDTO(e));
            }
            r.setErrorStoreList(dList);
            log.log(Level.OFF, "Errors found {0}", r.getErrorStoreList().size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to getServerErrors");
            throw new DataException("Failed to getServerErrors\n"
                    + getErrorString(e));
        }
        return r;
    }

    public Company getCompanyByID(Integer id) {
        return em.find(Company.class, id);
    }

    public void addDevice(GcmDeviceDTO d) throws DataException {
        try {
            GcmDevice g = new GcmDevice();
            g.setCompany(em.find(Company.class, d.getCompanyID()));
            g.setCompanyStaff(em.find(CompanyStaff.class, d.getCompanyStaffID()));
            if (d.getProjectSiteID() != null
                    && d.getProjectSiteID() > 0) {
                g.setProjectSite(em.find(ProjectSite.class, d.getProjectSiteID()));
            }
            g.setDateRegistered(new Date());
            g.setManufacturer(d.getManufacturer());
            g.setMessageCount(0);
            g.setModel(d.getModel());
            g.setRegistrationID(d.getRegistrationID());
            g.setSerialNumber(d.getSerialNumber());
            g.setProduct(d.getProduct());

            em.persist(g);
            log.log(Level.WARNING, "New device loaded");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");

        }
    }

    public ResponseDTO addProjectSiteTaskStatus(
            ProjectSiteTaskStatusDTO status) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            ProjectSiteTask c = em.find(ProjectSiteTask.class,
                    status.getProjectSiteTaskID());
            ProjectSiteTaskStatus t = new ProjectSiteTaskStatus();
            t.setDateUpdated(new Date());
            t.setProjectSiteTask(c);
            t.setProjectSiteStaff(em.find(ProjectSiteStaff.class, status.getProjectSiteStaffID()));
            t.setTaskStatus(em.find(TaskStatus.class, status.getTaskStatus().getTaskStatusID()));

            em.persist(t);
            Query q = em.createNamedQuery("ProjectSiteTaskStatus.findbyTask",
                    ProjectSiteTaskStatus.class);
            q.setParameter("id", c.getProjectSiteTaskID());
            List<ProjectSiteTaskStatus> list = q.getResultList();
            for (ProjectSiteTaskStatus s : list) {
                resp.getProjectSiteTaskStatusList().add(new ProjectSiteTaskStatusDTO(s));
            }

            log.log(Level.OFF, "ProjectSiteTaskStatus added");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO addProjectDiaryRecord(
            ProjectDiaryRecordDTO diary) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            ProjectSiteStaff c = em.find(ProjectSiteStaff.class,
                    diary.getProjectSiteStaff().getProjectSiteStaffID());
            ProjectDiaryRecord t = new ProjectDiaryRecord();
            t.setDiaryDate(new Date());
            t.setProjectSiteStaff(c);
            t.setProjectStatusType(em.find(ProjectStatusType.class,
                    diary.getProjectStatusType().getProjectStatusTypeID()));

            em.persist(t);
            Query q = em.createNamedQuery("ProjectDiaryRecord.findByProjectSite",
                    ProjectDiaryRecord.class);
            q.setParameter("projectSiteID", c.getProjectSite().getProjectSiteID());
            List<ProjectDiaryRecord> list = q.getResultList();
            for (ProjectDiaryRecord projectDiaryRecord : list) {
                resp.getProjectDiaryRecordList().add(new ProjectDiaryRecordDTO(projectDiaryRecord));
            }

            log.log(Level.OFF, "ProjectDiaryRecord added");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO addProjectSiteTask(
            ProjectSiteTaskDTO task) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            ProjectSite c = em.find(ProjectSite.class, task.getProjectSiteID());
            ProjectSiteTask t = new ProjectSiteTask();
            t.setDateRegistered(new Date());
            t.setProjectSite(c);
            t.setTaskName(task.getTaskName());
            t.setTaskDescription(task.getTaskDescription());

            em.persist(t);
            Query q = em.createNamedQuery("ProjectSiteTask.findByTaskName",
                    ProjectSiteTask.class);
            q.setParameter("name", task.getTaskName());
            q.setParameter("projectSiteID", task.getProjectSiteID());
            q.setMaxResults(1);
            t = (ProjectSiteTask) q.getSingleResult();
            resp.getProjectSiteTaskList().add(new ProjectSiteTaskDTO(t));

            log.log(Level.OFF, "Project site staff registered for: {0} ",
                    new Object[]{c.getProjectSiteName()});

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO registerProjectSiteStaff(
            ProjectSiteStaffDTO staff) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            ProjectSite c = em.find(ProjectSite.class, staff.getProjectSiteID());
            ProjectSiteStaff ps = new ProjectSiteStaff();
            ps.setCompanyStaff(em.find(CompanyStaff.class, staff.getCompanyStaff().getCompanyStaffID()));
            ps.setDateRegistered(new Date());
            ps.setProjectSite(c);
            ps.setPin(getRandomPin());

            em.persist(ps);
            Query q = em.createNamedQuery("ProjectSiteStaff.findBySiteAndStaff",
                    ProjectSiteStaff.class);
            q.setParameter("companyStaffID", staff.getCompanyStaff().getCompanyStaffID());
            q.setParameter("projectSiteID", staff.getProjectSiteID());
            q.setMaxResults(1);
            ps = (ProjectSiteStaff) q.getSingleResult();
            resp.getProjectSiteStaffList().add(new ProjectSiteStaffDTO(ps));
            try {
                if (staff.getGcmDevice() != null) {
                    addDevice(staff.getGcmDevice());
                }

            } catch (DataException e) {
                log.log(Level.WARNING, "Unable to add device to GCMDevice table", e);
            }

            log.log(Level.OFF, "Project site staff registered for: {0} ",
                    new Object[]{c.getProjectSiteName()});

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO registerProjectSite(
            ProjectSiteDTO site) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Project c = em.find(Project.class, site.getProjectID());
            ProjectSite ps = new ProjectSite();
            ps.setProject(c);
            ps.setActiveFlag(0);
            ps.setLatitude(site.getLatitude());
            ps.setLongitude(site.getLongitude());
            ps.setProjectSiteName(site.getProjectSiteName());

            em.persist(ps);

            Query q = em.createNamedQuery("ProjectSite.findByProjectAndSiteName", ProjectSite.class);
            q.setParameter("name", site.getProjectSiteName());
            q.setParameter("projectID", c.getProjectID());
            q.setMaxResults(1);
            ps = (ProjectSite) q.getSingleResult();
            resp.getProjectSiteList().add(new ProjectSiteDTO(ps));

            log.log(Level.OFF, "Project site registered for: {0} - {1} ",
                    new Object[]{c.getProjectName(), site.getProjectSiteName()});

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO registerProject(
            ProjectDTO proj) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Company c = em.find(Company.class, proj.getCompanyID());
            Project project = new Project();
            project.setCompany(c);
            project.setProjectName(proj.getProjectName());
            project.setDescription(proj.getDescription());
            project.setDateRegistered(new Date());
            em.persist(project);
            Query q = em.createNamedQuery("Project.findByCompanyProjectName", Project.class);
            q.setParameter("projectName", proj.getProjectName());
            q.setParameter("companyID", c.getCompanyID());
            q.setMaxResults(1);
            project = (Project) q.getSingleResult();
            resp.getProjectList().add(new ProjectDTO(project));
            log.log(Level.OFF, "Project registered for: {0} - {1} ",
                    new Object[]{c.getCompanyName(), proj.getProjectName()});

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO registerCompanyStaff(
            CompanyStaffDTO staff) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Company c = em.find(Company.class, staff.getCompanyID());
            CompanyStaff cs = new CompanyStaff();
            cs.setCompany(c);
            cs.setFirstName(staff.getFirstName());
            cs.setCellphone(staff.getCellphone());
            cs.setEmail(staff.getEmail());
            cs.setLastName(staff.getLastName());
            cs.setPin(getRandomPin());
            cs.setCompanyStaffType(em.find(CompanyStaffType.class,
                    staff.getCompanyStaffType().getCompanyStaffTypeID()));
            em.persist(cs);

            Query q = em.createNamedQuery("CompanyStaff.findByEmail", CompanyStaff.class);
            q.setParameter("email", cs.getEmail());
            q.setMaxResults(1);
            cs = (CompanyStaff) q.getSingleResult();
            resp.getCompanyStaffList().add(new CompanyStaffDTO(cs));

            try {
                if (staff.getGcmDevice() != null) {
                    addDevice(staff.getGcmDevice());
                }

            } catch (DataException e) {
                log.log(Level.WARNING, "Unable to add device to GCMDevice table", e);
            }

            log.log(Level.OFF, "Company staff registered for: {0} - {1} {2}",
                    new Object[]{c.getCompanyName(), staff.getFirstName(), staff.getLastName()});

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException("Failed");
        }

        return resp;

    }

    public ResponseDTO registerCompany(CompanyDTO company,
            CompanyStaffDTO staff) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        try {
            Company c = new Company();
            c.setCompanyName(company.getCompanyName());
            em.persist(c);
            Query q = em.createNamedQuery("Company.findByCompanyName", Company.class);
            q.setParameter("companyName", company.getCompanyName());
            q.setMaxResults(1);
            Company x = (Company) q.getSingleResult();
            CompanyStaff cs = new CompanyStaff();
            cs.setCompany(x);
            cs.setFirstName(staff.getFirstName());
            cs.setCellphone(staff.getCellphone());
            cs.setEmail(staff.getEmail());
            cs.setLastName(staff.getLastName());
            cs.setPin(staff.getPin());
            cs.setCompanyStaffType(em.find(CompanyStaffType.class, 1));
            em.persist(cs);

            q = em.createNamedQuery("CompanyStaff.findByEmail", CompanyStaff.class);
            q.setParameter("email", cs.getEmail());
            q.setMaxResults(1);
            cs = (CompanyStaff) q.getSingleResult();
            resp.getCompanyStaffList().add(new CompanyStaffDTO(cs));
            resp.setCompany(new CompanyDTO(x));
            log.log(Level.OFF, "Company registered: {0}", x.getCompanyName());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
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

    private String getRandomPin() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random(System.currentTimeMillis());
        int x = rand.nextInt(9);
        if (x == 0) {
            x = 3;
        }
        sb.append(x);
        sb.append(rand.nextInt(9));
        sb.append(rand.nextInt(9));
        sb.append(rand.nextInt(9));
        sb.append(rand.nextInt(9));
        sb.append(rand.nextInt(9));
        return sb.toString();
    }
    static final Logger log = Logger.getLogger(DataUtil.class.getSimpleName());
}
