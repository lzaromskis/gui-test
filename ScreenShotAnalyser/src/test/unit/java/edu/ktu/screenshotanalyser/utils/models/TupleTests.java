package edu.ktu.screenshotanalyser.utils.models;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;

public class TupleTests {
    @Test
    void testConstructorIntIntLongValues() {
        var t = new Tuple<Integer, Integer, Long>(1, 2, 3l);

        Assert.assertEquals((Integer) 1, t.first);
        Assert.assertEquals((Integer) 2, t.second);
        Assert.assertEquals((Long) 3l, t.third);
    }
}
