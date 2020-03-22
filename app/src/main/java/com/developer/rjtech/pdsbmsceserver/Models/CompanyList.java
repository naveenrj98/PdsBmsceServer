package com.developer.rjtech.pdsbmsceserver.Models;

public class CompanyList {



    private String Name;
    private String Image;
    private String duration;
    private String noOfDays;
    private String performanceBC;
    private String stipend;
    private String ccID;
    private String CGPA;
    private String EligibleDepartment;
    private String companyName;
    private String dateOfVisit;
    private String jobDescription;
    private String jobLocation;
    private String noOfPeopleSelected;

    public CompanyList() {
    }

    public CompanyList(String name, String image, String duration, String noOfDays, String performanceBC, String stipend, String ccID, String CGPA, String eligibleDepartment, String companyName, String dateOfVisit, String jobDescription, String jobLocation, String noOfPeopleSelected) {
        Name = name;
        Image = image;
        this.duration = duration;
        this.noOfDays = noOfDays;
        this.performanceBC = performanceBC;
        this.stipend = stipend;
        this.ccID = ccID;
        this.CGPA = CGPA;
        EligibleDepartment = eligibleDepartment;
        this.companyName = companyName;
        this.dateOfVisit = dateOfVisit;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.noOfPeopleSelected = noOfPeopleSelected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getPerformanceBC() {
        return performanceBC;
    }

    public void setPerformanceBC(String performanceBC) {
        this.performanceBC = performanceBC;
    }

    public String getStipend() {
        return stipend;
    }

    public void setStipend(String stipend) {
        this.stipend = stipend;
    }

    public String getCcID() {
        return ccID;
    }

    public void setCcID(String ccID) {
        this.ccID = ccID;
    }

    public String getCGPA() {
        return CGPA;
    }

    public void setCGPA(String CGPA) {
        this.CGPA = CGPA;
    }

    public String getEligibleDepartment() {
        return EligibleDepartment;
    }

    public void setEligibleDepartment(String eligibleDepartment) {
        EligibleDepartment = eligibleDepartment;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getNoOfPeopleSelected() {
        return noOfPeopleSelected;
    }

    public void setNoOfPeopleSelected(String noOfPeopleSelected) {
        this.noOfPeopleSelected = noOfPeopleSelected;
    }

    @Override
    public String toString() {
        return "CompanyList{" +
                "Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", duration='" + duration + '\'' +
                ", noOfDays='" + noOfDays + '\'' +
                ", performanceBC='" + performanceBC + '\'' +
                ", stipend='" + stipend + '\'' +
                ", ccID='" + ccID + '\'' +
                ", CGPA='" + CGPA + '\'' +
                ", EligibleDepartment='" + EligibleDepartment + '\'' +
                ", companyName='" + companyName + '\'' +
                ", dateOfVisit='" + dateOfVisit + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", noOfPeopleSelected='" + noOfPeopleSelected + '\'' +
                '}';
    }
}