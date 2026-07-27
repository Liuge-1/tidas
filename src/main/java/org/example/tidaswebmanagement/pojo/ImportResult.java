package org.example.tidaswebmanagement.pojo;

import java.util.ArrayList;
import java.util.List;

/** Excel 导入结果汇总 */
public class ImportResult {
    private int totalRows;
    private int successRows;
    private int failRows;
    private List<String> errors = new ArrayList<>();

    public void addError(int row, String msg) {
        errors.add("第" + row + "行：" + msg);
    }

    public boolean hasErrors() { return !errors.isEmpty(); }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }
    public int getSuccessRows() { return successRows; }
    public void setSuccessRows(int successRows) { this.successRows = successRows; }
    public int getFailRows() { return failRows; }
    public void setFailRows(int failRows) { this.failRows = failRows; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
