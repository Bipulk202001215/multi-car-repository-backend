package com.multicar.repository.demo.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserRoleIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        LocalDateTime now = LocalDateTime.now();
        // Format: URID + year + month + day + hour + minute + second
        // Example: URID241225143052
        String timestamp = String.format("%02d%02d%02d%02d%02d%02d",
                now.getYear() % 100,  // Last 2 digits of year
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond());
        return "URID" + timestamp;
    }
}



