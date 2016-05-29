package com.stog.android.validator;


import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.Calendar;
import java.util.Date;

public class DateValidator implements MaskedValidator {

    public int day;
    public int month;
    public int year;


    private int[] spacers = {2, 5};

    private boolean fullLength;

    // 1.
    @Override
    public void afterTextChanged(Editable source) {
        fullLength = (source.length() >= 5);

        String dateStr = source.toString();

        Date date = StringHelper.getDateForString(dateStr);
        if (date == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        day = 0;
        month = 0;
        year = 0;
        fullLength = false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public boolean hasFullLength() {
        return fullLength;
    }

    @Override
    public boolean isValid() {
        Calendar now = Calendar.getInstance();
        return (day >= 1 && 31 >= day) && (month >= 1 && 12 >= month) && (year < now.get(Calendar.YEAR) + 15);
    }

    @Override
    public String getValue() {
        return String.format("%4d-%02d-%02d", year, month, day);
    }



    /* В строку назначения dest будeт вставлены символы из интервала start-end  фильруемой последовательности source.
    * source - фильруемая последовательность символов.
    * start - индекс первого вставляемого символа в фильтруемой последовательности символов
    * end - индекс поледенего вставляемого символа в фильтруемой последовательности символов
    *
    * dest - Строка назначения
    * dstart - начальная позиция в строке назначения, куда будет вставлена фильтруемая последовательность
    * dend - конечная позиция в строке назначения, куда будет вставлена фильтруемая последовательность
    *
    * */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        SpannableStringBuilder result = new SpannableStringBuilder(source);

        // Вот это проверка и последующая автозамена первого символа дня
        if (dstart == 0  && result.length() > 0 && ('3' < result.charAt(0) && result.charAt(0) <= '9')) {
            result.insert(0, "0");
            end++;
        }

        // Вот это проверка и последующая автозамена первого символа месяца
        if (dstart == 3  && result.length() > 0 && ('1' < result.charAt(0) && result.charAt(0) <= '9')) {
            result.insert(0, "0");
            end++;
        }

        // Вот это проверка и последующая автозамена первого символа месяца
        if (dstart == 2  && result.length() > 0 && ('1' < result.charAt(0) && result.charAt(0) <= '9')) {
            result.insert(0, ".0");
            end++;
        }

        for (int spacer : spacers){

            int distanceToSpacer = spacer - dstart; // расстояние в строке назначения(dest) от первого заменяемого символа до позиции разделителя
            // При удалении последнего символа  dend - dstart = 1
            if (dend - dstart == 0) {
                // Если интервал source включает в себя позицию разделителя и в этой позиции еще нет разделителя (result.charAt(distanceToSpacer))
                if (distanceToSpacer == end || (distanceToSpacer >= 0 && distanceToSpacer < end && result.charAt(distanceToSpacer) != '.')) {
                    result.insert(distanceToSpacer, ".");
                    end++;
                }
            }
        }

        // Посмотрим как будет выглядеть строка после обработки
        String updated = new SpannableStringBuilder(dest).replace(dstart, dend, result, start, end).toString();

        // валидация заведенного дня
        if (updated.length() >= 2) {
            if (updated.charAt(0) == '3' && updated.charAt(1) > '1') {
                return "";
            }
            if (updated.charAt(0) == '0' && updated.charAt(1) == '0') {
                return "";
            }
        }

        // валидация заведенного месяца
        if (updated.length() >= 5) {
            if (updated.charAt(3) != '0' && updated.charAt(4) > '2') {
                return "";
            }
            if (updated.charAt(3) == '0' && updated.charAt(4) == '0') {
                return "";
            }
        }

        // валидация заведенного года
        if (updated.length() >= 7) {
            if (updated.charAt(6) < '1' || updated.charAt(6) > '2') {
                return "";
            }
        }
        if (updated.length() >= 8) {
            if (updated.charAt(6) != '2' && updated.charAt(7) != '9') {
                return "";
            }
            if (updated.charAt(6) != '1' && updated.charAt(7) > '0') {
                return "";
            }
        }

        // Тут ограничение по длине
        if (updated.length() > 10) {
            return "";
        }

        return result;
    }

}
