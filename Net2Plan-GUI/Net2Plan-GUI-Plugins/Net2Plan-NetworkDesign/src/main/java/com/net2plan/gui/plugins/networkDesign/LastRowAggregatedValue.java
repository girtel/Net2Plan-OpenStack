package com.net2plan.gui.plugins.networkDesign;

public class LastRowAggregatedValue
{
    private String value;
    private InfNumberFormat numberFormat = new InfNumberFormat("##.##");

    public static final String EMPTY_VALUE = "---";

    public LastRowAggregatedValue()
    {
        value = EMPTY_VALUE;
    }
    public LastRowAggregatedValue(Number val)
    {
        this(val.doubleValue());
    }

    public LastRowAggregatedValue(double val)
    {
        final boolean isDecimal = val % 1 != 0;

        if (isDecimal)
            value = numberFormat.format(val);
        else
            value = numberFormat.format(((Number) val).intValue());
    }


    @Override
    public String toString()
    {
        return value;
    }
}
