/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duong
 */
package model;
public class RecordChiTietCongNo {
    private PhieuThuChi phieuThuChi;
    private CongNo cn;

    public RecordChiTietCongNo() {
    }

    public PhieuThuChi getPhieuThuChi() {
        return phieuThuChi;
    }

    public void setPhieuThuChi(PhieuThuChi phieuThuChi) {
        this.phieuThuChi = phieuThuChi;
    }

    public CongNo getCn() {
        return cn;
    }

    public void setCn(CongNo cn) {
        this.cn = cn;
    }
    
}
