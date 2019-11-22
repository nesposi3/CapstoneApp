package com.nesposi3.capstoneapp.ui.stockPages;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.nesposi3.capstoneapp.data.StaticUtils;

public class PriceValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        int intVal = (int) value;
        return StaticUtils.centsToDolars(intVal);
    }
}
