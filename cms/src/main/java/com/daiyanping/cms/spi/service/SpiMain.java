package com.daiyanping.cms.spi.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiMain {
    public static void main(String[] args) throws IOException {
        ServiceLoader<SpiService> spiLoader = ServiceLoader.load(SpiService.class);
        Iterator<SpiService> iteratorSpi = spiLoader.iterator();
        while (iteratorSpi.hasNext()) {
            SpiService spiService = iteratorSpi.next();
            spiService.sayHello();
        }
    }


}
