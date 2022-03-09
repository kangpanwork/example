package com.sanri.test.easypoi;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ReadExcel {

    @Test
    public void testImportExcel() throws Exception {
//        ClassPathResource classPathResource = new ClassPathResource("/data.xlsx");
        FileInputStream fileInputStream = new FileInputStream("d:/test/data.xlsx");
        ExcelImportResult<ImportPojo> importPojoExcelImportResult = importExcel(fileInputStream);
        List<ImportPojo> list = importPojoExcelImportResult.getList();
        System.out.println(list);
    }

    public ExcelImportResult<ImportPojo> importExcel(InputStream inputStream) throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        importParams.setNeedVerify(true);
        ExcelImportResult<ImportPojo> excelImportResult = ExcelImportUtil.importExcelMore(inputStream, ImportPojo.class, importParams);
        if (excelImportResult.isVerfiyFail()){
            ImportPojo importPojo = excelImportResult.getFailList().get(0);
            String errorMsg = importPojo.getErrorMsg();
            throw new IllegalArgumentException(errorMsg);
        }

        return excelImportResult;
    }

    @Data
    public static class ImportPojo implements IExcelModel, IExcelDataModel {
        @NotNull(message = "机构经纪人姓名不能为空")
        @Excel(name = "机构经纪人姓名", orderNum = "0", width = 30)
        private String userName;

        @NotNull(message = "机构经纪人电话号码不能为空")
        @Digits(message = "请输入正确的手机号码",integer = 11,fraction = 0)
        @Excel(name = "机构经纪人电话号码", orderNum = "1", width = 30)
        private String userPhone;

        @NotNull(message = "楼盘ID不能为空")
        @Excel(name = "楼盘ID", orderNum = "2", width = 30)
        private String buildId;

        private String errorMsg;

        private int rowNum;


        @Override
        public void setRowNum(int i) {
            this.rowNum = i;
        }
    }
}
