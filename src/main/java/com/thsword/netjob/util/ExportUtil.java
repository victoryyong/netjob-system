package com.thsword.netjob.util;

import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExportUtil {
	@SuppressWarnings("deprecation")
	public static void exportExcel(String sheetName, String[] keys,
			String[] headers, JSONArray array, OutputStream outStream) {
		try {
			// 声明一个工作薄
			HSSFWorkbook wb = new HSSFWorkbook();
			if (StringUtils.isEmpty(sheetName) || null == keys
					|| keys.length < 1 || null == headers || headers.length < 1
					|| null == array || array.size() < 1)
				return;
			HSSFSheet sheet = wb.createSheet(sheetName);
			// 给单子名称一个长度
			sheet.setDefaultColumnWidth((short) 15);
			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();
			// 样式字体居中
			HSSFRow row = null;
			HSSFCell cell = null;
			for (int j = 0; j < array.size() + 1; j++) {
				row = sheet.createRow(j);
				if (j == 0) {
					for (int k = 0; k < keys.length; k++) {
						cell = row.createCell(k);
						cell.setCellValue(headers[k]);
					}
				} else {
					JSONObject obj = array.getJSONObject(j - 1);
					for (int k = 0; k < keys.length; k++) {
						cell = row.createCell(k);
						cell.setCellValue(obj.getString(keys[k]));
					}
				}
			}
			wb.write(outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
