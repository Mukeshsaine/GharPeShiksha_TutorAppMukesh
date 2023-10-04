package com.gharpeshiksha.tutorapp.data_model;

public class BillingHistory {
    private String Plan, PaymentDate, paymentAmount, status;


    public BillingHistory(String plan, String paymentDate, String paymentAmount, String status) {
        Plan = plan;
        PaymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.status = status;
    }

    public String getPlan() {
        return Plan;
    }

    public void setPlan(String plan) {
        Plan = plan;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
