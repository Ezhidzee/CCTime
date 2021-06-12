package logic;

public class MainModel {
    Integer dayIV, nightIV, eveningIV, morningIV;
    String dayTv, nightTv, eveningTv, morningTv,
            dayPOP, nightPOP, eveningPOP, morningPOP,
            dayTempTv, nightTempTv, eveningTempTv, morningTempTv,
            dayDescTv, nightDescTv, eveningDescTv, morningDescTv,
            dayConDescTv, nightConDescTv, eveningConDescTv, morningConDescTv, date;

    public MainModel(String date, Integer dayIV, Integer nightIV, Integer eveningIV, Integer morningIV,
                     String dayTv, String nightTv, String eveningTv, String morningTv,
                     String dayPOP, String nightPOP, String eveningPOP, String morningPOP,
                     String dayTempTv, String nightTempTv, String eveningTempTv, String morningTempTv,
                     String dayDescTv, String nightDescTv, String eveningDescTv, String morningDescTv,
                     String dayConDescTv, String nightConDescTv, String eveningConDescTv, String morningConDescTv) {
        this.date = date;
        this.dayIV = dayIV;
        this.nightIV = nightIV;
        this.eveningIV = eveningIV;
        this.morningIV = morningIV;
        this.dayTv = dayTv;
        this.nightTv = nightTv;
        this.eveningTv = eveningTv;
        this.morningTv = morningTv;
        this.dayPOP = dayPOP;
        this.nightPOP = nightPOP;
        this.eveningPOP = eveningPOP;
        this.morningPOP = morningPOP;
        this.dayTempTv = dayTempTv;
        this.nightTempTv = nightTempTv;
        this.eveningTempTv = eveningTempTv;
        this.morningTempTv = morningTempTv;
        this.dayDescTv = dayDescTv;
        this.nightDescTv = nightDescTv;
        this.eveningDescTv = eveningDescTv;
        this.morningDescTv = morningDescTv;
        this.dayConDescTv = dayConDescTv;
        this.nightConDescTv = nightConDescTv;
        this.eveningConDescTv = eveningConDescTv;
        this.morningConDescTv = morningConDescTv;
    }

    public Integer getDayIV() {
        return dayIV;
    }

    public Integer getNightIV() {
        return nightIV;
    }

    public Integer getEveningIV() {
        return eveningIV;
    }

    public Integer getMorningIV() {
        return morningIV;
    }

    public String getDate() {
        return date;
    }

    public String getDayTv() {
        return dayTv;
    }

    public String getNightTv() {
        return nightTv;
    }

    public String getEveningTv() {
        return eveningTv;
    }

    public String getMorningTv() {
        return morningTv;
    }

    public String getDayPOP() {
        return dayPOP;
    }

    public String getNightPOP() {
        return nightPOP;
    }

    public String getEveningPOP() {
        return eveningPOP;
    }

    public String getMorningPOP() {
        return morningPOP;
    }

    public String getDayTempTv() {
        return dayTempTv;
    }

    public String getNightTempTv() {
        return nightTempTv;
    }

    public String getEveningTempTv() {
        return eveningTempTv;
    }

    public String getMorningTempTv() {
        return morningTempTv;
    }

    public String getDayDescTv() {
        return dayDescTv;
    }

    public String getNightDescTv() {
        return nightDescTv;
    }

    public String getEveningDescTv() {
        return eveningDescTv;
    }

    public String getMorningDescTv() {
        return morningDescTv;
    }

    public String getDayConDescTv() {
        return dayConDescTv;
    }

    public String getNightConDescTv() {
        return nightConDescTv;
    }

    public String getEveningConDescTv() {
        return eveningConDescTv;
    }

    public String getMorningConDescTv() {
        return morningConDescTv;
    }
}
