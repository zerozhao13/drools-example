package com.legends.drools;

import java.util.ArrayList;
import java.util.List;

import com.legends.drools.entity.ClothOrder;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GrammerTests {

    @Test
    void testPackageGlobal() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        kieSession.setGlobal("quantity", 10);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("global_package_"));
        kieSession.dispose();
    }
    
    @Test
    void testArrayGlobal() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        List<String> tshirtList = new ArrayList<String> ();
        tshirtList.add("adidas");

        kieSession.setGlobal("tshirts", tshirtList);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("global_array_"));
        System.out.println("now we have how many tshits ? " + tshirtList.size());
        kieSession.dispose();
    }
        
    @Test
    void testQuery() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder coAnta = ClothOrder.builder().sku("anta").build();
        ClothOrder coNike = ClothOrder.builder().sku("nike").build();

        kieSession.insert(coAnta);
        kieSession.insert(coNike);

        QueryResults qr = kieSession.getQueryResults("queryNike");
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("grammer_query_"));

        System.out.println("find how many orders ? " + qr.size());

        for(QueryResultsRow qrr : qr) {
            System.out.println("the order sku is : " + ((ClothOrder)qrr.get("$order")).getSku());
        }

        kieSession.dispose();
    }

    @Test
    void testQueryWithParam() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder coAntaGt99 = ClothOrder.builder().sku("anta").orgPrice(199).build();
        ClothOrder coAntaLt99 = ClothOrder.builder().sku("anta").orgPrice(49).build();

        kieSession.insert(coAntaGt99);
        kieSession.insert(coAntaLt99);

        QueryResults qr = kieSession.getQueryResults("queryGt99", 99);
       // kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("grammer_query_"));

        System.out.println("find how many orders ? " + qr.size());
        
        for(QueryResultsRow qrr : qr) {
            System.out.println("the order price is : " + ((ClothOrder)qrr.get("$order")).getOrgPrice());
        }

        kieSession.dispose();
    }

    @Test
    void lhsPlusInTest() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieContainer = kServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        ClothOrder coAnta = ClothOrder.builder().sku("anta").orgPrice(199).build();
        ClothOrder coXtep = ClothOrder.builder().sku("xtep").orgPrice(49).build();
        ClothOrder coLining = ClothOrder.builder().sku("lining").orgPrice(199).build();
        ClothOrder coNike = ClothOrder.builder().sku("anta").orgPrice(49).build();

        kieSession.insert(coAnta);
        kieSession.insert(coXtep);
        kieSession.insert(coLining);
        kieSession.insert(coNike);

        List<ClothOrder> clothOrders = new ArrayList<ClothOrder>();

        kieSession.setGlobal("orderList", clothOrders);
       // QueryResults qr = kieSession.getQueryResults("queryGt99", 99);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("lhsplus_cloth_madeInChina"));

        kieSession.dispose();
    }
}
