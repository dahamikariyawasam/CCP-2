package service;

public class ReportBuilder {
    private String reportType;
    private String startDate;
    private String endDate;

    public ReportBuilder setReportType(String reportType) {
        this.reportType = reportType;
        return this;
    }

    public ReportBuilder setDateRange(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public String build() {
        return "Report: " + reportType + " from " + startDate + " to " + endDate;
    }
}
