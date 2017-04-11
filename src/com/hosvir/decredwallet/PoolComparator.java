package com.hosvir.decredwallet;

import java.util.Comparator;

/**
 * @author captain-redbeard
 * @version 1.00
 * @since 3/04/17
 */
public class PoolComparator implements Comparator<Pool> {
    @Override
    public int compare(Pool p1, Pool p2) {
        return p1.getName().compareTo(p2.getName());
    }
}
