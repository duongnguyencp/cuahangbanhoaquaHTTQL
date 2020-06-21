/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.*;
import model.*;
import control.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Duong
 */
public class GDXacNhanBanHang extends javax.swing.JFrame {

    /**
     * Creates new form GDXacNhanBanHang
     */
    private ArrayList<HoaDonBanHang> listHDBH;
    private PhieuThuChi ptc;
    private int tienHang;

    public GDXacNhanBanHang(int tienHang, ArrayList<HoaDonBanHang> hdbh, PhieuThuChi phieuThu) {
        initComponents();
        this.ptc = phieuThu;
        this.listHDBH = hdbh;
        this.tienHang = tienHang;
        jTextFieldTongTien.setText("" + tienHang);
        addListenerText(jTextFieldKhachDua);

    }

    String dinhDangTien(long number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = df.format(new BigDecimal(number));
        return s;
    }

    void addListenerText(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {

            }
        });
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (field.equals(jTextFieldKhachDua)) {
                        int number = Integer.parseInt(jTextFieldKhachDua.getText());
                        jTextFieldKhachDua.setText(dinhDangTien(number));
                        int number2 = tienHang - number;
                        jTextFieldTraKhach.setText(dinhDangTien(number2));
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(field, "Only numbers are allowed", "Warning", JOptionPane.WARNING_MESSAGE);
                    e2.printStackTrace();
                    field.setText("");
                }
            }
        });

        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTongTien = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldKhachDua = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTraKhach = new javax.swing.JTextField();
        jComboBoxChuyenKhoan = new javax.swing.JComboBox<>();
        jButtonDongBillVaIn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_card_payment_48px.png"))); // NOI18N
        jLabel1.setText("Thanh toán");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tổng tiền");

        jTextFieldTongTien.setEditable(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Khách đưa");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Trả lại");

        jComboBoxChuyenKhoan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBoxChuyenKhoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản", "Quẹt thẻ" }));

        jButtonDongBillVaIn.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonDongBillVaIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_print_16px.png"))); // NOI18N
        jButtonDongBillVaIn.setText("Đóng bill và in");
        jButtonDongBillVaIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDongBillVaInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldTongTien)
                            .addComponent(jTextFieldKhachDua)
                            .addComponent(jTextFieldTraKhach, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButtonDongBillVaIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBoxChuyenKhoan, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(173, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTraKhach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jComboBoxChuyenKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonDongBillVaIn)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDongBillVaInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDongBillVaInActionPerformed
        String chuyenKhoan = jComboBoxChuyenKhoan.getSelectedItem().toString();
        ptc.setChuyenKhoan(chuyenKhoan);
        ptc.setSoTien(tienHang);
        PhieuThuChiDAO phieuThuChiDAO=new PhieuThuChiDAO();
        phieuThuChiDAO.themPhieuThuChi(ptc);
        HoaDonBanHangDAO hoaDonBanHangDAO = new HoaDonBanHangDAO();
        for (int i = 0; i < listHDBH.size(); i++) {
            hoaDonBanHangDAO.themHoaDonBanHang(listHDBH.get(i));
        }
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_ok_48px.png"));
        JOptionPane.showMessageDialog(null, "Đã thanh toán thành công cho khách hàng", "thanh toán thành công", JOptionPane.INFORMATION_MESSAGE, icon);

    }//GEN-LAST:event_jButtonDongBillVaInActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GDXacNhanBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDXacNhanBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDXacNhanBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDXacNhanBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDongBillVaIn;
    private javax.swing.JComboBox<String> jComboBoxChuyenKhoan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldKhachDua;
    private javax.swing.JTextField jTextFieldTongTien;
    private javax.swing.JTextField jTextFieldTraKhach;
    // End of variables declaration//GEN-END:variables
}
