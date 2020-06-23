/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import control.BienLaiKhoDAO;
import control.BienLaiXuatDAO;
import control.CuaHangDAO;
import control.KhoDAO;
import control.MatHangDAO;
import control.NhaCungCapDAO;
import control.NhanVienDAO;
import control.XuatHangDAO;
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
public class GDXuatHangFrm extends javax.swing.JFrame implements Job {

    private Kho khoSelected;
    private ArrayList<Kho> listKho;
    private ArrayList<NhanVien> listNV;
    private NhanVien nvSelected;
    private ArrayList<CuaHang> listCuaHang;
    private CuaHang cuaHangSelected;
    private ArrayList<RecordSanPham> listMatHang = new ArrayList<RecordSanPham>();
    private ArrayList<RecordSanPham> listMatHangSearch = new ArrayList<RecordSanPham>();
    private ArrayList<RecordBienLaiXuat> listBienLaiXuat = new ArrayList<RecordBienLaiXuat>();

    public GDXuatHangFrm() {
        initComponents();
        loadKho();
        loadNV();
        loadCuaHang();
        loadDanhSachMH();
        addListenerText(jTextFieldTiLeLai);
        addListenerText(jTextFieldTiLeThue);
        loadThemMatHangDaChon();
        createMatBienLai();
        loadUpdateDB();
        jPanelLoading.setVisible(false);

    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        jPanelLoading.setVisible(true);
        loadDanhSachMH();        
//        try {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        for (int i = 0; i < 200; i++) {
//                            int m = jProgressBar1.getMaximum();
//                            int v = jProgressBar1.getValue();
//                            if (v < m) {
//                                jProgressBar1.setValue(jProgressBar1.getValue() + 1);
//                            } else {
//                                i = 201;
//                                jPanelLoading.setVisible(false);
//                                loadDanhSachMH();
//                            }
//                            Thread.sleep(8);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            t.start();
//
//        } catch (Exception e) {
//        }
        System.out.println("This is a quartz job!" + arg0.getFireTime());
    }

    void loadUpdateDB() {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

            sched.start();
            JobDetail job = newJob(GDXuatHangFrm.class)
                    .withIdentity("job1", "group1") // name "myJob", group "group1"
                    .build();
            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(10)
                            .withIntervalInSeconds(1)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
        } catch (Exception e) {
        }
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
        this.maBienLai = "PX-";
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
    void loadDanhSachMH(ArrayList<RecordSanPham> listMatHang) {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã mặt hàng", "Tên mặt hàng", "ĐVT", "Số lượng", "Giá"}, 0);
        defaultTableModel.setRowCount(0);
        jTableDanhSachMH.setModel(defaultTableModel);
        jTableDanhSachMH.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableDanhSachMH.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTableDanhSachMH.getColumnModel().getColumn(5).setPreferredWidth(5);
        JTableHeader header = jTableDanhSachMH.getTableHeader();
        header.setResizingAllowed(false);
        jTableDanhSachMH.setFocusable(false);
        jTableDanhSachMH.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableDanhSachMH.setRowMargin(3);
        jTableDanhSachMH.setRowSelectionAllowed(true);
        jTableDanhSachMH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableDanhSachMH.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        for (int i = 0; i < listMatHang.size(); i++) {
            RecordSanPham recordSanPham = listMatHang.get(i);
            SanPham sp = recordSanPham.getPham();
            int soLuong = recordSanPham.getSoLuong();
            String ma = sp.getMaMatHang();
            String ten = sp.getTenMatHang();
            String donVi = sp.getDonViTinh();
            int gia = sp.getGia();
            defaultTableModel.addRow(new Object[]{stt, ma, ten, donVi, soLuong, dinhDangTien(gia)});
            stt++;
        }

        defaultTableModel.fireTableDataChanged();
    }

    // tải dữ liệu từ cơ sở dữ liệu vào bảng mặt hàng
    void loadDanhSachMH() {
        System.out.println("loading...");
        XuatHangDAO aO = new XuatHangDAO();
        listMatHang = aO.loadMatHangTrongKhoTheoKho(khoSelected);
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã Sản Phẩm", "Mã mặt hàng", "Tên mặt hàng", "ĐVT", "Số lượng", "Giá"}, 0);
        defaultTableModel.setRowCount(0);
        jTableDanhSachMH.setModel(defaultTableModel);
        jTableDanhSachMH.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableDanhSachMH.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTableDanhSachMH.getColumnModel().getColumn(5).setPreferredWidth(5);
        JTableHeader header = jTableDanhSachMH.getTableHeader();
        header.setResizingAllowed(false);
        jTableDanhSachMH.setFocusable(false);
        jTableDanhSachMH.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableDanhSachMH.setRowMargin(3);
        jTableDanhSachMH.setRowSelectionAllowed(true);
        jTableDanhSachMH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableDanhSachMH.setSelectionForeground(new Color(204, 255, 255));
        int stt = 0;
        for (int i = 0; i < listMatHang.size(); i++) {
            RecordSanPham recordSanPham = listMatHang.get(i);
            SanPham sp = recordSanPham.getPham();
            int soLuong = recordSanPham.getSoLuong();
            String ma = sp.getMaMatHang();
            String maSp = sp.getMaSp();
            String ten = sp.getTenMatHang();
            String donVi = sp.getDonViTinh();
            int gia = sp.getGia();
            defaultTableModel.addRow(new Object[]{stt, maSp, ma, ten, donVi, soLuong, dinhDangTien(gia)});
            stt++;
        }

        defaultTableModel.fireTableDataChanged();
    }

    void loadCuaHang() {
        jComboBoxCuaHang.removeAllItems();
        CuaHangDAO cuaHangDAO = new CuaHangDAO();
        this.listCuaHang = cuaHangDAO.getAllCuaHang();
        cuaHangSelected = listCuaHang.get(0);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < listCuaHang.size(); i++) {
            model.addElement(listCuaHang.get(i).getTenCuaHang() + listCuaHang.get(i).getId());
        }
        jComboBoxCuaHang.setModel(model);
        jComboBoxCuaHang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox comboBoxTest = (JComboBox) e.getSource();
                int stt = comboBoxTest.getSelectedIndex();
                cuaHangSelected = listCuaHang.get(stt);
            }
        });
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
                loadDanhSachMH();
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
        jDateChooserNgayXuat = new com.toedter.calendar.JDateChooser();
        jTextField3 = new javax.swing.JTextField();
        jTextFieldMaBienLai = new javax.swing.JTextField();
        jComboBoxKho = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBoxNhanVien = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxCuaHang = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jButtonTimKiem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDanhSachMH = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTextFieldTimKiem = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerSoLuong = new javax.swing.JSpinner();
        JButtonThem = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTiLeThue = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldTiLeLai = new javax.swing.JTextField();
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
        jButtonXuatKho = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanelLoading = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(162, 174, 240));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jDateChooserNgayXuat.setDateFormatString("dd/MM/yyyy");

        jTextFieldMaBienLai.setEditable(false);

        jComboBoxKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kho Hà Nội", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Nhân viên xuất");

        jLabel1.setText("Ngày");

        jComboBoxNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NV Nguyễn Thị Loan", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Số phiếu");

        jLabel3.setText("Kho xuất");

        jLabel4.setText("Diễn giải");

        jLabel7.setText("Ghi chú");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel11.setText("Cửa Hàng");

        jComboBoxCuaHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3)
                    .addComponent(jTextField2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxCuaHang, 0, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooserNgayXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jDateChooserNgayXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldMaBienLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBoxNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBoxCuaHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxKho, jComboBoxNhanVien, jDateChooserNgayXuat, jLabel1, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jTextField2, jTextField3, jTextFieldMaBienLai});

        jButtonTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButtonTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/magnifying-glass16x16.png"))); // NOI18N
        jButtonTimKiem.setText("Tìm kiếm");
        jButtonTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTimKiemActionPerformed(evt);
            }
        });

        jTableDanhSachMH.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableDanhSachMH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "", "Mã hàng", "Tên hàng", "ĐVT", "Số lượng", "Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
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
        jTableDanhSachMH.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        JButtonThem.setText("Thêm");
        JButtonThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonThemActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setText("Tỉ lệ Thuế");

        jTextFieldTiLeThue.setText("0");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel10.setText("Tỉ Lệ Lãi");

        jTextFieldTiLeLai.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JButtonThem))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jButtonTimKiem)
                            .addGap(18, 18, 18)
                            .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTiLeThue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldTiLeLai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {JButtonThem, jButtonTimKiem, jSpinnerSoLuong, jTextFieldTimKiem});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(jSpinnerSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldTiLeThue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldTiLeLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonTimKiem)
                    .addComponent(jTextFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonTimKiem, jTextFieldTimKiem});

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_export_48px.png"))); // NOI18N
        jLabel12.setText("Phiếu xuất kho");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Mã hàng", "Tên mặt hàng", "ĐVT", "Số lượng", "Đơn giá", "Tỉ lệ thuế", "Tỉ lệ lãi", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false
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
            jTableMatHangThem.getColumnModel().getColumn(8).setResizable(false);
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

        jButtonXuatKho.setText("Xuất kho");
        jButtonXuatKho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXuatKhoActionPerformed(evt);
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
                                .addComponent(jButtonXuatKho)))
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

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton7, jButton8, jButtonXoaDong, jButtonXuatKho});

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
                    .addComponent(jButtonXuatKho)
                    .addComponent(jButton8))
                .addGap(34, 34, 34))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton7, jButton8, jButtonXoaDong, jButtonXuatKho});

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButton1.setText("Tồn kho");

        jLabel13.setText("Đang tải...");

        javax.swing.GroupLayout jPanelLoadingLayout = new javax.swing.GroupLayout(jPanelLoading);
        jPanelLoading.setLayout(jPanelLoadingLayout);
        jPanelLoadingLayout.setHorizontalGroup(
            jPanelLoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoadingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLoadingLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel13)
                        .addGap(40, 40, 40)))
                .addContainerGap())
        );
        jPanelLoadingLayout.setVerticalGroup(
            jPanelLoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoadingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(395, 395, 395)
                .addComponent(jPanelLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(106, 106, 106))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(406, 406, 406)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    void loadThemMatHangDaChon() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[]{"", "Mã mặt hàng", "Tên mặt hàng", "ĐVT", "Số lượng", "Đơn giá", "Tỉ lệ thuế", "Tỉ lệ lãi", "Thành tiền"}, 0);
        defaultTableModel.setRowCount(0);
        jTableMatHangThem.setModel(defaultTableModel);
        jTableMatHangThem.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(3).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(5).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(6).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(7).setPreferredWidth(5);
        jTableMatHangThem.getColumnModel().getColumn(8).setPreferredWidth(5);

        JTableHeader header = jTableMatHangThem.getTableHeader();
        header.setResizingAllowed(false);
        jTableMatHangThem.setFocusable(false);
        jTableMatHangThem.setDefaultRenderer(String.class, new VisitorRenderer());
        jTableMatHangThem.setRowMargin(3);
        jTableMatHangThem.setRowSelectionAllowed(true);
        jTableMatHangThem.setSelectionForeground(new Color(204, 255, 255));

        int stt = 0;
        for (int i = 0; i < listBienLaiXuat.size(); i++) {
            RecordBienLaiXuat recordBienLaiXuat = listBienLaiXuat.get(i);
            BienLaiXuat bienLaiXuat = recordBienLaiXuat.getBienLaiXuat();
            SanPham pham = recordBienLaiXuat.getSanPham();
            String maMH = pham.getMaMatHang();
            String tenMH = pham.getTenMatHang();
            int soLuong = bienLaiXuat.getSoLuong();
            int donGia = pham.getGia();
            int tiLeThue = bienLaiXuat.getTiLeThue();
            int tiLeLai = bienLaiXuat.getTiLeLai();
            String dvt = pham.getDonViTinh();
            int thanhTien = donGia * soLuong + (donGia * soLuong * tiLeLai / 100) - (donGia * soLuong * tiLeThue / 100);
            defaultTableModel.addRow(new Object[]{stt, maMH, tenMH, dvt, soLuong, dinhDangTien(donGia), tiLeThue + "%", tiLeLai + "%", dinhDangTien(thanhTien)});
            stt++;

        }
        defaultTableModel.fireTableDataChanged();

    }
// xử lý sự kiến ấn nút tìm mặt hàng
    private void jButtonTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTimKiemActionPerformed
        ArrayList<RecordSanPham> listMatHangTmp = new ArrayList<RecordSanPham>();
        String txtSearch = jTextFieldTimKiem.getText();
        if (txtSearch.equals("")) {
            loadDanhSachMH();
            listMatHangSearch.clear();
        } else {
            for (int i = 0; i < listMatHang.size(); i++) {
                RecordSanPham recordSanPham = listMatHang.get(i);
                SanPham pham = recordSanPham.getPham();
                int soLuong = recordSanPham.getSoLuong();
                if (pham.getTenMatHang() != null) {
                    if (pham.getTenMatHang().contains(txtSearch)) {
                        listMatHangTmp.add(recordSanPham);
                    }
                }

            }
            listMatHangSearch = listMatHangTmp;
            loadDanhSachMH(listMatHangTmp);
        }
    }//GEN-LAST:event_jButtonTimKiemActionPerformed

    private void JButtonThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonThemActionPerformed
        boolean check = true;
        int row = jTableDanhSachMH.getSelectedRow();
        int soLuong = (int) jSpinnerSoLuong.getValue();
        int tiLeThue = 0;
        int tiLeLai = 0;
        try {
            tiLeThue = Integer.parseInt(jTextFieldTiLeThue.getText());
            tiLeLai = Integer.parseInt(jTextFieldTiLeLai.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String ngayXuat = ((JTextField) jDateChooserNgayXuat.getDateEditor().getUiComponent()).getText();
        if (row == -1) {
            check = false;
            JOptionPane.showMessageDialog(null, "Chọn mặt hàng bạn muốn thêm!", "cảnh báo chọn mặt hàng", JOptionPane.WARNING_MESSAGE);

        } else if (soLuong <= 0) {
            check = false;
            JOptionPane.showMessageDialog(null, "Vui lòng chọn số lượng mặt hàng!", "cảnh báo chọn số lượng mặt hàng", JOptionPane.WARNING_MESSAGE);
        }
        SanPham spThem = new SanPham();
        Integer soLuongSP = new Integer(0);
        RecordSanPham recordSanPham = new RecordSanPham();
        if (listMatHangSearch.size() != 0) {
            recordSanPham = listMatHangSearch.get(row);
            spThem = recordSanPham.getPham();
            soLuongSP = recordSanPham.getSoLuong();
        } else {
            recordSanPham = listMatHang.get(row);
            spThem = recordSanPham.getPham();
            soLuongSP = recordSanPham.getSoLuong();
        }
        if (soLuongSP.intValue() < soLuong) {
            check = false;
            JOptionPane.showMessageDialog(null, "Số lượng mặt hàng này trong kho chỉ còn " + soLuongSP + "!", "cảnh báo chọn mặt hàng", JOptionPane.WARNING_MESSAGE);
        }
        if (check == true) {

            BienLaiXuat bienLaiXuat = new BienLaiXuat();
            bienLaiXuat.setMaBienLai(maBienLai);

            bienLaiXuat.setNv(nvSelected);
            bienLaiXuat.setCuaHang(cuaHangSelected);
            System.out.println("cuaHangSelected=" + cuaHangSelected.getTenCuaHang());
            bienLaiXuat.setKho(khoSelected);
            bienLaiXuat.setTiLeLai(tiLeLai);
            bienLaiXuat.setTiLeThue(tiLeThue);
            bienLaiXuat.setNgayLap(ngayXuat);
            bienLaiXuat.setKho(khoSelected);
            bienLaiXuat.setSoLuong(soLuong);
            bienLaiXuat.setTongCong(bienLaiXuat.getSoLuong() * spThem.getGia());
            RecordBienLaiXuat recordBienLaiXuat = new RecordBienLaiXuat();
            recordBienLaiXuat.setBienLaiXuat(bienLaiXuat);
            recordBienLaiXuat.setSanPham(spThem);
            this.listBienLaiXuat.add(recordBienLaiXuat);
            loadThemMatHangDaChon();
            if (listMatHangSearch.size() != 0) {
                recordSanPham.setSoLuong(soLuongSP - soLuong);
                listMatHangSearch.set(row, recordSanPham);
                loadDanhSachMH(listMatHangSearch);
            } else {
                recordSanPham.setSoLuong(soLuongSP - soLuong);
                listMatHang.set(row, recordSanPham);
                loadDanhSachMH(listMatHang);

            }
        }
    }//GEN-LAST:event_JButtonThemActionPerformed

    private void jButtonXoaDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaDongActionPerformed
        int row = jTableMatHangThem.getSelectedRow();
        listBienLaiXuat.remove(row);
        loadThemMatHangDaChon();
    }//GEN-LAST:event_jButtonXoaDongActionPerformed

    private void jButtonXuatKhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXuatKhoActionPerformed
        for (int i = 0; i < listBienLaiXuat.size(); i++) {
            RecordBienLaiXuat recordBienLaiXuat = listBienLaiXuat.get(i);
            BienLaiXuat bienLaiXuat = recordBienLaiXuat.getBienLaiXuat();
            SanPham sp = recordBienLaiXuat.getSanPham();
            BienLaiXuatDAO bienLaiXuatDAO = new BienLaiXuatDAO();
            bienLaiXuatDAO.themBienLaiXuat(bienLaiXuat, sp);
        }
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgCuaHangBanHoaQua/icons8_ok_48px.png"));
        JOptionPane.showMessageDialog(null, "Đã hoàn tất xuất hàng cho cửa hàng " + cuaHangSelected.getTenCuaHang(), "Nhập hàng thành công", JOptionPane.INFORMATION_MESSAGE, icon);
        listBienLaiXuat.clear();
        loadThemMatHangDaChon();
        loadDanhSachMH();
    }//GEN-LAST:event_jButtonXuatKhoActionPerformed

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
            java.util.logging.Logger.getLogger(GDXuatHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDXuatHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDXuatHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDXuatHangFrm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
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
                GDXuatHangFrm dNhapHangFrm = new GDXuatHangFrm();
                dNhapHangFrm.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonThem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButtonTimKiem;
    private javax.swing.JButton jButtonXoaDong;
    private javax.swing.JButton jButtonXuatKho;
    private javax.swing.JComboBox<String> jComboBoxCuaHang;
    private javax.swing.JComboBox<String> jComboBoxKho;
    private javax.swing.JComboBox<String> jComboBoxNhanVien;
    private com.toedter.calendar.JDateChooser jDateChooserNgayXuat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelLoading;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinnerSoLuong;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTableDanhSachMH;
    private javax.swing.JTable jTableMatHangThem;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextFieldMaBienLai;
    private javax.swing.JTextField jTextFieldTiLeLai;
    private javax.swing.JTextField jTextFieldTiLeThue;
    private javax.swing.JTextField jTextFieldTimKiem;
    // End of variables declaration//GEN-END:variables
}
