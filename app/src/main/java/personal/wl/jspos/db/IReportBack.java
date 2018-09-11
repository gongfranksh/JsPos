package personal.wl.jspos.db;

public interface IReportBack {
    public void reportBack(String tag, String message);
    public void reportTransient(String tag, String message);
}
