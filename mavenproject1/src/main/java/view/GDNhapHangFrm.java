/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import control.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.UIManager;
import javax.swing.*;
import java.util.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import model.*;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Duong
 */
public class GDNhapHangFrm extends javax.swing.JFrame implements Job {

    private NhaCungCap nccSelected;
    private ArrayList<NhaCungCap> listNcc;
    private Kho khoSelected;
    private ArrayList<Kho> listKho;
    private ArrayList<NhanVien> listNV;
    private NhanVien nvSelected;
    private ArrayList<MatHang> listMatHang;
    private ArrayList<MatHang> listMatHangSearch = new ArrayList<>();
    private int giaMatHang;
    private ArrayList<RecordSanPham> listMatHangDaChon = new ArrayList<>();

    public GDNhapHangFrm() {
        initComponents();
        loadNCC();
        loadKho();
        loadNV();
        loadDanhSachMH();
        addListenerText(jTextFieldDonGia);
        loadThemMatHangDaChon();
        createMatBienLai();
        loadUpdateDB();
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        loadDanhSachMH();
    }

    void loadUpdateDB() {
        Thread run;
        Runnable task = null;
        run = new Thread(task);
        task = new Runnable() {
            boolean exit = false;

            @Override
            public void run() {
                while (!exit) {
                    loadNCC();
                    loadNV();
                    loadDanhSachMH();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            public void stop() {
                exit = true;
            }
        };
        run = new Thread(task);
        run.start();
    }

    int countDigit(int number) {
        int count = 0;
        while (number > 0) {
            number /= 10;
            count += 1;
        }
        return count;
    }
    private String maBienLai;

    void createMatBienLai() {
        BienLaiKhoDAO aO = new BienLaiKhoDAO();
        ArrayList<BienLaiKho> listBienLaiKho = aO.getAllBienLaiKho();
        int count = countDigit(listBienLaiKho.size() + 1);
        this.maBienLai = "PN-";
        int rest = 7 - count;
        while (rest > 0) {
            maBienLai += '0';
            rest--;
        }
        this.maBienLai = this.maBienLai + "" + listBienLaiKho.size();
        jTextFieldMaBienLai.setText(this.maBienLai);
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
                try {
                    if (!field.getText().equals("")) {
                        DecimalFormat df = new DecimalFormat("#,##0");
                        giaMatHang = Integer.parseInt(field.getText());
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
    // tải dữ liệu từ danh sách mặt hàng tìm kiếm vào bảng mặt hàng

    void loadDanhSachMH(ArrayList<MatHang> listMatHang) {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã mặt hàng", "Tên mặt hàng", "ĐVT", "Số lượng"}, 0);
        defaultTableModel.setRowCount(0);
        jTableDanhSachMH.setModel(defaultTableModel);
        jTableDanhSachMH.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTableDanhSachMH.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableDanhSachMH.getColumnModel().getColumn(4).setPreferredWidth(10);
        JTableHeader header = jTableDanhSachMH.getTableHeader();
        header.setResizingAllowed(false);
        jTableDanhSachMH.setFocusable(false);
        jTableDanhSachMH.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableDanhSachMH.setRowMargin(3);
        jTableDanhSachMH.setRowSelectionAllowed(true);
        jTableDanhSachMH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableDanhSachMH.setSelectionForeground(new Color(204, 255, 255));

        for (int i = 0; i < listMatHang.size(); i++) {
            int stt = i;
            String ma = listMatHang.get(i).getMaMatHang();
            String ten = listMatHang.get(i).getTenMatHang();
            String donVi = listMatHang.get(i).getDonViTinh();
            String moTa = listMatHang.get(i).getMoTa();
            defaultTableModel.addRow(new Object[]{stt, ma, ten, donVi, moTa});
        }
        defaultTableModel.fireTableDataChanged();
    }

    // tải dữ liệu từ cơ sở dữ liệu vào bảng mặt hàng
    void loadDanhSachMH() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã mặt hàng", "Tên mặt hàng", "ĐVT", "Mô tả"}, 0);
        defaultTableModel.setRowCount(0);
        System.out.println("loadDanhSachMH");
        listMatHang = new MatHangDAO().getAllMatHang();
        jTableDanhSachMH.setModel(defaultTableModel);
        jTableDanhSachMH.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTableDanhSachMH.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableDanhSachMH.getColumnModel().getColumn(4).setPreferredWidth(10);
        JTableHeader header = jTableDanhSachMH.getTableHeader();
        header.setResizingAllowed(false);
        jTableDanhSachMH.setFocusable(false);
        jTableDanhSachMH.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableDanhSachMH.setRowMargin(3);
        jTableDanhSachMH.setRowSelectionAllowed(true);
        jTableDanhSachMH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableDanhSachMH.setSelectionForeground(new Color(204, 255, 255));

        for (int i = 0; i < listMatHang.size(); i++) {
            int stt = i;
            String ma = listMatHang.get(i).getMaMatHang();
            String ten = listMatHang.get(i).getTenMatHang();
            String donVi = listMatHang.get(i).getDonViTinh();
            String moTa = listMatHang.get(i).getMoTa();
            defaultTableModel.addRow(new Object[]{stt, ma, ten, donVi, moTa});
        }
        defaultTableModel.fireTableDataChanged();
    }

    // tải dữ liệu nhân viên kho từ cơ sở dữ liệu
    void loadNV() {
        jComboBoxNhanVien.removeAllItems();
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        this.listNV = nhanVienDAO.getAllNVKho();
        nvSelected = listNV.get(0);
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
                if(stt!=-1)
                nvSelected = listNV.get(stt);
            }
        });
    }

    // tải dữ liệu kho từ csdl
    void loadKho() {
        jComboBoxKho.removeAllItems();
        KhoDAO khoDAO = new KhoDAO();
        this.listKho = khoDAO.getAllKho();
        khoSelected = listKho.get(0);

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < listKho.size(); i++) {
            model.addElement("Kho ở " + listKho.get(i).getDiaChi());
        }

        jComboBoxKho.setModel(model);
        jComboBoxKho.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox comboBoxTest = (JComboBox) e.getSource();
                int stt = comboBoxTest.getSelectedIndex();
                khoSelected = listKho.get(stt);
            }
        });
    }

    // tải danh sách nhà cung cấp từ csdl
    void loadNCC() {
        jComboBoxNcc.removeAllItems();
        NhaCungCapDAO nccdao = new NhaCungCapDAO();
        this.listNcc = nccdao.getAllNhaCungCap();
        nccSelected = listNcc.get(0);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < listNcc.size(); i++) {
            model.addElement(listNcc.get(i).getTen());
        }
        jComboBoxNcc.setModel(model);
        jComboBoxNcc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox comboBoxTest = (JComboBox) e.getSource();
                int stt = comboBoxTest.getSelectedIndex();
                if(stt!=-1)
                nccSelected = listNcc.get(stt);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField5 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jDateChooserNgayLap = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldGhiChu = new javax.swing.JTextField();
        jTextFieldMaBienLai = new javax.swing.JTextField();
        jComboBoxKho = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldLyDo = new javax.swing.JTextField();
        jComboBoxNhanVien = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxNcc = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButtonTimKiem = new javax.swing.JButton();
        jButtonTaoMoi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDanhSachMH = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTextFieldTimKiem = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerSoLuong = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldDonGia = new javax.swing.JTextField();
        JButtonThem = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jDateChooserHanSd = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButtonXoaDong = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMatHangThem = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButtonThanhToan = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(162, 174, 240));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jDateChooserNgayLap.setDateFormatString("dd/MM/yyyy");

        jLabel2.setText("Nhà cung cấp");

        jTextFieldMaBienLai.setEditable(false);

        jComboBoxKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kho Hà Nội", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Nhân viên nhập");

        jLabel1.setText("Ngày");

        jComboBoxNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NV Nguyễn Thị Loan", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Số phiếu");

        jComboBoxNcc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chị Yến", "Item 2", "Item 3", "Item 4" }));
        jComboBoxNcc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxNccItemStateChanged(evt);
            }
        });
        jComboBoxNcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxNccMouseClicked(evt);
            }
        });

        jLabel3.setText("Kho nhập");

        jLabel4.setText("Diễn giải");

        jLabel7.setText("Ghi chú");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxNcc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldGhiChu)
                    .addComponent(jTextFieldLyDo)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(jTextFieldMaBienLai)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldMaBienLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxNcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldLyDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxKho, jComboBoxNcc, jComboBoxNhanVien, jDateChooserNgayLap, jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jTextFieldGhiChu, jTextFieldLyDo, jTextFieldMaBienLai});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tổng cộng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel11.setText("Tiền Hàng");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel13.setText("Tỉ lệ thuế");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel14.setText("Phí vận chuyển");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel15.setText("Tổng cộng");

        jTextField12.setEditable(false);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel17.setText("Tỉ lệ lãi");

        jTextField20.setEnabled(false);

        jLabel23.setText("%");

        jLabel16.setText("%");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel17)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14))
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField13)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(jTextField20)))
                    .addComponent(jTextField7)
                    .addComponent(jTextField10))
                .addContainerGap(186, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addGap(10, 10, 10)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 24, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTextField10, jTextField12, jTextField7, jTextField8});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel11, jLabel13, jLabel14, jLabel15});

        jButtonTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/magnifying-glass16x16.png"))); // NOI18N
        jButtonTimKiem.setText("Tìm kiếm");
        jButtonTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTimKiemActionPerformed(evt);
            }
        });

        jButtonTaoMoi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonTaoMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/themmathang24x24.png"))); // NOI18N
        jButtonTaoMoi.setText("Tạo mới");
        jButtonTaoMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTaoMoiActionPerformed(evt);
            }
        });

        jTableDanhSachMH.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableDanhSachMH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "Mã hàng", "Tên hàng", "ĐVT", "Mô tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDanhSachMH.setFocusable(false);
        jTableDanhSachMH.setOpaque(false);
        jTableDanhSachMH.setRowMargin(3);
        jTableDanhSachMH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableDanhSachMH.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableDanhSachMH);
        jTableDanhSachMH.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTableDanhSachMH.getColumnModel().getColumnCount() > 0) {
            jTableDanhSachMH.getColumnModel().getColumn(0).setResizable(false);
            jTableDanhSachMH.getColumnModel().getColumn(1).setResizable(false);
            jTableDanhSachMH.getColumnModel().getColumn(2).setResizable(false);
            jTableDanhSachMH.getColumnModel().getColumn(3).setResizable(false);
            jTableDanhSachMH.getColumnModel().getColumn(4).setResizable(false);
        }

        jTable3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "", "Mã hàng", "Tên hàng", "ĐVT", "Giá", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setColumnSelectionAllowed(true);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable3);
        jTable3.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setResizable(false);
            jTable3.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel9.setText("Số lượng");

        jLabel10.setText("Đơn giá:");

        JButtonThem.setText("Thêm");
        JButtonThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonThemActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel25.setText("Hạn sử dụng");

        jDateChooserHanSd.setDateFormatString("dd/MM/yyyy\n");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JButtonThem))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButtonTaoMoi)
                                .addGap(10, 10, 10)
                                .addComponent(jButtonTimKiem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserHanSd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {JButtonThem, jButtonTaoMoi, jButtonTimKiem, jSpinnerSoLuong, jTextFieldTimKiem});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonThem)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooserHanSd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(46, 46, 46)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonTimKiem)
                    .addComponent(jButtonTaoMoi)
                    .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonTaoMoi, jButtonTimKiem, jTextFieldTimKiem});

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/import 24x24.png"))); // NOI18N
        jLabel12.setText("Phiếu nhập kho");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButtonXoaDong.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonXoaDong.setText("Xóa dòng");
        jButtonXoaDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaDongActionPerformed(evt);
            }
        });

        jTableMatHangThem.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableMatHangThem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Mã hàng", "Tên mặt hàng", "Hạn sử dụng", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMatHangThem.setRowMargin(3);
        jTableMatHangThem.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableMatHangThem);
        jTableMatHangThem.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableMatHangThem.getColumnModel().getColumnCount() > 0) {
            jTableMatHangThem.getColumnModel().getColumn(0).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(1).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(2).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(3).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(4).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(5).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(6).setResizable(false);
            jTableMatHangThem.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel18.setText("TỔNG");

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField14.setText("80");
        jTextField14.setEnabled(false);
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setText("326,000");
        jTextField15.setEnabled(false);

        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField16.setText("0");
        jTextField16.setEnabled(false);

        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField17.setText("6,520,000");
        jTextField17.setEnabled(false);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel19.setText("Số lượng");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel20.setText("Đơn giá");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel21.setText("Tỉ lệ thuế");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel22.setText("Thành tiền");

        jButton7.setText("In lại phiếu");

        jButtonThanhToan.setText("Thanh toán");
        jButtonThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThanhToanActionPerformed(evt);
            }
        });

        jButton8.setText("Thoát");

        jLabel8.setText("Tỉ lệ lãi");

        jTextField18.setEnabled(false);
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonThanhToan)))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addGap(147, 147, 147))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel8))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField18)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel22))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(162, 162, 162))))
                    .addComponent(jButtonXoaDong)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField14, jTextField15, jTextField16, jTextField17});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton7, jButton8, jButtonThanhToan, jButtonXoaDong});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonXoaDong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButtonThanhToan)
                    .addComponent(jButton8))
                .addGap(34, 34, 34))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton7, jButton8, jButtonThanhToan, jButtonXoaDong});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(42, 42, 42)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void jComboBoxNccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxNccMouseClicked
    }//GEN-LAST:event_jComboBoxNccMouseClicked

    private void jComboBoxNccItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxNccItemStateChanged

    }//GEN-LAST:event_jComboBoxNccItemStateChanged
    // sự kiện nút tạo mới
    private void jButtonTaoMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTaoMoiActionPerformed
        GDNhapMatHangFrm dNhapMatHangFrm = new GDNhapMatHangFrm();
        dNhapMatHangFrm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dNhapMatHangFrm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit the application?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    loadDanhSachMH();// tải lại danh sách mặt hàng
                }
            }
        });
        dNhapMatHangFrm.pack();
        dNhapMatHangFrm.setLocationRelativeTo(null);
        dNhapMatHangFrm.setVisible(true);
    }//GEN-LAST:event_jButtonTaoMoiActionPerformed

    void loadThemMatHangDaChon() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã mặt hàng", "Tên mặt hàng", "Hạn sử dụng", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
        defaultTableModel.setRowCount(0);
        jTableMatHangThem.setModel(defaultTableModel);
        jTableMatHangThem.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(5).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(6).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(7).setPreferredWidth(5);
        JTableHeader header = jTableMatHangThem.getTableHeader();
        header.setResizingAllowed(false);
        jTableMatHangThem.setFocusable(false);
        jTableMatHangThem.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableMatHangThem.setRowMargin(3);
        jTableMatHangThem.setRowSelectionAllowed(true);
        jTableMatHangThem.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        for (int i = 0; i < listMatHangDaChon.size(); i++) {
            RecordSanPham recordSanPham = listMatHangDaChon.get(i);
            SanPham pham = recordSanPham.getPham();
            int soluong = recordSanPham.getSoLuong();
            System.out.println("soluong+tensp" + soluong + " " + pham.getTenMatHang());
            String maMH = pham.getMaMatHang();
            String tenMH = pham.getTenMatHang();
            String hsd = pham.getHanSuDung();
            String dvt = pham.getDonViTinh();
            int gia = pham.getGia();
            int thanhTien = gia * soluong;
            defaultTableModel.addRow(new Object[]{stt, maMH, tenMH, hsd, dvt, soluong, dinhDangTien(gia), dinhDangTien(thanhTien)});
            stt++;
        }
        defaultTableModel.fireTableDataChanged();

    }
// xử lý sự kiến ấn nút tìm mặt hàng
    private void jButtonTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTimKiemActionPerformed
        ArrayList<MatHang> listMatHangTmp = new ArrayList<>();
        String txtSearch = jTextFieldTimKiem.getText();
        if (txtSearch.equals("")) {
            loadDanhSachMH();
            listMatHangSearch.clear();
        } else {
            for (int i = 0; i < listMatHang.size(); i++) {
                MatHang hang = listMatHang.get(i);
                if (hang.getTenMatHang() != null || hang.getMoTa() != null) {
                    if (hang.getTenMatHang().contains(txtSearch) || hang.getMoTa().contains(txtSearch)) {
                        listMatHangTmp.add(hang);
                    }
                }
            }
            listMatHangSearch = listMatHangTmp;
            loadDanhSachMH(listMatHangTmp);
        }
    }//GEN-LAST:event_jButtonTimKiemActionPerformed
    public boolean compareSanPham(SanPham sp1, SanPham sp2) {
        if (sp1.getIdMatHang() == sp2.getIdMatHang()) {
            return true;
        }
        return false;
    }
    private void JButtonThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonThemActionPerformed
        String donGia = jTextFieldDonGia.getText();
        boolean check = true;
        int row = jTableDanhSachMH.getSelectedRow();
        int soLuong = (int) jSpinnerSoLuong.getValue();
        String hsd = "";
        try {
            hsd = ((JTextField) jDateChooserHanSd.getDateEditor().getUiComponent()).getText();
        } catch (Exception e) {
            System.out.println("hsd chua duoc chon");
            e.printStackTrace();
        }
        if (row == -1) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chọn mặt hàng bạn muốn thêm!", "cảnh báo chọn mặt hàng", JOptionPane.WARNING_MESSAGE);

        } else if (donGia.equals("")) {
            check = false;
            JOptionPane.showMessageDialog(jTextFieldDonGia, "Điền giá mặt hàng bạn đã chọn!", "cảnh báo điền giá", JOptionPane.WARNING_MESSAGE);
            jTextFieldDonGia.requestFocus();
        }
        MatHang matHangThem;
        if (listMatHangSearch.size() != 0) {
            matHangThem = listMatHangSearch.get(row);
        } else {
            matHangThem = listMatHang.get(row);
        }
        SanPham sp = new SanPham(matHangThem);
        sp.setHanSuDung(hsd);
        sp.setGia(giaMatHang);
        String maSp = "SP";
        sp.setMaSp(maSp);
        System.out.println(sp.toString());
        if (check = true) {
            boolean check2 = false;
            for (int i = 0; i < listMatHangDaChon.size(); i++) {
                RecordSanPham recordSanPham = listMatHangDaChon.get(i);
                SanPham pham = recordSanPham.getPham();
                int soLuong2 = recordSanPham.getSoLuong();
                if (compareSanPham(pham, sp)) {
                    check2 = true;
                    recordSanPham.setSoLuong(soLuong + soLuong2);
                }
            }
            if (check2 == false) {
                RecordSanPham recordSanPham = new RecordSanPham();
                recordSanPham.setPham(sp);
                recordSanPham.setSoLuong(soLuong);
                listMatHangDaChon.add(recordSanPham);
            }
            loadThemMatHangDaChon();
        }
    }//GEN-LAST:event_JButtonThemActionPerformed

    private void jButtonXoaDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaDongActionPerformed
        int row = jTableMatHangThem.getSelectedRow();
        String ma = jTableMatHangThem.getValueAt(row, 1).toString();
        String tenMH = jTableMatHangThem.getValueAt(row, 2).toString();
        String hsd = jTableMatHangThem.getValueAt(row, 3).toString();
        String dvt = jTableMatHangThem.getValueAt(row, 4).toString();
        int soLuong = Integer.parseInt(jTableMatHangThem.getValueAt(row, 5).toString());
        String donGia = jTableMatHangThem.getValueAt(row, 6).toString();
        for (int i = 0; i < listMatHangDaChon.size(); i++) {
            RecordSanPham recordSanPham = new RecordSanPham();
            recordSanPham = listMatHangDaChon.get(i);
            SanPham pham = recordSanPham.getPham();
            int soluong2 = recordSanPham.getSoLuong();
            String maMH2 = pham.getMaMatHang();
            String tenMH2 = pham.getTenMatHang();
            String hsd2 = pham.getHanSuDung();
            String dvt2 = pham.getDonViTinh();
            int gia2 = pham.getGia();
            if (ma.equals(maMH2) && tenMH.equals(tenMH2) && hsd.equals(hsd2) && soLuong == soluong2 && row != -1) {
                listMatHangDaChon.remove(i);
                break;
            }

        }
        loadThemMatHangDaChon();
    }//GEN-LAST:event_jButtonXoaDongActionPerformed

    private void jButtonThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThanhToanActionPerformed
        String ngayNhap = ((JTextField) jDateChooserNgayLap.getDateEditor().getUiComponent()).getText();
        System.out.println(nccSelected.getTen() + " ten ncc da chon");
        System.out.println(nvSelected.getHoTen() + " ten nvSelected da chon");
        System.out.println(khoSelected.getDiaChi() + " ten khoSelected da chon");
        String dienGiai = jTextFieldLyDo.getText();
        String ghiChu = jTextFieldGhiChu.getText();
        GDXacNhanNhapHang dXacNhanNhapHang = new GDXacNhanNhapHang(listMatHangDaChon, nccSelected, nvSelected, ngayNhap, khoSelected, this.maBienLai, dienGiai, ghiChu);
        dXacNhanNhapHang.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dXacNhanNhapHang.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Có phải bạn muốn đóng cửa sổ này?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            }
        });
        dXacNhanNhapHang.pack();
        dXacNhanNhapHang.setLocationRelativeTo(null);
        dXacNhanNhapHang.setVisible(true);
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
            java.util.logging.Logger.getLogger(GDNhapHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDNhapHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDNhapHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDNhapHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                GDNhapHangFrm dNhapHangFrm = new GDNhapHangFrm();
                dNhapHangFrm.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonThem;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButtonTaoMoi;
    private javax.swing.JButton jButtonThanhToan;
    private javax.swing.JButton jButtonTimKiem;
    private javax.swing.JButton jButtonXoaDong;
    private javax.swing.JComboBox<String> jComboBoxKho;
    private javax.swing.JComboBox<String> jComboBoxNcc;
    private javax.swing.JComboBox<String> jComboBoxNhanVien;
    private com.toedter.calendar.JDateChooser jDateChooserHanSd;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinnerSoLuong;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTableDanhSachMH;
    private javax.swing.JTable jTableMatHangThem;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextFieldDonGia;
    private javax.swing.JTextField jTextFieldGhiChu;
    private javax.swing.JTextField jTextFieldLyDo;
    private javax.swing.JTextField jTextFieldMaBienLai;
    private javax.swing.JTextField jTextFieldTimKiem;
    // End of variables declaration//GEN-END:variables
}
