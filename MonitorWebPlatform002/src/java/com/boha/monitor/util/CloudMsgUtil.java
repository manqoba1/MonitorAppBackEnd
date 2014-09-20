/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.util;

import com.boha.monitor.data.GcmDevice;
import com.boha.monitor.dto.transfer.ResponseDTO;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class CloudMsgUtil {

    @PersistenceContext
    EntityManager em;
    private static final int RETRIES = 5;
    public static final String API_KEY = "AIzaSyCJIUMPXsL-GVAfNAl1i-fDy6qf7g5TtCU";

    public  ResponseDTO sendRequestUpdateToProjectSite(int companyID, PlatformUtil platformUtil) throws
            Exception, DataException {
        ResponseDTO resp = new ResponseDTO();
        //send message to Google servers
        Sender sender = new Sender(API_KEY);
        Message message = new Message.Builder()
                .addData("message", "refresh class activities")
                .addData("dateStamp", "" + new Date().getTime()).build();

        Query q = em.createNamedQuery("GcmDevice.findInstructorDevices", GcmDevice.class);
        q.setParameter("id", companyID);
        List<GcmDevice> gList = q.getResultList();
        List<String> registrationIDs = new ArrayList<>();
        for (GcmDevice m : gList) {
            registrationIDs.add(m.getRegistrationID());
        }
        if (registrationIDs.isEmpty()) {
            LOG.log(Level.SEVERE, "#### No instructor registrationIDs found ");
            resp.setMessage("No instructor found or their devices are not registered");
            resp.setStatusCode(RETRIES);
            platformUtil.addErrorStore(889, "#### No intructor devices found ", "Cloud Message Services");
            return resp;
        }
        boolean OK;
        String rMsg;
        if (registrationIDs.size() == 1) {
            Result result = sender.send(message, registrationIDs.get(0), RETRIES);
            OK = handleResult(result, platformUtil);
        } else {
            MulticastResult multiCastResult = sender.send(
                    message, registrationIDs, RETRIES);
            OK = handleMultiCastResult(multiCastResult, platformUtil);
        }
        if (OK) {
            rMsg = "Google GCM - message has been sent to Google servers";
        } else {
            rMsg = "Google GCM - message has not been sent. Error occured";
            resp.setStatusCode(117);
            resp.setMessage(rMsg);
            platformUtil.addErrorStore(889, "Google GCM - message has not been sent. Error occured", "Cloud Message Services");
        }
        resp.setMessage(rMsg);
        return resp;
    }
    

    private  List<GcmDevice> getDevices(int companyID) {
        Query q = em.createNamedQuery("GcmDevice.findInstructorDevices");
        q.setParameter("id", companyID);
        return q.getResultList();
    }

   
    public static final int GCM_MESSAGE_ERROR = 3, ALL_OK = 0;

    private  int sendMessage(String json, List<String> registrationIDs, PlatformUtil platformUtil) throws IOException, Exception {
        Sender sender = new Sender(API_KEY);
        Message message = new Message.Builder()
                .addData("message", json)
                .addData("dateStamp", "" + new Date().getTime()).build();

        boolean OK;
        if (registrationIDs.size() == 1) {
            Result result = sender.send(message, registrationIDs.get(0), RETRIES);
            OK = handleResult(result, platformUtil);
        } else {
            MulticastResult multiCastResult = sender.send(
                    message, registrationIDs, RETRIES);
            OK = handleMultiCastResult(multiCastResult, platformUtil);
        }
        if (!OK) {
            platformUtil.addErrorStore(889, "Google GCM - message has not been sent. Error occured", "Cloud Message Services");
            return GCM_MESSAGE_ERROR;
        }
        return ALL_OK;
    }

    private  boolean handleResult(Result result, PlatformUtil platformUtil)
            throws Exception {

        LOG.log(Level.INFO, "Handle result from Google GCM servers: {0}", result.toString());
        if (result.getErrorCodeName() != null) {
            if (result.getErrorCodeName().equals(
                    Constants.ERROR_NOT_REGISTERED)) {
                // TODO remove the registration from the database
                LOG.log(Level.SEVERE, "#### GCM device not registered");
                platformUtil.addErrorStore(889, "#### GCM device not registered", "Cloud Message Services");
                return false;
            }
            if (result.getErrorCodeName().equals(
                    Constants.ERROR_UNAVAILABLE)) {
                LOG.log(Level.SEVERE, "#### GCM servers not available");
                platformUtil.addErrorStore(889, "#### GCM servers not available", "Cloud Message Services");
                return false;
            }
            LOG.log(Level.SEVERE, "#### GCM message send error : {0}",
                    result.getErrorCodeName());
            platformUtil.addErrorStore(889, "#### GCM message send error\nErrorCodeName: " + result.getErrorCodeName(), "Cloud Message Services");
            return false;
        }

        if (result.getMessageId() != null) {
            LOG.log(Level.INFO, "Result messageID from GCM: {0}", result.getMessageId());
            if (result.getCanonicalRegistrationId() != null) {
                LOG.log(Level.INFO,
                        "### Google GCM - canonical registration id found, updating db ...");
                //TODO update device registration id
                //EntityManager em = EMUtil.getEntityManager();

            }
        }
        return true;
    }

    private  boolean handleMultiCastResult(MulticastResult multiCastResult, PlatformUtil platformUtil)
            throws Exception {
        LOG.log(Level.INFO, "Handle result from Google GCM servers: {0}", multiCastResult.toString());
        if (multiCastResult.getFailure() == 0
                && multiCastResult.getCanonicalIds() == 0) {
            LOG.log(Level.INFO, "### Google Cloud message send is OK, messages: {0}", multiCastResult.getTotal());
            return true;
        }
        LOG.log(Level.INFO,
                "### Google GCM - iterating through multicast Result for errors...");
        for (Result result : multiCastResult.getResults()) {
            if (result.getErrorCodeName() != null) {
                if (result.getErrorCodeName().equals(
                        Constants.ERROR_NOT_REGISTERED)) {
                    // TODO remove the registration from the database
                    LOG.log(Level.SEVERE, "#### GCM device not registered");
                    platformUtil.addErrorStore(889, "#### GCM device not registered", "Cloud Message Services");
                    return false;
                }
                if (result.getErrorCodeName().equals(
                        Constants.ERROR_UNAVAILABLE)) {
                    // TODO resubmit this message after back-off
                    LOG.log(Level.SEVERE, "#### GCM servers not available");
                    platformUtil.addErrorStore(889, "#### GCM servers not available", "Cloud Message Services");
                    return false;
                }
                LOG.log(Level.SEVERE, "#### GCM message send error : {0}",
                        result.getErrorCodeName());
                platformUtil.addErrorStore(889, "#### GCM message send error\nErrorCodeName: " + result.getErrorCodeName(), "Cloud Message Services");
                return false;
            }

            if (result.getMessageId() != null) {
                LOG.log(Level.INFO, "Result messageID from GCM: {0}", result.getMessageId());
                if (result.getCanonicalRegistrationId() != null) {
                    LOG.log(Level.INFO,
                            "### Google GCM - canonical registration id found, updating db ...");
                    //update device registration id - query by gcmdevice by reg id ???????????

                }
            }
        }
        return true;
    }
    static final Logger LOG = Logger.getLogger("CloudMsgUtil");
}
