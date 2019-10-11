package com.revolut.demo.dto.response;

public class TransactionDetailsResponse {

    String id;
    String acc_from;
    String acc_to;
    String amount;
    String date;
    String status;
    String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcc_from() {
        return acc_from;
    }

    public void setAcc_from(String acc_from) {
        this.acc_from = acc_from;
    }

    public String getAcc_to() {
        return acc_to;
    }

    public void setAcc_to(String acc_to) {
        this.acc_to = acc_to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
