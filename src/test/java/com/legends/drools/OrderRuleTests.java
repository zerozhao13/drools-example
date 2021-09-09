package com.legends.drools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.legends.drools.entity.ClothOrder;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderRuleTests {

	@Test
	void test() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder clothOrderLT99 = new ClothOrder();
        clothOrderLT99.setOrgPrice(98d);

        ClothOrder clothOrderGT99 = new ClothOrder();
        clothOrderGT99.setOrgPrice(198d);

        kieSession.insert(clothOrderLT99);
        kieSession.insert(clothOrderGT99);

        kieSession.fireAllRules();
        kieSession.dispose();
        assertEquals(clothOrderLT99.getOrgPrice(), clothOrderLT99.getRealPrice());
        assertEquals((clothOrderGT99.getOrgPrice() - clothOrderGT99.getRealPrice()), 10);
    }

    @Test
	void testMutiDiscount() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder clothOrder = new ClothOrder();
        clothOrder.setOrgPrice(310d);

        kieSession.insert(clothOrder);

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("muti_"));
        kieSession.dispose();
        assertEquals(clothOrder.getOrgPrice(), 260);
    }

    @Test
	void testSignalDiscount() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder clothOrder = new ClothOrder();
        clothOrder.setOrgPrice(310d);

        kieSession.insert(clothOrder);

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("signal_"));
        kieSession.dispose();
        assertEquals(clothOrder.getRealPrice(), 280);
    }

}
