/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.CongNo;
import model.*;
import control.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Duong
 */
public class GDCongNoFrm extends javax.swing.JFrame {

    private ArrayList<RecordCongNoNCC> listCongNoNCC = new ArrayList<>();
    private ArrayList<RecordChiTietCongNo> listCongNoChiTiet = new ArrayList<>();
    private NhaCungCap ncc;

    public GDCongNoFrm() {
        initComponents();
        loadDanhSachCongNo();
    }
    void loadDanhSachCongNo() {
        try {
            DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Tên khách", "Điện thoại", "Email", "Số Tiền Nợ"}, 0);
            defaultTableModel.setRowCount(0);
            CongNoDAO congNoDAO = new CongNoDAO();
            listCongNoNCC = congNoDAO.loadBangCongNo();
            jTableCongNo.setModel(defaultTableModel);
            jTableCongNo.getColumnModel().getColumn(0).setPreferredWidth(10);
            JTableHeader header = jTableCongNo.getTableHeader();
            header.setResizingAllowed(false);
            jTableCongNo.setFocusable(false);
            jTableCongNo.setDefaultRenderer(String.class, new VisitorRenderer());
            jTableCongNo.setRowMargin(3);
            jTableCongNo.setRowSelectionAllowed(true);
            jTableCongNo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableCongNo.setSelectionForeground(new Color(204, 255, 255));

            for (int i = 0; i < listCongNoNCC.size(); i++) {
                RecordCongNoNCC recordCongNoNCC = listCongNoNCC.get(i);
                int stt = i;
                NhaCungCap cap = recordCongNoNCC.getNcc();
                int tienConNo = recordCongNoNCC.getTienConNo();
                String tenKhach = cap.getTen();
                String email = cap.getEmail();
                String sdt = cap.getSodienthoai();
                defaultTableModel.addRow(new Object[]{stt, tenKhach, email, sdt, dinhDangTien(tienConNo)});
            }
            defaultTableModel.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void loadDanhSachChiTietCongNo() {
        PhieuThuChiDAO phieuThuChiDAO = new PhieuThuChiDAO();
        if(ncc==null) {
                    ncc=new NhaCungCap();
                    ncc.setTen("");
        }
        listCongNoChiTiet = phieuThuChiDAO.getAlPTCByName(ncc.getTen());
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Loại Phiếu", "Số phiếu", "Ngày", "Tiền nợ", "Đã thanh toán", "Lũy kế"}, 0);
        //defaultTableModel.setRowCount(0);
        jTableChiTietCongNO.setModel(defaultTableModel);
        jTableChiTietCongNO.getColumnModel().getColumn(0).setPreferredWidth(10);
        JTableHeader header = jTableChiTietCongNO.getTableHeader();
        header.setResizingAllowed(false);
        jTableChiTietCongNO.setFocusable(false);
        jTableChiTietCongNO.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableChiTietCongNO.setRowMargin(3);
        jTableChiTietCongNO.setRowSelectionAllowed(true);
        jTableChiTietCongNO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableChiTietCongNO.setSelectionForeground(new Color(204, 255, 255));
        int sum = 0;
        int luyKe=0;
        for (int i = 0; i < listCongNoChiTiet.size(); i++) {
            RecordChiTietCongNo recordChiTietCongNo = listCongNoChiTiet.get(i);
            int stt = i;
            PhieuThuChi cap = recordChiTietCongNo.getPhieuThuChi();
            CongNo cn = recordChiTietCongNo.getCn();
            luyKe +=cn.getSoTienNo() - cap.getSoTien();
            defaultTableModel.addRow(new Object[]{stt, cap.getLoaiPhieu(), cap.getSoPhieu(), cap.getNgayLap(), cn.getSoTienNo(), cap.getSoTien(), dinhDangTien(luyKe)});
        }
        defaultTableModel.fireTableDataChanged();
    }

    public class VisitorRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (hasFocus) {
                setBorder(new LineBorder(Color.BLACK));
            }
            return this;
        }
    }

    String dinhDangTien(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = df.format(new BigDecimal(number));
        return s;
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCongNo = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableChiTietCongNO = new javax.swing.JTable();
        jButtonThanhToan = new javax.swing.JButton();
        jButtonLamMoi = new javax.swing.JButton();
        jTextFieldTimKiem = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButtonTim = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_gold_bars_48px_1.png"))); // NOI18N
        jLabel1.setText("CÔNG NỢ");

        jTableCongNo.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableCongNo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên khách", "Địa chỉ", "Điện thoại", "Email", "Số tiền nợ"
            }
        ));
        jTableCongNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCongNoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCongNo);

        jTableChiTietCongNO.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableChiTietCongNO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Loại phiếu", "Số phiếu", "Ngày", "Tổng cộng", "Thanh toán", "Lũy kế "
            }
        ));
        jScrollPane2.setViewportView(jTableChiTietCongNO);

        jButtonThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonThanhToan.setText("Thanh toán");
        jButtonThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThanhToanActionPerformed(evt);
            }
        });

        jButtonLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonLamMoi.setText("Làm mới");
        jButtonLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLamMoiActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonTim.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/magnifying-glass16x16.png"))); // NOI18N
        jButtonTim.setText("Tìm ");
        jButtonTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTimActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setText("Cửa Hàng");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonThanhToan)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonLamMoi)
                        .addGap(57, 57, 57)
                        .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonTim))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(143, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonLamMoi, jButtonThanhToan, jButtonTim, jComboBox1});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonThanhToan)
                    .addComponent(jButtonLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonTim))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonLamMoi, jButtonThanhToan, jButtonTim, jComboBox1, jTextFieldTimKiem});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(607, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThanhToanActionPerformed
        try {
            int row = jTableCongNo.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn nhà cung cấp bạn muốn thanh toán!", "Cảnh báo chọn đối tác", JOptionPane.WARNING_MESSAGE);
            } else {
                RecordCongNoNCC recordCongNoNCC = listCongNoNCC.get(row);
                NhaCungCap cap = recordCongNoNCC.getNcc();
                GDTaoPhieuThuChi dTaoPhieuThuChi = new GDTaoPhieuThuChi(cap);
                dTaoPhieuThuChi.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                dTaoPhieuThuChi.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        JFrame frame = (JFrame) e.getSource();

                        int result = JOptionPane.showConfirmDialog(
                                frame,
                                "Are you sure you want to exit the application?",
                                "Exit Application",
                                JOptionPane.YES_NO_OPTION);

                        if (result == JOptionPane.YES_OPTION) {
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            loadDanhSachChiTietCongNo();
                        }
                    }
                });
                dTaoPhieuThuChi.pack();
                dTaoPhieuThuChi.setLocationRelativeTo(null);
                dTaoPhieuThuChi.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButtonThanhToanActionPerformed

    private void jTableCongNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCongNoMouseClicked
        int row = jTableCongNo.getSelectedRow();
        if (row != -1) {
            RecordCongNoNCC congNoNCC = listCongNoNCC.get(row);
            NhaCungCap nhaCungCap = congNoNCC.getNcc();
            this.ncc = nhaCungCap;
            loadDanhSachChiTietCongNo();
        }
    }//GEN-LAST:event_jTableCongNoMouseClicked

    private void jButtonLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLamMoiActionPerformed
        loadDanhSachCongNo();
        loadDanhSachChiTietCongNo();
    }//GEN-LAST:event_jButtonLamMoiActionPerformed
    private void jButtonTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTimActionPerformed
       
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonTimActionPerformed

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
            java.util.logging.Logger.getLogger(GDCongNoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDCongNoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDCongNoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDCongNoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new GDCongNoFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLamMoi;
    private javax.swing.JButton jButtonThanhToan;
    private javax.swing.JButton jButtonTim;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableChiTietCongNO;
    private javax.swing.JTable jTableCongNo;
    private javax.swing.JTextField jTextFieldTimKiem;
    // End of variables declaration//GEN-END:variables
}
