CREATE TABLE [CuaHangHoaQua].[dbo].[Nguoi] (
    idNguoi int PRIMARY KEY IDENTITY(1,1),
    email varchar(255),
    ngaySinh nvarchar(255),
    gioiTinh nvarchar(255),
    hocVan nvarchar(255),
    diaChi nvarchar(255),
	hoTen nvarchar (255) not null
);
drop table [CuaHangHoaQua].[dbo].[Nguoi];
create table [CuaHangHoaQua].[dbo].[CongTy](
	idCongTy int PRIMARY KEY IDENTITY(1,1),
	tenCongTy nvarchar (255) not null,
	diaChi nvarchar(255) not null
)
drop table CongTy;
create table [CuaHangHoaQua].[dbo].BoPhan (
	idBoPhan int PRIMARY KEY IDENTITY(1,1),
	tenBoPhan nvarchar(255) not null,
	idCongTy int not null,
	 FOREIGN KEY (idCongTy) references CongTy (idCongTy)
)
create table [CuaHangHoaQua].[dbo].ChiNhanh(
	idChiNhanh int Primary key identity(1,1),
	tenChiNhanh nvarchar (255) not null,
)
create table [CuaHangHoaQua].[dbo].CuaHang(
	idCuaHang int primary key identity(1,1),
	diaChi nvarchar(255) not null,
	tenCuaHang nvarchar (255) not null,
	idChiNhanh int not null,
	foreign key (idChiNhanh) references ChiNhanh (idChiNhanh)
)
CREATE TABLE [CuaHangHoaQua].[dbo].NhanVien (
    idNhanVien int PRIMARY KEY IDENTITY(1,1),
    vaiTro nvarchar(255) not null,
    username nvarchar(255) not null,
    password nvarchar(255) not null,
    idBoPhan int not null,
    idCuaHang int not null,
	idNguoi int not null,
	foreign key (idBoPhan) references BoPhan (idBoPhan),
	foreign key (idCuaHang) references CuaHang (idCuaHang),
	foreign key (idNguoi) references Nguoi (idNguoi),

);
CREATE TABLE [CuaHangHoaQua].[dbo].NhaCungCap (
    idNhaCungCap int PRIMARY KEY IDENTITY(1,1),
    ten nvarchar(255)  not null,
    email nvarchar(255),
    sodienthoai nvarchar(255) ,
);

CREATE TABLE [CuaHangHoaQua].[dbo].MatHang (
    idMatHang int PRIMARY KEY IDENTITY(1,1),
	maMatHang nvarchar(255)  not null,
	tenMatHang nvarchar (255) not null,
	moTa nvarchar (255),
	donVi nvarchar(255) not null
);

drop table [CuaHangHoaQua].[dbo].SanPham
CREATE TABLE [CuaHangHoaQua].[dbo].SanPham (
    idSanPham int PRIMARY KEY IDENTITY(1,1),
	maSp nvarchar(255) not null,
	gia int not null,
	hanSuDung nvarchar(255) not null,
	idMatHang int not null,
	idBienLaiKho int ,
	foreign key (idMatHang) references MatHang (idMatHang),
	foreign key (idBienLaiKho) references BienLaiKho(idBienLaiKho)
);

CREATE TABLE [CuaHangHoaQua].[dbo].BienLaiKho (
    idBienLaiKho int PRIMARY KEY IDENTITY(1,1),
	maBienLaiKho nvarchar (255) not null,
    ngayLap nvarchar(255)  not null,
	idKho  int not null,
    soLuong int not null,
	tongCong bigint not null,
	foreign key (idKho) references Kho(idKho)
);
drop table [CuaHangHoaQua].[dbo].BienLaiXuat
CREATE TABLE [CuaHangHoaQua].[dbo].BienLaiXuat (
    idBienLaiXuat int PRIMARY KEY IDENTITY(1,1),
	tiLeThue int not null,
	tiLeLai int not null,
	idBienLaiKho int not null,
	idCuaHang int not null,
	idNhanVien int not null,
	foreign key (idBienLaiKho) references BienLaiKho(idBienLaiKho),
	foreign key (idNhanVien) references NhanVien(idNhanVien),
	foreign key (idCuaHang) references CuaHang(idCuaHang)
);
CREATE TABLE [CuaHangHoaQua].[dbo].Kho (
    idKho int PRIMARY KEY IDENTITY(1,1),
    idNhanVien int not null,
	diaChi nvarchar (255),
	foreign key (idNhanVien) references NhanVien(idNhanVien),
);
CREATE TABLE [CuaHangHoaQua].[dbo].HopDong (
    idHopDong int PRIMARY KEY IDENTITY(1,1),
    tenHopDong nvarchar(255) not null,
    ngayKi nvarchar (255),
	denNgay nvarchar (255) ,
	idNhanVien int not null,
	idNhaCungCap int not null,
	foreign key (idNhanVien) references NhanVien(idNhanVien),
	foreign key (idNhaCungCap) references NhaCungCap(idNhaCungCap),
);
CREATE TABLE [CuaHangHoaQua].[dbo].PhieuThuChi (
    idPhieuThuChi int PRIMARY KEY IDENTITY(1,1),
    loaiPhieu nvarchar (255) not null,/*phieu thu hay phieu chi*/
    soPhieu nvarchar(255) not null ,/*PC001*/
	ngayLap nvarchar (255) not null,
	tenDoiTuong nvarchar(255) not null,
	lyDo nvarchar(255) not null,
	dienGiai nvarchar(255),
	idNhanVien int not null,
	soTien int not null,
	chuyenKhoan nvarchar (255),
	/*nhan vien nhap kho khac nhan vien ki hop dong*/
	foreign key (idNhanVien) references NhanVien(idNhanVien)
);
drop table [CuaHangHoaQua].[dbo].BienLaiNhap

CREATE TABLE [CuaHangHoaQua].[dbo].BienLaiNhap (
    idBienLaiNhap int PRIMARY KEY IDENTITY(1,1),
    idBienLaiKho int not null,
    idHopDong int ,
    idPhieuThuChi int ,
	idNhanVien int not null,/*nhan vien nhap kho khac nhan vien ki hop dong*/
	foreign key (idPhieuThuChi) references PhieuThuChi(idPhieuThuChi),
	foreign key (idBienLaiKho) references BienLaiKho(idBienLaiKho),
	foreign key (idHopDong) references HopDong(idHopDong),
	foreign key (idNhanVien) references NhanVien(idNhanVien)
);
drop table [CuaHangHoaQua].[dbo].CongNo
CREATE TABLE [CuaHangHoaQua].[dbo].CongNo (
    idCongNo int PRIMARY KEY IDENTITY(1,1),
	maSoThue nvarchar (255) not null,
	soTienNo bigint not null,
	idNhaCungCap int not null,
	idPhieuThuChi int ,
	foreign key (idNhaCungCap) references NhaCungCap(idNhaCungCap),
	foreign key (idPhieuThuChi) references  PhieuThuChi(idPhieuThuChi),

);
CREATE TABLE [CuaHangHoaQua].[dbo].CaLam (
    idCaLam int PRIMARY KEY IDENTITY(1,1),
	tenCa nvarchar (255) not null,
	ngayBatDau nvarchar (255) not null,
	ngayKetThuc nvarchar (255) not null,
);
CREATE TABLE [CuaHangHoaQua].[dbo].LichLam (
    idLichLam int PRIMARY KEY IDENTITY(1,1),
	ngayLap nvarchar(255) not null,
	idCaLam int not null,
	idNhanVien int not null,
	foreign key (idCaLam) references CaLam(idCaLam),
	foreign key (idNhanVien) references  NhanVien(idNhanVien)
);
CREATE TABLE [CuaHangHoaQua].[dbo].ChamCong (
    idBangChamCong int PRIMARY KEY IDENTITY(1,1),
	idLichLam int not null,
	idPhieuThuChi int not null,
	foreign key (idLichLam) references LichLam(idLichLam),
	foreign key (idPhieuThuChi) references  PhieuThuChi(idPhieuThuChi)
);
drop table [CuaHangHoaQua].[dbo].HoaDonBanHang
CREATE TABLE [CuaHangHoaQua].[dbo].HoaDonBanHang (
    idHoaDonBanHang int PRIMARY KEY IDENTITY(1,1),
	soLuong bigint not null,
	ngay nvarchar(255) not null,
	idSanPham int not null,
	soTien bigint not null,
	idNhanVien int not null,
	foreign key (idSanPham) references SanPham(idSanPham),
	foreign key (idNhanVien) references NhanVien(idNhanVien)
);
CREATE TABLE [CuaHangHoaQua].[dbo].SPDB (
    idSPDB int PRIMARY KEY IDENTITY(1,1),
	idHoaDonBanHang int not null,
	idSanPham int not null,
	soLuong int not null,
	foreign key (idSanPham) references SanPham(idSanPham),
	foreign key (idHoaDonBanHang) references HoaDonBanHang(idHoaDonBanHang)
);
/*---------------------Xoa Tat Ca-------------------------*/
drop table [CuaHangHoaQua].[dbo].CongNo
drop table [CuaHangHoaQua].[dbo].BienLaiNhap
drop table [CuaHangHoaQua].[dbo].BienLaiXuat
drop table [CuaHangHoaQua].[dbo].SanPham
drop table [CuaHangHoaQua].[dbo].BienLaiKho
drop table [CuaHangHoaQua].[dbo].MatHang
drop table [CuaHangHoaQua].[dbo].HopDong
drop table [CuaHangHoaQua].[dbo].NhanVien
drop table [CuaHangHoaQua].[dbo].BoPhan
drop table [CuaHangHoaQua].[dbo].CongTy
drop table [CuaHangHoaQua].[dbo].CuaHang
drop table [CuaHangHoaQua].[dbo].ChiNhanh
drop table [CuaHangHoaQua].[dbo].NhaCungCap
drop table [CuaHangHoaQua].[dbo].Kho
drop table [CuaHangHoaQua].[dbo].Nguoi
drop table [CuaHangHoaQua].[dbo].PhieuThuChi
drop table [CuaHangHoaQua].[dbo].SPDB

/*-------------------insert--------------------------------*/
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Jorge Biaggetti','jbiaggetti0@constantcontact.com','202-297-3749')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Zebadiah MacArthur','zmacarthur1@uiuc.edu','193-952-8139')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Paulo Boards','pboards2@123-reg.co.uk','475-442-1647')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Franky Sperring','fsperring3@state.gov','539-976-2743')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Maritsa Chidler','mchidler4@census.gov','379-848-2393')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Paulo Boards','pboards2@123-reg.co.uk','475-442-1647')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Charity Paull','cpaull5@si.edu','541-592-9970')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Frederic Fitchew','ffitchew6@ezinearticles.com','898-340-3656')
insert into [CuaHangHoaQua].[dbo].NhaCungCap (ten,email,sodienthoai) values ('Sheila-kathryn Silk','ssilk8@nhs.uk','525-636-7166')

/*----------------Congty-------------*/
insert into [CuaHangHoaQua].[dbo].Congty (tenCongTy, diachi) values ('Công Ty BFF','Số 14/16, Đường 990, Khu Phố 4, Phường Phú Hữu, Quận 9, Tp. Hồ Chí Minh (TPHCM), Việt Nam')
/*----------------BoPhan-------------*/
delete from [CuaHangHoaQua].[dbo].BoPhan 
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].BoPhan ON;  
insert into [CuaHangHoaQua].[dbo].BoPhan (idBoPhan,tenBoPhan,idCongTy) values (1,N'Tài chính – kế toán',1);
insert into [CuaHangHoaQua].[dbo].BoPhan (idBoPhan,tenBoPhan,idCongTy) values (2,N'Hành chính – nhân sự​',1);
insert into [CuaHangHoaQua].[dbo].BoPhan (idBoPhan,tenBoPhan,idCongTy) values (3,N'Mua hàng (cung ứng) – xuất nhập khẩu',1);
insert into [CuaHangHoaQua].[dbo].BoPhan (idBoPhan,tenBoPhan,idCongTy) values (4,N'Kế hoạch - kinh doanh',1);
insert into [CuaHangHoaQua].[dbo].BoPhan (idBoPhan,tenBoPhan,idCongTy) values (5,N'Quản lý chất lượng - QA/QC ',1);
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].BoPhan OFF;  
/*---------------ChiNhanh-----------*/
insert into [CuaHangHoaQua].[dbo].ChiNhanh (tenChiNhanh) values ('Nha Trang');
insert into [CuaHangHoaQua].[dbo].ChiNhanh (tenChiNhanh) values ('Ha Noi');
insert into [CuaHangHoaQua].[dbo].ChiNhanh (tenChiNhanh) values ('TP Ho Chi Minh');
/*--------------CuaHang-------------*/
delete from [CuaHangHoaQua].[dbo].CuaHang
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].CuaHang ON;  

insert into [CuaHangHoaQua].[dbo].CuaHang (idCuaHang,diaChi,tenCuaHang,idChiNhanh) values(1,N' Số 14/16, Đường 990, Khu Phố 4, Phường Phú Hữu, Quận 9, Tp. Hồ Chí Minh (TPHCM), Việt Nam','Cua Hang FFP',3);
insert into [CuaHangHoaQua].[dbo].CuaHang (idCuaHang,diaChi,tenCuaHang,idChiNhanh) values(2,N' Tầng 3, TCT 36 Bộ Quốc Phòng, Đường Giải Phóng, Quận Hoàng Mai, Hà Nội, Việt Nam','Cua Hang FFP',2);
insert into [CuaHangHoaQua].[dbo].CuaHang (idCuaHang,diaChi,tenCuaHang,idChiNhanh) values(3,N' 08 Đường A1, Vĩnh Điềm Trung, Thành phố Nha Trang, Khánh Hòa 57000','Cua Hang FFP',1);
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].CuaHang OFF; 
/*--------------Nguoi---------------*/
delete from [CuaHangHoaQua].[dbo].Nguoi
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].Nguoi ON;  

sp_adjustpublisheridentityrange @table_name = '[CuaHangHoaQua].[dbo].Nguoi'

insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(1,N'aengel0@imdb.com','20/10/1957','Female','Universitas Trisakti',N'L17-11 Tầng 17 Tòa nhà Vincom Center số 72 Lê Thánh Tôn, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh','Ailey Engel');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(2,N'tliptrot1@constantcontact.com','28/05/1937','Female','	Universiti Malaya',N'704/16 Đường Hương Lộ 2, Phường Bình Trị Đông A, Quận Bình Tân, Thành phố Hồ Chí Minh','Trenna Liptrot');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(3,N'fmcmonies2@chron.com','04/05/1950','Male','	Universiti Malaya',N'Tầng 12A, tòa nhà VTC Online, số 18 đường Tam Trinh, Phường Minh Khai, Quận Hai Bà Trưng, Thành phố Hà Nội','Francisco McMonies');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(4,N'ljeffray3@qq.com','26/07/1940','Female','	Universiti Malaya',N'Số 30 ngõ 137 Hồ Đắc Di, Phường Nam Đồng, Quận Đống Đa, Thành phố Hà Nội','Laraine Jeffray');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(5,N'ekimpton4@statcounter.com','14/03/1983','Female','	Fachhochschule Neu-Ulm',N'Lê Hồng Phong, Phường Phước Hải, Thành phố Nha Trang, Tỉnh Khánh Hòa','Ethelyn Kimpton');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(6,N'awillcocks5@state.tx.us','17/06/1985','Female','Persian Gulf University',N' Tòa nhà VCN TOWER Phước Hải - Số 2 Tố Hữu, Phường Phước Hải, Thành phố Nha Trang, Tỉnh Khánh Hòa','Laraine Jeffray');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(7,N'msaile0@timesonline.co.uk','16/04/1948','Male','University of Windsor',N'Nha Trang','Hally Faldoe');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(8,N'hfaldoe1@behance.net','16/04/1948','Male','University of Windsor',N'Ha Noi','Delbert Fawssett');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(9,N'adiperaus2@mapquest.com','16/04/1948','Male','University of Windsor',N'TP Ha Noi','Delbert Fawssett');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(10,N'glipscomb3@wufoo.com','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Alwin Le Gassick');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(11,N'gchastel4@redcross.org','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Meggi Le Sarr');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(12,N'mrivalant5@npr.org','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Gradey Lipscomb');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(13,N'pabdy6@globo.com','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Marsha Rivalant');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(14,N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Peadar Abdy');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(14,N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Nguyễn Thị Thoa');
insert into [CuaHangHoaQua].[dbo].Nguoi (idNguoi,email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(1000,N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ho Chi MInh','Nguyễn Thị Hiền');
insert into [CuaHangHoaQua].[dbo].Nguoi (email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Nha Trang',N'Nguyễn Thị Thu');
insert into [CuaHangHoaQua].[dbo].Nguoi (email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Nha Trang',N'Nguyễn Thị Thu');
insert into [CuaHangHoaQua].[dbo].Nguoi (email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ha Noi',N'Nguyễn Thị Anh');
insert into [CuaHangHoaQua].[dbo].Nguoi (email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ha Noi',N'Nguyễn Thị Tanh');
insert into [CuaHangHoaQua].[dbo].Nguoi (email,ngaySinh,gioiTinh,hocVan,diaChi,hoTen) values(N'mle7@sourceforge.net','16/04/1948','Male','University of Windsor',N'TP Ha Noi',N'Nguyễn Thị Chanh');
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].Nguoi OFF;  
/*-------------NhanVien-------------*/
delete from [CuaHangHoaQua].[dbo].NhanVien
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].NhanVien OFF;  
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(1,N'Kế toán',N'visaotrongtimanh_1998@yahoo.com','10khongco',1,1,1);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(2,N'Quản lí kho',N'visaotrongtimanh_1998@yahoo.com','10khongco',3,1,2);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(3,N'Quản lí kho',N'visaotrongtimanh_1998@yahoo.com','10khongco',3,1,3);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(4,N'Quản lí kho',N'adiperaus2@mapquest.com','10khongco',3,1,4);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(5,N'Quản lí kho',N'glipscomb3@wufoo.com','10khongco',3,1,5);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(6,N'Quản lí kho',N'gchastel4@redcross.org','10khongco',3,1,6);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(7,N'Quản lí kho',N'mrivalant5@npr.org','10khongco',3,1,7);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(8,N'Quản lí kho',N'pabdy6@globo.com','10khongco',3,1,8);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(9,N'Quản lí kho',N'mle7@sourceforge.net','10khongco',3,1,9);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(10,N'Quản lí kho',N'ale8@cyberchimps.com','10khongco',3,1,10);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(11,N'Kế toán',N'nkeling9@cam.ac.uk','10khongco',1,1,11);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(12,N'Kế toán',N'thaslewooda@technorati.com','10khongco',1,1,12);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(13,N'Kế toán',N'whirjakb@jimdo.com','10khongco',1,1,13);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(14,N'Kế toán',N'jdabnorc@hatena.ne.jp','10khongco',1,1,14);
insert into [CuaHangHoaQua].[dbo].NhanVien (idNhanVien,vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(15,N'Kế toán',N'visaotrongtimanh_1998@yahoo.com','10khongco',1,1,15);
insert into [CuaHangHoaQua].[dbo].NhanVien (vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(N'Quản lí kho',N'pabdy6@globo.com','10khongco',3,3,25);
insert into [CuaHangHoaQua].[dbo].NhanVien (vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(N'Quản lí kho',N'mle7@sourceforge.net','10khongco',3,3,26);
insert into [CuaHangHoaQua].[dbo].NhanVien (vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(N'Quản lí kho',N'pabdy6@globo.com','10khongco',3,2,27);
insert into [CuaHangHoaQua].[dbo].NhanVien (vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(N'Quản lí kho',N'mle7@sourceforge.net','10khongco',3,2,28);
insert into [CuaHangHoaQua].[dbo].NhanVien (vaiTro,username,[password],idBoPhan,idCuaHang,idNguoi) values(N'Bán Hàng',N'mle7@sourceforge.net','10khongco',4,2,105025);
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].NhanVien OFF; 
/*------------------KHO-----------------*/
delete from [CuaHangHoaQua].[dbo].Kho 
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].Kho ON;  
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (1,1,N'TP Hồ Chi Minh');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (2,3,N'TP Hà Nội');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (3,7,N'TP Nha Trang');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (104003,25,N'TP Nha Trang');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (104004,26,N'TP Nha Trang');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (104005,27,N'TP Ha Noi');
insert into [CuaHangHoaQua].[dbo].Kho (idKho,idNhanVien,diaChi) values (104006,28,N'TP Ha Noi');
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].Kho OFF; 
/*------------------Select-------------*/
select * from [CuaHangHoaQua].[dbo].Kho inner join [CuaHangHoaQua].[dbo].NhanVien ON NhanVien.idNhanVien=Kho.idKho
/*------------------HOPDONG-----------------*/
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].HopDong ON;  
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (1,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,1);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (2,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,2);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (3,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,3);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (4,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,4);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (5,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,5);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (6,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,6);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (7,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,7);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (8,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,8);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (9,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,9);
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (10,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,10);
<<<<<<< HEAD
insert into [CuaHangHoaQua].[dbo].HopDong (tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',18,344012);
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].HopDong OFF;  
=======
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (11,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,11);
SET IDENTITY_INSERT [CuaHangHoaQua].[dbo].HopDong OFF; 

 
>>>>>>> origin/master
DECLARE @TransactionName varchar(20) = 'Transaction1'; 
BEGIN TRAN @TransactionName  
insert into [CuaHangHoaQua].[dbo].HopDong (idHopDong,tenHopDong,ngayKi,denNgay,idNhanVien,idNhaCungCap) values (12,N'Hợp đồng hợp tác đầu tư',N'12/06/2020',N'vô thời hạn',2,9);
rollback Tran @TransactionName;
/*--------------truy van xuat kho-------------*/
  select  sp.maSp,mh.tenMatHang,mh.maMatHang, sp.gia,sp.hanSuDung,sp.idMatHang,blk.idBienLaiKho,blk.soLuong,mh.donVi from [CuaHangHoaQua].[dbo].[SanPham] sp inner join   [CuaHangHoaQua].[dbo].[BienLaiKho] blk on sp.idBienLaiKho=blk.idBienLaiKho 
  inner join  [CuaHangHoaQua].[dbo].[BienLaiNhap] bln on sp.idBienLaiKho=bln.idBienLaiKho inner join [CuaHangHoaQua].[dbo].[MatHang] mh on sp.idMatHang=mh.idMatHang  where idKho=1;
  
  select * from  [CuaHangHoaQua].[dbo].[BienLaiKho] where idBienLaiKho=159;
  select bln.idBienLaiNhap,bln.idBienLaiKho, bln.idHopDong, bln.idNhanVien from [CuaHangHoaQua].[dbo].[BienLaiNhap] bln inner join [CuaHangHoaQua].[dbo].[BienLaiKho] blk on bln.idBienLaiKho=blk.idBienLaiKho inner join [CuaHangHoaQua].[dbo].[SanPham] sp on sp.idBienLaiKho=bln.idBienLaiKho where idSanPham=13; 
  delete from [CuaHangHoaQua].[dbo].[SanPham]

  update [CuaHangHoaQua].[dbo].[BienLaiKho] set soLuong= soLuong-4 where idBienLaiKho=170
  update blk set blk.soLuong=spNhap.soLuong-spXuat.soLuong   from  ( select blk.idBienLaiKho,blk.maBienLaiKho, sp.maSp,sp.gia,blk.soLuong from [CuaHangHoaQua].[dbo].[SanPham]  sp inner join [CuaHangHoaQua].[dbo].[BienLaiNhap]  bln on sp.idBienLaiKho = bln.idBienLaiKho inner join  [CuaHangHoaQua].[dbo].[BienLaiKho] blk on sp.idBienLaiKho=blk.idBienLaiKho  ) spNhap inner join
  (select blk.idBienLaiKho, blk.maBienLaiKho,sp.maSp,sp.gia,blk.soLuong  from [CuaHangHoaQua].[dbo].[SanPham]  sp inner join [CuaHangHoaQua].[dbo].[BienLaiXuat]  blx on sp.idBienLaiKho = blx.idBienLaiKho inner join  [CuaHangHoaQua].[dbo].[BienLaiKho] blk on sp.idBienLaiKho=blk.idBienLaiKho ) spXuat on spNhap.maSp=spXuat.maSp inner join [CuaHangHoaQua].[dbo].[BienLaiKho] blk on blk.idBienLaiKho= spNhap.idBienLaiKho
	
  use distribution;
select subscriber_db, publication, article, subscriber, ranges_allocated, range_begin,range_end,next_range_begin,next_range_end,max_used
from MSmerge_identity_range_allocations
where article = 'dbo.Nguoi' and is_pub_range = 0
order by subscriber_db;



sp_adjustpublisheridentityrange @table_name = 'Nguoi',
sp_adjustpublisheridentityrange @table_name = 'ChiNhanh'
sp_adjustpublisheridentityrange @table_name = 'BienLaiKho'
sp_adjustpublisheridentityrange @table_name = 'BienLaiNhap'
sp_adjustpublisheridentityrange @table_name = 'BienLaiXuat'
sp_adjustpublisheridentityrange @table_name = 'BoPhan'
sp_adjustpublisheridentityrange @table_name = 'CaLam'
sp_adjustpublisheridentityrange @table_name = 'ChamCong'
sp_adjustpublisheridentityrange @table_name = 'ChiNhanh'
sp_adjustpublisheridentityrange @table_name = 'CongNo'
sp_adjustpublisheridentityrange @table_name = 'CongTy'
sp_adjustpublisheridentityrange @table_name = 'CuaHang'
SP_HELPCONSTRAINT 'Nguoi'
sp_adjustpublisheridentityrange @table_name = 'HoaDonBanHang'
sp_adjustpublisheridentityrange @table_name = 'HopDong'
sp_adjustpublisheridentityrange @table_name = 'Kho'

  use distribution;
select subscriber_db, publication, article, subscriber, ranges_allocated, range_begin,range_end,next_range_begin,next_range_end,max_used
from MSmerge_identity_range_allocations
where article = 'Nguoi' and is_pub_range = 0
order by subscriber_db;
sp_adjustpublisheridentityrange @table_name = 'LichLam'
sp_adjustpublisheridentityrange @table_name = 'MatHang'
sp_adjustpublisheridentityrange @table_name = 'Nguoi'
sp_adjustpublisheridentityrange @table_name = 'NhaCungCap'
sp_adjustpublisheridentityrange @table_name = 'NhanVien'
sp_adjustpublisheridentityrange @table_name = 'PhieuThuChi'
sp_adjustpublisheridentityrange @table_name = 'SanPham'

