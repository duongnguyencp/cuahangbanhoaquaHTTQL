/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assistant;

import control.HoaDonBanHangDAO;
import control.MatHangDAO;
import java.util.ArrayList;
import java.util.HashMap;
import model.MatHang;
import model.*;
/**
 *
 * @author tuan2
 */


public class AssistKmean {
    private HoaDonBanHangDAO hoaDonDAO = new HoaDonBanHangDAO();
    private MatHangDAO matHangDAO = new MatHangDAO();
    
    

    public ArrayList<Pair> getN_C_2(int n,ArrayList<MatHang> listMH){
        ArrayList<Pair> res = new ArrayList<>();
        ArrayList<Integer> arr = new ArrayList<>();
        for(int i  = 0 ; i < n ; i ++ )
            arr.add(i);
        int stop = 0;
        while(stop == 0 ){
            Pair point = new Pair( listMH.get( arr.get(0) ).getIdMatHang() , listMH.get( arr.get(1) ).getIdMatHang() );
            res.add(point);
            int i = 2;
            while(i > -1  && arr.get(i) == n - 2 + i ) i--;
            if(i > -1){
               
            }
            else stop = 1;
        }
        return res; 
    }
    
    public  HashMap<Pair,Double> getDisimiler(){
        HashMap<Pair,Double> probabilityMH = new HashMap();
        ArrayList<MatHang> listMH = matHangDAO.getAllMatHang();
        ArrayList<HoaDonBanHang> listHoaDonBanHang;
        listHoaDonBanHang = HoaDonBanHangDAO.getHoaDonBanHangs();
    }
    
}
