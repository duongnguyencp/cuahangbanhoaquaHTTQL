/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.BienLaiKhoDAO;
import control.KhoDAO;
import control.NhanVienDAO;
import control.SanPhamDAO;
import control.XuatHangDAO;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.RecordSanPham;
import model.*;
import control.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Duong
 */
public class GDQLPhieuThuChi extends javax.swing.JFrame {

    /**
     * Creates new form GDQLPhieuThuChi
     */
    private ArrayList<RecordSanPham> listSanPham2 = new ArrayList<>();
    private Kho selectedKho;
    private NhanVien selectNV;
    private ArrayList<RecordSanPham> listSanPhamSelected = new ArrayList<>();
    private int tienHang = 0;
    public GDQLPhieuThuChi() {
        initComponents();
        Kho kho = new Kho();
        kho.setId(1);
        loadSanPham(kho);
        loadKho();
        loadNV();
        addListenerText((jTextFieldGiamGia));
        addListenerText((jTextFieldTiLeThue));
        jTextFieldSoHD.setText(createMatBienLai());
    }
//thÃªm sá»± kiá»‡n Ã´ nháº­p Ä‘Æ¡n giÃ¡ Ä‘á»‹nh dáº¡ng money
    //thÃªm sá»± kiá»‡n Ã´ nháº­p Ä‘Æ¡n giÃ¡ Ä‘á»‹nh dáº¡ng money

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
                    if (field.equals(jTextFieldGiamGia)) {
                        tinhGiamGia();
                    } else if (field.equals(jTextFieldTiLeThue)) {
                        tinhTiLeThue();
                    } else if (field.equals(jTextFieldVanChuyen)) {
                        tinhTongCong();
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

    void tinhTongCong() {
        int number = 0;
        int number1 = Integer.parseInt(jTextFieldGiamGia2.getText());
        int number2 = Integer.parseInt(jTextFieldTiLeThue2.getText());
        int number3 = Integer.parseInt(jTextFieldVanChuyen.getText());
        number += tienHang - number1 - number2 - number3;
        jTextFieldTongCong.setText(dinhDangTien(number));
    }

    void tinhGiamGia() {
        int number = Integer.parseInt(jTextFieldGiamGia.getText());
        number = number * tienHang / 100;
        jTextFieldGiamGia2.setText(dinhDangTien(number));
    }

    void tinhTiLeThue() {
        int number = Integer.parseInt(jTextFieldTiLeThue.getText());
        number = number * tienHang / 100;
        jTextFieldTiLeThue2.setText(dinhDangTien(number));
    }

    int countDigit(int number) {
        int count = 0;
        while (number > 0) {
            number /= 10;
            count += 1;
        }
        return count;
    }

    void loadNV() {
        jComboBoxNhanVien.removeAllItems();
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        ArrayList<NhanVien> listNV = nhanVienDAO.getAllNVBanHang();
        selectNV = listNV.get(0);
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
                selectNV = listNV.get(stt);
            }
        });
    }

    String createMatBienLai() {
        HoaDonBanHangDAO aO = new HoaDonBanHangDAO();
        ArrayList<HoaDonBanHang> hoaDonBanHangs = aO.getHoaDonBanHangs();
        int count = countDigit(hoaDonBanHangs.size() + 1);
        String maBH = "BH-";
        int rest = 7 - count;
        while (rest > 0) {
            maBH += '0';
            rest--;
        }
        maBH = maBH + "" + hoaDonBanHangs.size();
        return maBH;
    }

    String dinhDangTien(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = df.format(new BigDecimal(number));
        return s;
    }

    void loadSanPham() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "MÃ£ máº·t hÃ ng", "TÃªn máº·t hÃ ng", "GiÃ¡ bÃ¡n láº»", "Háº¡n sá»­ dá»¥ng", "Ä�VT", "Sá»‘ lÆ°á»£ng"}, 0);
        defaultTableModel.setRowCount(0);
        jTableSanPham.setModel(defaultTableModel);
        jTableSanPham.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableSanPham.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableSanPham.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTableSanPham.getColumnModel().getColumn(6).setPreferredWidth(15);
        JTableHeader header = jTableSanPham.getTableHeader();
        header.setResizingAllowed(false);
        jTableSanPham.setFocusable(false);
        jTableSanPham.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableSanPham.setRowMargin(3);
        jTableSanPham.setRowSelectionAllowed(true);
        jTableSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableSanPham.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        for (int i = 0; i < listSanPham2.size(); i++) {
            RecordSanPham recordSanPham = listSanPham2.get(i);
            SanPham sp = recordSanPham.getPham();
            int soLuong = recordSanPham.getSoLuong();
            defaultTableModel.addRow(new Object[]{stt, sp.getMaMatHang(), sp.getTenMatHang(), sp.getGia(), sp.getHanSuDung(), sp.getDonViTinh(), soLuong});
            stt++;
        }

        defaultTableModel.fireTableDataChanged();
    }

    void loadSanPham(Kho kho) {
        XuatHangDAO spdao = new XuatHangDAO();
        listSanPham2 = spdao.loadSanPhamXuatKho(kho);
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "MÃ£ máº·t hÃ ng", "TÃªn máº·t hÃ ng", "GiÃ¡ bÃ¡n láº»", "Háº¡n sá»­ dá»¥ng", "Ä�VT", "Sá»‘ lÆ°á»£ng"}, 0);
        defaultTableModel.setRowCount(0);
        jTableSanPham.setModel(defaultTableModel);
        jTableSanPham.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableSanPham.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableSanPham.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTableSanPham.getColumnModel().getColumn(6).setPreferredWidth(15);
        JTableHeader header = jTableSanPham.getTableHeader();
        header.setResizingAllowed(false);
        jTableSanPham.setFocusable(false);
        jTableSanPham.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableSanPham.setRowMargin(3);
        jTableSanPham.setRowSelectionAllowed(true);
        jTableSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableSanPham.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        for (int i = 0; i < listSanPham2.size(); i++) {
            RecordSanPham recordSanPham = listSanPham2.get(i);
            SanPham sp = recordSanPham.getPham();
            int soLuong = recordSanPham.getSoLuong();
            defaultTableModel.addRow(new Object[]{stt, sp.getMaMatHang(), sp.getTenMatHang(), sp.getGia(), sp.getHanSuDung(), sp.getDonViTinh(), soLuong});
            stt++;
        }

        defaultTableModel.fireTableDataChanged();
    }

    void loadSanPhamDaChon() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "MÃ£ máº·t hÃ ng", "TÃªn máº·t hÃ ng", "Sá»‘ lÆ°á»£ng", "Ä�VT", "Ä�Æ¡n giÃ¡", "CK%", "Tiá»�n giáº£m", "ThÃ nh tiá»�n"}, 0);
        defaultTableModel.setRowCount(0);
        jTableSpDaChon.setModel(defaultTableModel);
        jTableSpDaChon.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableSpDaChon.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableSpDaChon.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTableSpDaChon.getColumnModel().getColumn(6).setPreferredWidth(15);
        JTableHeader header = jTableSpDaChon.getTableHeader();
        header.setResizingAllowed(false);
        jTableSpDaChon.setFocusable(false);
        jTableSpDaChon.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableSpDaChon.setRowMargin(3);
        jTableSpDaChon.setRowSelectionAllowed(true);
        jTableSpDaChon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableSpDaChon.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        this.tienHang = 0;
        for (int i = 0; i < listSanPhamSelected.size(); i++) {
            RecordSanPham recordSanPham = listSanPhamSelected.get(i);
            tiLeCK = listTiLeCK.get(i);
            SanPham sp = recordSanPham.getPham();
            int soLuong = recordSanPham.getSoLuong();
            int thanhTien = sp.getGia() - tiLeCK * sp.getGia() / 100;
            tienHang += thanhTien;
            defaultTableModel.addRow(new Object[]{stt, sp.getMaMatHang(), sp.getTenMatHang(), soLuong, sp.getDonViTinh(), sp.getGia(), tiLeCK, dinhDangTien(sp.getGia() * tiLeCK / 100), dinhDangTien(thanhTien)});
            stt++;
        }

        defaultTableModel.fireTableDataChanged();
    }

    void loadKho() {
        jComboBoxKho.removeAllItems();
        KhoDAO khoDAO = new KhoDAO();
        ArrayList<Kho> listKho = khoDAO.getAllKho();
        selectedKho = listKho.get(0);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < listKho.size(); i++) {
            model.addElement("Kho á»Ÿ " + listKho.get(i).getDiaChi());
        }
        jComboBoxKho.setModel(model);
        jComboBoxKho.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox comboBoxTest = (JComboBox) e.getSource();
                int stt = comboBoxTest.getSelectedIndex();
                selectedKho = listKho.get(stt);
                loadSanPham(selectedKho);
            }
        });
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

    boolean check2RecordSp(RecordSanPham rsp1, RecordSanPham rsp2) {
        if (rsp1.getPham().getIdSanPham() == rsp2.getPham().getIdSanPham()) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSanPham = new javax.swing.JTable();
        jButtonThem = new javax.swing.JButton();
        jComboBoxKho = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerSoLuong = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooserNgayLap = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldSoHD = new javax.swing.JTextField();
        jTextFieldDienGiai = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSpDaChon = new javax.swing.JTable();
        jComboBoxNhanVien = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldGhiChu = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldTienHang = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldGiamGia = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldGiamGia2 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldTiLeThue = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldTiLeThue2 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldVanChuyen = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldTongCong = new javax.swing.JTextField();
        jButtonThanhToan = new javax.swing.JButton();
        jButtonXoaDong = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldCK = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_checkout_48px_1.png"))); // NOI18N
        jLabel2.setText("HÃ³a Ä‘Æ¡n bÃ¡n hÃ ng");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
        );

        jTableSanPham.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "", "MÃ£ hÃ ng", "TÃªn hÃ ng", "GiÃ¡ bÃ¡n láº»", "Ä�VT", "Háº¡n sá»­ dá»¥ng"
            }
        ));
        jScrollPane1.setViewportView(jTableSanPham);

        jButtonThem.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonThem.setText("ThÃªm");
        jButtonThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonThem, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jComboBoxKho.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBoxKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel4.setText("Kho");

        jLabel3.setText("Sá»‘ LÆ°á»£ng");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel6.setText("NgÃ y láº­p");

        jDateChooserNgayLap.setDateFormatString("dd/MM/yyy\n\n");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel5.setText("Sá»‘ HÄ�");

        jTextFieldSoHD.setEditable(false);
        jTextFieldSoHD.setBackground(new java.awt.Color(204, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel8.setText("Diá»…n giáº£i");

        jLabel9.setText("NhÃ¢n viÃªn");

        jTableSpDaChon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MÃ£ hÃ ng", "TÃªn máº·t hÃ ng", "Sá»‘ lÆ°á»£ng", "Ä�VT", "Ä�Æ¡n giÃ¡", "CK%", "Tiá»�n giáº£m", "ThÃ nh tiá»�n"
            }
        ));
        jScrollPane2.setViewportView(jTableSpDaChon);

        jComboBoxNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel10.setText("Ghi chÃº");

        jTextFieldGhiChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGhiChuActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel11.setText("Tiá»�n hÃ ng");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel12.setText("Giáº£m giÃ¡");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel13.setText("%");

        jTextFieldGiamGia2.setEditable(false);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel14.setText("Tá»‰ lá»‡ thuáº¿");

        jLabel15.setText("%");

        jTextFieldTiLeThue2.setEditable(false);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel16.setText("PhÃ­ váº­n chuyá»ƒn");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel18.setText("Ä�á»•i tráº£");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Tá»•ng cá»™ng");

        jTextFieldTongCong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldTongCong.setForeground(new java.awt.Color(255, 51, 51));

        jButtonThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_cash_18px.png"))); // NOI18N
        jButtonThanhToan.setText("Thanh toÃ¡n");
        jButtonThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThanhToanActionPerformed(evt);
            }
        });

        jButtonXoaDong.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonXoaDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_delete_18px.png"))); // NOI18N
        jButtonXoaDong.setText("XÃ³a");
        jButtonXoaDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaDongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButtonThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonXoaDong, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldTienHang)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldTiLeThue, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                                    .addComponent(jTextFieldGiamGia, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldGiamGia2))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldTiLeThue2))))
                            .addComponent(jTextFieldVanChuyen)
                            .addComponent(jTextField10)
                            .addComponent(jTextFieldTongCong, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(16, 16, 16)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldGhiChu, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                    .addComponent(jTextFieldDienGiai)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(15, 15, 15)
                                .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel5))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldSoHD)
                            .addComponent(jComboBoxNhanVien, 0, 187, Short.MAX_VALUE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldSoHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDienGiai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jTextFieldTienHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonThanhToan)
                                    .addComponent(jButtonXoaDong))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jTextFieldGiamGia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldTiLeThue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldTiLeThue2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldVanChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(15, 15, 15)
                        .addComponent(jLabel19))
                    .addComponent(jTextFieldTongCong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxNhanVien, jDateChooserNgayLap, jTextFieldDienGiai, jTextFieldGhiChu, jTextFieldSoHD});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTextField10, jTextFieldGiamGia, jTextFieldGiamGia2, jTextFieldTiLeThue, jTextFieldTiLeThue2, jTextFieldTienHang, jTextFieldTongCong, jTextFieldVanChuyen});

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel7.setText("Chiáº¿t kháº¥u");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCK, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 221, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonXoaDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaDongActionPerformed
        int row = jTableSpDaChon.getSelectedRow();
        boolean check = true;
        if (row == -1) {
            check = false;
            JOptionPane.showMessageDialog(null, "chá»�n dÃ²ng cáº§n xÃ³a", "cáº£nh bÃ¡o chá»�n dÃ²ng xÃ³a", JOptionPane.WARNING_MESSAGE);
        } else {
            RecordSanPham rsp = listSanPhamSelected.remove(row);
            for (int i = 0; i < listSanPham2.size(); i++) {
                RecordSanPham rsp2 = listSanPham2.get(i);
                int sl = rsp2.getSoLuong();
                if (check2RecordSp(rsp, rsp2)) {
                    rsp2.setSoLuong(sl + rsp.getSoLuong());
                }
            }
            loadSanPham();
            loadSanPhamDaChon();
        }
    }//GEN-LAST:event_jButtonXoaDongActionPerformed

    private void jTextFieldGhiChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGhiChuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGhiChuActionPerformed
    private int tiLeCK;
    ArrayList<Integer> listTiLeCK = new ArrayList<>();
    private void jButtonThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThemActionPerformed

        int row = jTableSanPham.getSelectedRow();
        boolean check = true;
        int soLuong = (Integer) jSpinnerSoLuong.getValue();
        if (row == -1) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chá»�n máº·t hÃ ng báº¡n muá»‘n thÃªm!", "cáº£nh bÃ¡o chá»�n máº·t hÃ ng", JOptionPane.WARNING_MESSAGE);
        } else if (soLuong > listSanPham2.get(row).getSoLuong()) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chá»�n sá»‘ lÆ°á»£ng nhá»� hÆ¡n !", "cáº£nh bÃ¡o chá»�n quÃ¡ sá»‘ lÆ°á»£ng sáº£n pháº§m cÃ³", JOptionPane.WARNING_MESSAGE);
        } else {
            if (!jTextFieldCK.getText().equals("")) {
                tiLeCK = Integer.parseInt(jTextFieldCK.getText());
            }
            RecordSanPham recordSanPham = listSanPham2.get(row);
            int soLuongBD = recordSanPham.getSoLuong();
            SanPham sp = recordSanPham.getPham();
            recordSanPham.setSoLuong(soLuongBD - soLuong);
            listSanPham2.set(row, recordSanPham);
            recordSanPham = new RecordSanPham();
            recordSanPham.setPham(sp);
            recordSanPham.setSoLuong(soLuong);
            listSanPhamSelected.add(recordSanPham);
            listTiLeCK.add(tiLeCK);
            loadSanPhamDaChon();
            loadSanPham();
            jTextFieldTienHang.setText(dinhDangTien(tienHang));
        }

    }//GEN-LAST:event_jButtonThemActionPerformed
    int tinhSoLuong() {
        int number = 0;
        for (int i = 0; i < listSanPhamSelected.size(); i++) {
            number += listSanPhamSelected.get(i).getSoLuong();
        }
        return number;
    }
    private void jButtonThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThanhToanActionPerformed
        String ngayLap = ((JTextField) jDateChooserNgayLap.getDateEditor().getUiComponent()).getText();
        String soPhieu = jTextFieldSoHD.getText();
        boolean check = true;
        if (ngayLap.equals("")) {
            check = false;
            JOptionPane.showMessageDialog(null, "chá»�n ngÃ y láº­p", "cáº£nh bÃ¡o chá»�n ngÃ y láº­p", JOptionPane.WARNING_MESSAGE);
        } else if (listSanPhamSelected.size() > 0) {
            ArrayList<HoaDonBanHang> hoaDonBanHangs = new ArrayList<>();
            for (int i = 0; i < listSanPhamSelected.size(); i++) {
                RecordSanPham recordSanPham = listSanPhamSelected.get(i);
                SanPham sp = recordSanPham.getPham();
                HoaDonBanHang hoaDonBanHang = new HoaDonBanHang();
                hoaDonBanHang.setNgay(ngayLap);
                hoaDonBanHang.setNv(selectNV);
                hoaDonBanHang.setSoLuong(tinhSoLuong());
                hoaDonBanHang.setSoTien(tienHang);
                hoaDonBanHang.setSp(sp);
                hoaDonBanHangs.add(hoaDonBanHang);
            }
            PhieuThuChi phieuThuChi = new PhieuThuChi();
            phieuThuChi.setNgayLap(ngayLap);
            phieuThuChi.setDienGiai(jTextFieldDienGiai.getText());
            phieuThuChi.setLoaiPhieu("Phiáº¿u bÃ¡n hÃ ng");
            phieuThuChi.setLyDo(jTextFieldGhiChu.getText());
            phieuThuChi.setNv(selectNV);
            phieuThuChi.setTenDoiTuong("KhÃ¡ch hÃ ng");
            phieuThuChi.setSoPhieu(soPhieu);
            GDXacNhanBanHang dXacNhanBanHang = new GDXacNhanBanHang(tienHang, hoaDonBanHangs, phieuThuChi);
            dXacNhanBanHang.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            dXacNhanBanHang.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    JFrame frame = (JFrame) e.getSource();
                    int result = JOptionPane.showConfirmDialog(
                            frame,
                            "CÃ³ pháº£i báº¡n muá»‘n Ä‘Ã³ng cá»­a sá»• nÃ y?",
                            "Exit Application",
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        loadSanPham(selectedKho);
                        listSanPhamSelected.clear();;
                        loadSanPhamDaChon();
                    }
                }
            });
            dXacNhanBanHang.pack();
            dXacNhanBanHang.setLocationRelativeTo(null);
            dXacNhanBanHang.setVisible(true);
        }
    }//GEN-LAST:event_jButtonThanhToanActionPerformed

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
            java.util.logging.Logger.getLogger(GDQLPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDQLPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDQLPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDQLPhieuThuChi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new GDQLPhieuThuChi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonThanhToan;
    private javax.swing.JButton jButtonThem;
    private javax.swing.JButton jButtonXoaDong;
    private javax.swing.JComboBox<String> jComboBoxKho;
    private javax.swing.JComboBox<String> jComboBoxNhanVien;
    private com.toedter.calendar.JDateChooser jDateChooserNgayLap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerSoLuong;
    private javax.swing.JTable jTableSanPham;
    private javax.swing.JTable jTableSpDaChon;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextFieldCK;
    private javax.swing.JTextField jTextFieldDienGiai;
    private javax.swing.JTextField jTextFieldGhiChu;
    private javax.swing.JTextField jTextFieldGiamGia;
    private javax.swing.JTextField jTextFieldGiamGia2;
    private javax.swing.JTextField jTextFieldSoHD;
    private javax.swing.JTextField jTextFieldTiLeThue;
    private javax.swing.JTextField jTextFieldTiLeThue2;
    private javax.swing.JTextField jTextFieldTienHang;
    private javax.swing.JTextField jTextFieldTongCong;
    private javax.swing.JTextField jTextFieldVanChuyen;
    // End of variables declaration//GEN-END:variables
}
