package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.SplitResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SplitResultService {
    public SplitResult calculatesDebtorsAndCreditors(Long groupId) {
//        List<BigDecimal> valores = new ArrayList<>();
//        List<Integer> pesos = new ArrayList<>();
//        contasConsolidadas.getResponsaveis().forEach(r -> {
//            Integer peso = r.getPeso();
//            pesos.add(peso);
//            for (Conta conta : r.getContas()) {
//                valores.add(conta.getValor());
//            }
//        });
//
//        double somaDosValores = valores.stream()
//                .map(BigDecimal::doubleValue).mapToDouble(Double::doubleValue).sum();
//        int somaDePesos = pesos.stream().mapToInt(Integer::intValue).sum();
//
//        contasConsolidadas
//                .getResponsaveis()
//                .forEach(r -> r.ponderarValores(somaDosValores, somaDePesos));


        return null;
    }
}
