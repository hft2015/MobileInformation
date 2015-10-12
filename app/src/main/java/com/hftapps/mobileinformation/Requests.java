package com.hftapps.mobileinformation;


public class Requests {

    int _id;
    String _mobile,_op_name,_cr_name,_sim_type,_logo;

    public Requests(int _id,String _mobile, String _op_name, String _cr_name, String _sim_type, String _logo) {
        this._mobile = _mobile;
        this._op_name = _op_name;
        this._id = _id;
        this._sim_type = _sim_type;
        this._logo = _logo;
        this._cr_name = _cr_name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_mobile() {
        return _mobile;
    }

    public void set_mobile(String _mobile) {
        this._mobile = _mobile;
    }

    public String get_op_name() {
        return _op_name;
    }

    public void set_op_name(String _op_name) {
        this._op_name = _op_name;
    }

    public String get_logo() {
        return _logo;
    }

    public void set_logo(String _logo) {
        this._logo = _logo;
    }

    public String get_sim_type() {
        return _sim_type;
    }

    public void set_sim_type(String _sim_type) {
        this._sim_type = _sim_type;
    }

    public String get_cr_name() {
        return _cr_name;
    }

    public void set_cr_name(String _cr_name) {
        this._cr_name = _cr_name;
    }
}
