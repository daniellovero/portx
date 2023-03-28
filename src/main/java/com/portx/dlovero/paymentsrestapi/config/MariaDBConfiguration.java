package com.portx.dlovero.paymentsrestapi.config;

import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.MariaDBPaymentRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = MariaDBPaymentRepository.class)
public class MariaDBConfiguration {
}
