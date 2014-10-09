package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

/**
 * Created by asamolov on 25/09/14.
 */
public class TestClearCache extends EntityTest {

    @Test
    public void testClearCache() throws Exception {
        this.clearCache();
    }
}
