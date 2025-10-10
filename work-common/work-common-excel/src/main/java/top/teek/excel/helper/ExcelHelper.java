package top.teek.excel.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import top.teek.excel.convert.ExcelBigNumberConvert;
import top.teek.excel.dict.ExcelDictManager;
import top.teek.excel.dropdown.DropDownOptions;
import top.teek.excel.dropdown.ExcelDownHandler;
import top.teek.excel.handler.CellMergeStrategy;
import top.teek.excel.listen.BaseExcelListener;
import top.teek.excel.listen.ExcelListener;
import top.teek.excel.model.ExcelResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Teeker
 * @date 2024/6/10 17:17:29
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelHelper {

    /**
     * 同步导入（适用于小数据量）
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }

    /**
     * 使用校验监听器，异步导入，同步返回
     *
     * @param is         输入流
     * @param clazz      对象类型
     * @param isValidate 是否 Validator 检验 默认为是
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
        BaseExcelListener<T> listener = new BaseExcelListener<>(isValidate);
        return importExcel(is, clazz, listener);
    }

    /**
     * 使用自定义监听器，异步导入，自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 分页异步导入
     *
     * @param is         输入流
     * @param clazz      对象类型
     * @param batchCount 分页大小
     * @param consumer   数据每次到达分页阈值后，返回数据
     */
    public static <T> void importExcel(InputStream is, Class<T> clazz, Integer batchCount, Consumer<T> consumer) {
        importExcel(is, clazz, new PageReadListener(consumer, batchCount));
    }

    /**
     * 自定义原生监听器导入
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义原生监听器
     */
    public static <T> void importExcel(InputStream is, Class<T> clazz, ReadListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        ExcelDictManager.remove();
    }

    /**
     * 使用校验监听器，异步导入，同步返回
     *
     * @param filename   文件绝对路径
     * @param clazz      对象类型
     * @param isValidate 是否 Validator 检验 默认为是
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(String filename, Class<T> clazz, boolean isValidate) {
        BaseExcelListener<T> listener = new BaseExcelListener<>(isValidate);
        return importExcel(filename, clazz, listener);
    }

    /**
     * 使用自定义监听器，异步导入，自定义返回
     *
     * @param filename 文件绝对路径
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(String filename, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(filename, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 分页异步导入
     *
     * @param filename   文件绝对路径
     * @param clazz      对象类型
     * @param batchCount 分页大小
     * @param consumer   数据每次到达分页阈值后，返回数据
     */
    public static <T> void importExcel(String filename, Class<T> clazz, Integer batchCount, Consumer<T> consumer) {
        importExcel(filename, clazz, new PageReadListener(consumer, batchCount));
    }

    /**
     * 自定义原生监听器导入
     *
     * @param filename 文件绝对路径
     * @param clazz    对象类型
     * @param listener 自定义原生监听器
     */
    public static <T> void importExcel(String filename, Class<T> clazz, ReadListener<T> listener) {
        EasyExcel.read(filename, clazz, listener).sheet().doRead();
        ExcelDictManager.remove();
    }

    /**
     * 导出 Excel
     *
     * @param list       导出数据集合
     * @param sheetName  工作表的名称
     * @param headerInfo 头部信息
     * @param response   响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, List<List<String>> headerInfo, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            EasyExcel.write(response.getOutputStream())
                    .head(headerInfo)
                    .autoCloseStream(false)
                    // 自动适配
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 大数值自动转换，防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .sheet(sheetName)
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        }
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param response  响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(list, sheetName, clazz, false, os, null);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        }
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param response  响应体
     * @param options   级联下拉选
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response, List<DropDownOptions> options) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(list, sheetName, clazz, false, os, options);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param merge     是否合并单元格
     * @param response  响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(list, sheetName, clazz, merge, os, null);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param merge     是否合并单元格
     * @param response  响应体
     * @param options   级联下拉选
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, HttpServletResponse response, List<DropDownOptions> options) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(list, sheetName, clazz, merge, os, options);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param os        输出流
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, OutputStream os) {
        exportExcel(list, sheetName, clazz, false, os, null);
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param os        输出流
     * @param options   级联下拉选内容
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, OutputStream os, List<DropDownOptions> options) {
        exportExcel(list, sheetName, clazz, false, os, options);
    }

    /**
     * 导出 Excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param merge     是否合并单元格
     * @param os        输出流
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge,
                                       OutputStream os, List<DropDownOptions> options) {
        ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
                .autoCloseStream(false)
                // 自动适配
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                // 大数值自动转换，防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .sheet(sheetName);
        if (merge) {
            // 合并处理器
            builder.registerWriteHandler(new CellMergeStrategy(list, true));
        }
        // 添加下拉框操作
        builder.registerWriteHandler(new ExcelDownHandler(options));
        builder.doWrite(list);
        ExcelDictManager.remove();
    }

    /**
     * 纯 Excel 模板导出
     *
     * @param filename     文件路径，如果没有指定磁盘符，则默认读取项目 resource 目录下的文件
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param response     响应体
     */
    public static void exportTemplate(String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            EasyExcel.write(os)
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .sheet().doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplate(List<Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplate(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplate(List<Object> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        // 单表多数据导出，模板格式为 {.属性}
        for (Object d : data) {
            excelWriter.fill(d, writeSheet);
        }
        excelWriter.finish();
    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplateMultiList(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 多sheet模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplateMultiSheet(List<Map<String, Object>> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplateMultiSheet(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出 Excel 异常");
        } finally {
            ExcelDictManager.remove();
        }
    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        for (Map.Entry<String, Object> map : data.entrySet()) {
            // 设置列表后续还有数据
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            if (map.getValue() instanceof Collection) {
                // 多表导出必须使用 FillWrapper
                excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
            } else {
                excelWriter.fill(map.getValue(), writeSheet);
            }
        }
        excelWriter.finish();
    }

    /**
     * 多 sheet 模板导出 模板格式为 {key.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplateMultiSheet(List<Map<String, Object>> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        for (int i = 0; i < data.size(); i++) {
            WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
            for (Map.Entry<String, Object> map : data.get(i).entrySet()) {
                // 设置列表后续还有数据
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                if (map.getValue() instanceof Collection) {
                    // 多表导出必须使用 FillWrapper
                    excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
                } else {
                    excelWriter.fill(map.getValue(), writeSheet);
                }
            }
        }
        excelWriter.finish();
    }

    /**
     * 重置响应体
     */
    private static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        String encode = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        String percentEncodedFileName = encode.replaceAll("\\+", "%20");
        String contentDispositionValue = "attachment; filename=%s;filename*=utf-8''%s".formatted(percentEncodedFileName, percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        return filename + "_" + IdUtil.fastSimpleUUID() + ".xlsx";
    }

    /**
     * 解析导出值
     *
     * @param originValue 参数值，如 0
     * @param exp         表达式，如 0:男, 1:女, 2:未知
     * @param separator   分隔符
     * @return 解析后值
     */
    public static String parseValueByExp(String originValue, String exp, String mappingKey, String separator) {
        StringBuilder property = new StringBuilder();
        // 切割每个字典
        String[] convertSource = exp.split(separator);

        for (String convertItem : convertSource) {
            // 切割 key 和 value
            String[] convertItemArr = convertItem.trim().split(mappingKey);
            // 如果 originValue 存在多个值
            if (originValue.contains(separator)) {
                for (String value : originValue.split(separator)) {
                    if (convertItemArr[0].trim().equals(value)) {
                        property.append(convertItemArr[1].trim()).append(separator);
                        break;
                    }
                }
            } else {
                // 如果 originValue 只有一个值
                if (convertItemArr[0].trim().equals(originValue)) {
                    return convertItemArr[1].trim();
                }
            }
        }
        return StringUtils.stripEnd(property.toString(), separator);
    }

    /**
     * 解析导入值
     *
     * @param originValue 参数值，如 男
     * @param exp         表达式，如 0:男, 1:女, 2:未知
     * @param separator   分隔符
     * @return 解析后值
     */
    public static String reverseValueByExp(String originValue, String exp, String mappingKey, String separator) {
        StringBuilder property = new StringBuilder();
        // 切割每个字典
        String[] convertSource = exp.split(separator);

        for (String convertItem : convertSource) {
            // 切割 key 和 value
            String[] convertItemArr = convertItem.trim().split(mappingKey);
            // 如果 originValue 存在多个值
            if (originValue.contains(separator)) {
                for (String value : originValue.split(separator)) {
                    if (convertItemArr[1].trim().equals(value)) {
                        property.append(convertItemArr[0].trim()).append(separator);
                        break;
                    }
                }
            } else {
                // 如果 originValue 只有一个值
                if (convertItemArr[1].trim().equals(originValue)) {
                    return convertItemArr[0].trim();
                }
            }
        }
        return StringUtils.stripEnd(property.toString(), separator);
    }
}
