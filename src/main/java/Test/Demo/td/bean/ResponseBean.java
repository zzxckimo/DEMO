package Test.Demo.td.bean;

public class ResponseBean {

    private Time time;
    private String disclaimer;
    private String chartName;
    private Bpi bpi;
    public void setTime(Time time) {
         this.time = time;
     }
     public Time getTime() {
         return time;
     }

    public void setDisclaimer(String disclaimer) {
         this.disclaimer = disclaimer;
     }
     public String getDisclaimer() {
         return disclaimer;
     }

    public void setChartname(String chartname) {
         this.chartName = chartname;
     }
     public String getChartname() {
         return chartName;
     }

    public void setBpi(Bpi bpi) {
         this.bpi = bpi;
     }
     public Bpi getBpi() {
         return bpi;
     }

}