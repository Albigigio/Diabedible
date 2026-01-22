
package com.example.diabedible.model.reading;

public class ReadingTrendSummary {
    private final int days;
    private final int count;
    private final double avg;
    private final double min;
    private final double max;
    private final double okPercent;

    public ReadingTrendSummary(int days, int count, double avg, double min, double max, double okPercent) {
        this.days = days;
        this.count = count;
        this.avg = avg;
        this.min = min;
        this.max = max;
        this.okPercent = okPercent;
    }

    public int getDays() { return days; }
    public int getCount() { return count; }
    public double getAvg() { return avg; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getOkPercent() { return okPercent; }

    @Override
    public String toString() {
        return days + " giorni | n=" + count +
                " | avg=" + round(avg) +
                " | min=" + round(min) +
                " | max=" + round(max) +
                " | OK%=" + round(okPercent) + "%";
    }

    private double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
