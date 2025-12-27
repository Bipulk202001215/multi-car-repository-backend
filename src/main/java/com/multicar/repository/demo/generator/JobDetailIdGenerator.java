package com.multicar.repository.demo.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

public class JobDetailIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        LocalDateTime now = LocalDateTime.now();
        // Format: JBID + year + month + day + hour + minute + second + milliseconds
        // Example: JBID20241225143052123
        String timestamp = String.format("%04d%02d%02d%02d%02d%02d%03d",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                now.getNano() / 1_000_000);
        return "JBID" + timestamp;
    }
}



