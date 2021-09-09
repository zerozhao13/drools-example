package com.legends.drools;

import com.legends.drools.entity.ClothOrder;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.core.io.impl.UrlResource;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RemoteRuleTests {

    private UrlResource urlResource;

    @Test
    void test() throws IOException {
        //规则在business central中的发布地址
        String url = "http://127.0.0.1:8080/business-central-7.58.0.Final-wildfly19/maven2/";

        KieServices kServices = KieServices.Factory.get();

        UrlResource urlResource = (UrlResource) kServices.getResources().newUrlResource(url);
        //business central的用户名密码，否则会401
        urlResource.setUsername("droolsAdmin");
        urlResource.setPassword("Qweasd!23");
        urlResource.setBasicAuthentication("enabled");

        InputStream inputStream = urlResource.getInputStream();
        KieRepository kieRepository = kServices.getRepository();
        KieModule kieModule = kieRepository.addKieModule(kServices.getResources().newInputStreamResource(inputStream));

        KieContainer kieContainer = kServices.newKieContainer(kieModule.getReleaseId());
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
