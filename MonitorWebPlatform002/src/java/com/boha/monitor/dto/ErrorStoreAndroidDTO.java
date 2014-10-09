/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.dto;

import com.boha.monitor.data.ErrorStoreAndroid;
import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class ErrorStoreAndroidDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int errorStoreAndroidID, companyID;
    private String companyName, logCat, stackTrace, androidVersion,
            brand, appVersionCode, appVersionName, packageName, phoneModel;
    private long errorDate;

    public ErrorStoreAndroidDTO(ErrorStoreAndroid a) {
        errorDate = a.getErrorDate().getTime();
        errorStoreAndroidID = a.getErrorStoreAndroidID();
        if (a.getCompany() != null) {
            companyID = a.getCompany().getCompanyID();
            companyName = a.getCompany().getCompanyName();
        }
        logCat = a.getLogCat();
        stackTrace = a.getStackTrace();
        androidVersion = a.getAndroidVersion();
        brand = a.getBrand();
        appVersionCode = a.getAppVersionCode();
        appVersionName = a.getAppVersionName();
        packageName = a.getPackageName();
        phoneModel = a.getPhoneModel();
    }

    public int getErrorStoreAndroidID() {
        return errorStoreAndroidID;
    }

    public void setErrorStoreAndroidID(int errorStoreAndroidID) {
        this.errorStoreAndroidID = errorStoreAndroidID;
    }

    public int getGolfGroupID() {
        return companyID;
    }

    public void setGolfGroupID(int golfGroupID) {
        this.companyID = golfGroupID;
    }

    public String getGolfGroupName() {
        return companyName;
    }

    public void setGolfGroupName(String golfGroupName) {
        this.companyName = golfGroupName;
    }

    public String getLogCat() {
        return logCat;
    }

    public void setLogCat(String logCat) {
        this.logCat = logCat;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public long getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(long errorDate) {
        this.errorDate = errorDate;
    }

}
