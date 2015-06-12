package top.rainynight.core.util;

import org.junit.Assert;
import org.junit.Test;


public class PageTest {

    @Test
    public void test(){
        Page p = new Page();
        p.setPageNumber(7);
        p.setRecordsCount(801);

        Assert.assertEquals(p.getStart(),121);
        Assert.assertEquals(p.getPagesCount(),41);
    }

}
