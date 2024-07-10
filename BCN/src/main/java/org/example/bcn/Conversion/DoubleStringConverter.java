package org.example.bcn.Conversion;

import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

public class DoubleStringConverter  extends StringConverter<Double> {

    private DecimalFormat decimalFormat;
    public DoubleStringConverter() {
        this.decimalFormat = new DecimalFormat("#,##0.00");
    }
    @Override
    public Double fromString(String value) {
        if(value == null || value.trim().isEmpty()){
            return null;
        }
        try {
            return decimalFormat.parse(value.trim()).doubleValue();
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String toString(Double value) {
        if(value == null) {
            return "";
        }
        return decimalFormat.format(value);  //format the double to currency format  #,##0.00
    }

}
