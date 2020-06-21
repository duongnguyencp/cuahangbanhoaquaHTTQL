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
public class RecordCongNoNCC {
    private NhaCungCap ncc;
    private int TienConNo;

    public RecordCongNoNCC() {
    }

    public NhaCungCap getNcc() {
        return ncc;
    }

    public void setNcc(NhaCungCap ncc) {
        this.ncc = ncc;
    }

    public int getTienConNo() {
        return TienConNo;
    }

    public void setTienConNo(int TienConNo) {
        this.TienConNo = TienConNo;
    }
    
}
