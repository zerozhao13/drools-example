package com.legends.drools.service;

import com.legends.drools.entity.ClothOrder;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl {
        @Resource KieBase kbase;
        private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
        public void payOrder(ClothOrder order) {
                KieSession kieSession = kbase.newKieSession();
                kieSession.insert(order);
                kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("order_discount_"));
                logger.debug("after discount, the order is : {}", order.toString());
                kieSession.dispose();
        }
}
