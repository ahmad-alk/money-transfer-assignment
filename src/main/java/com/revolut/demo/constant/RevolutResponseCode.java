package com.revolut.demo.constant;

/**
 * In exceptional circumstances it is useful to provide a code and meaningful response to clients
 */
public enum RevolutResponseCode {
    BR001("Unauthorized User To The Account"),
    BR002("Illegal transaction to same account"),
    BR003("Sender account is not active"),
    BR004("Receiver account is not active"),
    BR005("Funds Insufficient"),
    ;

    private String msg;

    RevolutResponseCode(String msg) {
        this.msg = msg;
    }

    public String getValue() {
        return msg;
    }

    @Override
    public String toString() {
        return "RevolutResponseCode{" + "code='" + this.name() + '\'' + "msg='" + msg + '\'' + '}';
    }
}
