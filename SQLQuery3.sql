/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 1000 [idNhanVien]
      ,[vaiTro]
      ,[username]
      ,[password]
      ,[idBoPhan]
      ,[idCuaHang]
      ,[idNguoi]
      ,[rowguid]
  FROM [CuaHangHoaQua2].[dbo].[NhanVien]
  select * from  [NhanVien]  inner join [Nguoi] on NhanVien.idNhanVien=Nguoi.idNguoi where vaiTro=N'Bán hàng'