/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.BienLaiKhoDAO;
import control.CongNoDAO;
import control.NhanVienDAO;
import control.PhieuThuChiDAO;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.*;

/**
 *
 * @author Duong
 */
public class GDTaoPhieuThuChi extends javax.swing.JFrame {

    private String maPhieu;
    private NhanVien selectedNV;
    private int soTien;
    private NhaCungCap ncc;
    private ArrayList<NhanVien> listNV = new ArrayList<>();

    public GDTaoPhieuThuChi(NhaCungCap ncc) {
        initComponents();
        Init();
        jComboBoxPhanLoai.setSelectedIndex(0);
        jTextFieldLyDo.setText("Trả tiền mua hàng");
        jTextFieldTen.setText(ncc.getTen());
        jTextFieldTen.setEditable(false);
        this.ncc = ncc;

    }

    public GDTaoPhieuThuChi() {
        initComponents();
        Init();
    }

    void Init() {
        createMat();
        loadNV();
        addListenerText(jTextFieldSoTien);

    }

    void loadNV() {
        jComboBoxNhanVien.removeAllItems();
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        listNV = nhanVienDAO.getAllNVKeToan();
        selectedNV = listNV.get(0);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < listNV.size(); i++) {
            model.addElement(listNV.get(i).getHoTen());
        }
        jComboBoxNhanVien.setModel(model);
        jComboBoxNhanVien.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox comboBoxTest = (JComboBox) e.getSource();
                int stt = comboBoxTest.getSelectedIndex();
                selectedNV = listNV.get(stt);
            }
        });

    }

    int countDigit(int number) {
        int count = 0;
        while (number > 0) {
            number /= 10;
            count += 1;
        }
        return count;
    }

    String dinhDangTien(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = df.format(new BigDecimal(number));
        return s;
    }

    void createMat() {
        PhieuThuChiDAO aO = new PhieuThuChiDAO();
        ArrayList<PhieuThuChi> phieuThuChis = aO.getAllPhieuThuChi();
        int count = countDigit(phieuThuChis.size() + 1);
        this.maPhieu = "PC-";
        int rest = 7 - count;
        while (rest > 0) {
            maPhieu += '0';
            rest--;
        }
        this.maPhieu = this.maPhieu + "" + phieuThuChis.size();
        jTextFieldSoPhieu.setText(this.maPhieu);
    }
//thêm sự kiện ô nhập đơn giá định dạng money

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
                    if (!field.getText().equals("")) {
                        DecimalFormat df = new DecimalFormat("#,##0");
                        soTien = Integer.parseInt(field.getText());
                        String s = df.format(new BigDecimal(field.getText()));
                        field.setText(s);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldSoPhieu = new javax.swing.JTextField();
        jDateChooserNgayLap = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxPhanLoai = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldLyDo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTen = new javax.swing.JTextField();
        jComboBoxLoaiDoiTuong = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxNhanVien = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldSoTien = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldGhiChu = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBoxChuyenKhoan = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_purchase_order_48px.png"))); // NOI18N
        jLabel6.setText("Phiếu chi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel1.setText("Ngày");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setText("Số phiếu");

        jTextFieldSoPhieu.setEditable(false);
        jTextFieldSoPhieu.setBackground(new java.awt.Color(204, 255, 255));
        jTextFieldSoPhieu.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jDateChooserNgayLap.setDateFormatString("dd/MM/yyyy");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel3.setText("Phân loại");

        jComboBoxPhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thanh toán công nợ", "Trả lương nhân viên", "Thanh toán nhập hàng" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel4.setText("Lý do chi");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel5.setText("Tên đối tượng");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel7.setText("Loại đối tượng");

        jComboBoxLoaiDoiTuong.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBoxLoaiDoiTuong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhà cung cấp", "Nhân viên", "Đối tượng khác" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel8.setText("Chuyển khoản");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel9.setText("Nhân viên");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel10.setText("Số tiền");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel11.setText("Cửa hàng");

        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trụ sở chính", " " }));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel12.setText("Ghi chú");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_print_16px.png"))); // NOI18N
        jButton1.setText("Lưu và In");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButton2.setText("Lưu");

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButton3.setText("Thoát");

        jComboBoxChuyenKhoan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBoxChuyenKhoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản", " " }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, 125, Short.MAX_VALUE)
                        .addComponent(jTextFieldSoTien, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextFieldGhiChu, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldLyDo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldTen)
                            .addComponent(jComboBoxPhanLoai, javax.swing.GroupLayout.Alignment.LEADING, 0, 199, Short.MAX_VALUE)
                            .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxLoaiDoiTuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jButton3))))
                    .addComponent(jComboBoxChuyenKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jDateChooserNgayLap, jTextFieldSoPhieu});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldSoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxPhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldLyDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLoaiDoiTuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBoxChuyenKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldSoTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jComboBox4, jComboBoxChuyenKhoan, jComboBoxLoaiDoiTuong, jComboBoxNhanVien, jComboBoxPhanLoai, jDateChooserNgayLap, jLabel1, jLabel2, jTextFieldGhiChu, jTextFieldLyDo, jTextFieldSoPhieu, jTextFieldSoTien, jTextFieldTen});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private ArrayList<CongNo> listCongNo;
    private String maCongNO;
    public void createMatCN() {
        int count = countDigit(listCongNo.size() + 1);
         this.maCongNO = "CN-";
        int rest = 7 - count;
        while (rest > 0) {
            maCongNO += '0';
            rest--;
        }
        this.maCongNO = this.maCongNO + "" + listCongNo.size();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String ngayLap = "";
        boolean check = true;
        try {
            ngayLap = ((JTextField) jDateChooserNgayLap.getDateEditor().getUiComponent()).getText();
        } catch (Exception e) {
            System.out.println("ngay lap chua duoc chon");
            e.printStackTrace();
        }
        if (ngayLap.equals("")) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chưa chọn ngày lập", "cảnh báo chọn ngày lập", JOptionPane.WARNING_MESSAGE);
        } else if (jTextFieldSoTien.getText().equals("")) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chưa nhập số tiền chi!", "cảnh báo điền số tiền", JOptionPane.WARNING_MESSAGE);
            jTextFieldSoTien.requestFocus();
        }
        if (check) {

            PhieuThuChi phieuThuChi = new PhieuThuChi();
            phieuThuChi.setChuyenKhoan(jComboBoxChuyenKhoan.getSelectedItem().toString());
            phieuThuChi.setLoaiPhieu(jComboBoxPhanLoai.getSelectedItem().toString());
            phieuThuChi.setTenDoiTuong(jTextFieldTen.getText());
            phieuThuChi.setNv(listNV.get(jComboBoxNhanVien.getSelectedIndex()));
            phieuThuChi.setSoTien(soTien);
            phieuThuChi.setLyDo(jTextFieldLyDo.getText());
            phieuThuChi.setDienGiai("");
            phieuThuChi.setSoPhieu(jTextFieldSoPhieu.getText());
            phieuThuChi.setNgayLap(ngayLap);
            CongNo cn = new CongNo();
            CongNoDAO congNoDao = new CongNoDAO();
            cn.setCap(this.ncc);
            cn.setMaSoThue(this.maCongNO);
            cn.setSoTienNo(0);
            congNoDao.traCongNo(cn,phieuThuChi);
            jTextFieldLyDo.setText("");
            jTextFieldSoTien.setText("");
            jTextFieldGhiChu.setText("");
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_ok_48px.png"));
            JOptionPane.showMessageDialog(null, "Đã hoàn tất nhập hàng", "Nhập hàng thành công", JOptionPane.INFORMATION_MESSAGE, icon);
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(GDTaoPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDTaoPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDTaoPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDTaoPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GDTaoPhieuThuChi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBoxChuyenKhoan;
    private javax.swing.JComboBox<String> jComboBoxLoaiDoiTuong;
    private javax.swing.JComboBox<String> jComboBoxNhanVien;
    private javax.swing.JComboBox<String> jComboBoxPhanLoai;
    private com.toedter.calendar.JDateChooser jDateChooserNgayLap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldGhiChu;
    private javax.swing.JTextField jTextFieldLyDo;
    private javax.swing.JTextField jTextFieldSoPhieu;
    private javax.swing.JTextField jTextFieldSoTien;
    private javax.swing.JTextField jTextFieldTen;
    // End of variables declaration//GEN-END:variables
}
