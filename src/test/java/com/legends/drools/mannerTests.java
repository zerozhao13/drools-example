package com.legends.drools;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mannerTests {
    @Test
    void testMutiDiscount() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        int quantity = 0;
        kieSession.setGlobal("quantity", quantity);

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("global_"));
        kieSession.dispose();
    }
}
