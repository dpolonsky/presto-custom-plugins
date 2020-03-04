package com.salesforce.prestosql.udfs.scalar;

import io.airlift.slice.Slice;
import io.airlift.slice.SliceUtf8;
import io.prestosql.spi.function.Description;
import io.prestosql.spi.function.ScalarFunction;
import io.prestosql.spi.function.SqlType;
import io.prestosql.spi.type.StandardTypes;

public class CustomStringFunctions
{

    @Description("returns index of first occurrence of a substring (or 0 if not found)")
    @ScalarFunction("locate")
    @SqlType(StandardTypes.BIGINT)
    public static long locateString(@SqlType(StandardTypes.VARCHAR) Slice substring, @SqlType(StandardTypes.VARCHAR) Slice string)
    {
        if (substring.length() == 0) {
            return 1;
        }
        if (string.length() == 0) {
            return 0;
        }
        return stringPosition(string, substring);
    }

    private static long stringPosition(Slice string, Slice substring)
    {
        if (substring.length() == 0) {
            return 1L;
        }
        else {
            int index = string.indexOf(substring);
            return index < 0 ? 0L : (long) (SliceUtf8.countCodePoints(string, 0, index) + 1);
        }
    }
}


