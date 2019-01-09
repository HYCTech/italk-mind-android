package com.lw.italk.entity;

/**
 * Created by cts on 2017/7/7.
 */
public class AccountInfo {
    public static AccountInfo EMPTY = new AccountInfo();
    int employee_id;
    String employee_name;
    String shop_id;
    String shop_name;
    String shop_supply_model;
    String customer_name;
    String private_key;
    String user_name;
    String token;
    int is_direct_supply;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_supply_model() {
        return shop_supply_model;
    }

    public void setShop_supply_model(String shop_supply_model) {
        this.shop_supply_model = shop_supply_model;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getIs_direct_supply() {
        return is_direct_supply;
    }

    public void setIs_direct_supply(int is_direct_supply) {
        this.is_direct_supply = is_direct_supply;
    }

    public boolean isDirectSupply() {
        return is_direct_supply == 1;
    }

    public String getFmtName() {
        String fmt = user_name;
        try {
            fmt = fmt.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fmt;
    }

}
