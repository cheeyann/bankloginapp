package com.example.xinbank;

public class imeiexist {
    private String imei, _id, ic;

    public imeiexist() {
    }

    public imeiexist(String imei, String _id, String ic) {
        this.imei = imei;
        this._id = _id;
        this.ic = ic;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }
}
