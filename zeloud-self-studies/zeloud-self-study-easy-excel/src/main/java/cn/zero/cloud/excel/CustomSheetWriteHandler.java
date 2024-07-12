package cn.zero.cloud.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author Xisun Wang
 * @since 7/11/2024 16:21
 */
public class CustomSheetWriteHandler extends AbstractSheetWriteHandler {
    private String imagePath;

    public CustomSheetWriteHandler(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        String path = this.getClass().getClassLoader().getResource("").getPath();

        Workbook workbook = writeWorkbookHolder.getWorkbook();

        SXSSFSheet sheet = (SXSSFSheet) writeSheetHolder.getSheet();

        for (int i = 0; i < 10; i++) {
            // 设置列宽（以1/256个字符宽度为单位）
            sheet.setColumnWidth(i, 256 * 20); // 将第i列的宽度设置为40个字符宽度
        }


        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        // 创建第一行
        Row pictureRow = sheet.createRow(0);
        pictureRow.setHeightInPoints(120); // 将行高设置为80个点

        // 合并单元格，参数分别为起始行、结束行、起始列、结束列
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 4));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 5, 9));

        // 保存图片
        try {
            // 读取图片并转换成byte数组
            InputStream inputStream = new FileInputStream(path + imagePath);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();

            FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(new File("C:\\Users\\xiswang\\XiSun\\aa.jpeg"));
            fileImageOutputStream.write(bytes, 0, bytes.length);
            fileImageOutputStream.close();

            // 将图片添加到工作簿中
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

            // 创建一个画布
            SXSSFDrawing drawing = sheet.createDrawingPatriarch();
            // 创建一个锚点，设置图片在合并后的单元格的位置
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(0); // 列开始
            anchor.setRow1(0); // 行开始
            anchor.setCol2(5); // 列结束
            anchor.setRow2(1); // 行结束

            // 将图片插入到合并的单元格中
            drawing.createPicture(anchor, pictureIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建第一行第二个合并的单元格
        Cell pictureRowCell = pictureRow.createCell(5);
        pictureRowCell.setCellValue("这是一张图片");
        pictureRowCell.setCellStyle(cellStyle);


        // 创建第二行
        Row titleRow = sheet.createRow(1);

        // 创建第一个单元格
        Cell cell1 = titleRow.createCell(0);
        cell1.setCellValue("图片");
        cell1.setCellStyle(cellStyle);
        Cell cell2 = titleRow.createCell(1);
        cell2.setCellValue("模型名称");
        cell2.setCellStyle(cellStyle);
        Cell cell3 = titleRow.createCell(2);
        cell3.setCellValue("模型类型");
        cell3.setCellStyle(cellStyle);
        Cell cell4 = titleRow.createCell(3);
        cell4.setCellValue("模型版本信息");
        cell4.setCellStyle(cellStyle);
        Cell cell5 = titleRow.createCell(4);
        cell5.setCellValue("数据文件名称");
        Cell cell6 = titleRow.createCell(5);
        cell6.setCellValue("测试时间");
        cell6.setCellStyle(cellStyle);
        Cell cell7 = titleRow.createCell(6);
        cell7.setCellValue("车型");
        cell7.setCellStyle(cellStyle);
        Cell cell8 = titleRow.createCell(7);
        cell8.setCellValue("AA");
        cell8.setCellStyle(cellStyle);
        Cell cell9 = titleRow.createCell(8);
        cell9.setCellValue("BB");
        cell9.setCellStyle(cellStyle);
        Cell cell10 = titleRow.createCell(9);
        cell10.setCellValue("CC");
        cell10.setCellStyle(cellStyle);
    }

    public static void main(String[] args) {

        // 使用自定义的SheetWriteHandler来写入Excel
        String fileName = "C:\\Users\\xiswang\\XiSun\\test.xlsx";
        String imagePath = "test.jpeg"; // 图片的路径
        EasyExcel.write(fileName)
                .registerWriteHandler(new CustomSheetWriteHandler(imagePath)) // 注册自定义的handler
                .sheet("ttt")
                .doWrite((Collection<?>) null); // 如果有数据模型可以传入，这里我们不需要写入任何数据
    }
}
