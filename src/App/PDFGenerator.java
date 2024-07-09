/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package App;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import entity.ChiTietHoaDon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class PDFGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static void createPDF(String dest, String customerName, String maHoaDon, String thoiGianRa, String thoiGianVao,
                                 String thoiGianSuDung, String tienDichVu, String tongTien, String donGia, String tienSuDungPhong,
                                 List<ChiTietHoaDon> dsCTHD) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);

            // Kích thước giấy K80 (80mm x chiều dài tùy chỉnh, ở đây sử dụng 297mm cho ví dụ)
            Rectangle k80 = new Rectangle(80 * 2.83465f, 297 * 2.83465f); // 80mm * 297mm
            Document document = new Document(pdf, new PageSize(k80));

            // Thêm hình ảnh logo
            try {
                String workingDir = System.getProperty("user.dir");
                File imgFile = new File(workingDir + "/src/image/studio.png"); // Đường dẫn tuyệt đối đến file logo
                if (imgFile.exists()) {
                    ImageData data = ImageDataFactory.create(Files.readAllBytes(imgFile.toPath()));
                    Image img = new Image(data);
                    img.setAutoScale(true); // Tự động thay đổi kích thước để phù hợp với chiều rộng
                    img.setWidth(80); // Chiều rộng logo đề xuất (pixel)
                    img.setHeight(80); // Chiều cao logo đề xuất (pixel)
                    img.setMarginBottom(10);
                    document.add(img);
                    logger.info("Logo image added successfully.");
                } else {
                    logger.error("Logo image not found.");
                }
            } catch (Exception e) {
                logger.error("Error loading logo image", e);
            }

            // Thêm tiêu đề
            Paragraph title = new Paragraph("INVOICE")
                    .setBold()
                    .setFontSize(16)
                    .setMarginBottom(10)
                    .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);
            document.add(title);

            // Thêm đường kẻ
            LineSeparator ls = new LineSeparator(new SolidLine());
            ls.setStrokeWidth(1);
            document.add(ls);

            // Thêm các đoạn văn bản
            document.add(new Paragraph("Customer Name: " + customerName).setFontSize(10));
            document.add(new Paragraph("Invoice ID: " + maHoaDon).setFontSize(10));
            document.add(new Paragraph("Check-in Time: " + thoiGianVao).setFontSize(10));
            document.add(new Paragraph("Check-out Time: " + thoiGianRa).setFontSize(10));
            document.add(new Paragraph("Duration: " + thoiGianSuDung).setFontSize(10));
            document.add(new Paragraph("Room Rate: " + donGia).setFontSize(10));
            document.add(new Paragraph("Room Charge: " + tienSuDungPhong).setFontSize(10));
            document.add(new Paragraph("Service Charge: " + tienDichVu).setFontSize(10));
            document.add(new Paragraph("Total: " + tongTien).setFontSize(10));

            // Thêm đường kẻ
            document.add(new Paragraph("\n"));
            document.add(ls);

            // Thêm bảng với chi tiết dịch vụ
            float[] columnWidths = {1, 5, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Thêm các tiêu đề cột vào bảng
            table.addHeaderCell(new Cell().add(new Paragraph("No.").setBold().setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Service").setBold().setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Qty").setBold().setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold().setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

            // Thêm các hàng dữ liệu vào bảng
            int index = 1;
            for (ChiTietHoaDon cthd : dsCTHD) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(index++)).setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
                table.addCell(new Cell().add(new Paragraph(cthd.getSanPham().getTenSanPham()).setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(cthd.getSoLuong())).setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(cthd.getSanPham().getDonGia())).setFontSize(10)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
            }

            document.add(table);

            // Thêm đường kẻ
            document.add(new Paragraph("\n"));
            document.add(ls);

            // Thêm chữ ký
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Staff Signature").setFontSize(10).setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT));
            document.add(new Paragraph("_____________________").setFontSize(10).setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT));

            document.close();
            logger.info("PDF created successfully!");
        } catch (Exception e) {
            logger.error("Error creating PDF", e);
        }
    }
}