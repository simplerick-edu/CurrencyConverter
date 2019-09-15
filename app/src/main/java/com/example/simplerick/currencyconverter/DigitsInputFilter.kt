package com.example.simplerick.currencyconverter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern


class DigitsInputFilter(afterDot : Int) : InputFilter {

    internal var mPattern = Pattern.compile("(0|[1-9]+[0-9]*)(\\.[0-9]{0,${afterDot}})?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val result = (dest.subSequence(0, dstart).toString()
                + source.toString()
                + dest.subSequence(dend, dest.length))

        val matcher = mPattern.matcher(result)

        return if (!matcher.matches()) "" else null

    }
}