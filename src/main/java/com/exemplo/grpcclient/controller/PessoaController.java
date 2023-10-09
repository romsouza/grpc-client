package com.exemplo.grpcclient.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.exemplo.grpcclient.service.PessoaService;
import com.exemplo.proto.FindByIdResponse;

@RestController
public class PessoaController {
    private final PessoaService pessoaService;

    PessoaController(PessoaService pessoaService){
	this.pessoaService = pessoaService;
    }


    @GetMapping("/pessoa/{id}")
    Map<String, Object> findById(@PathVariable Long id){
	return pessoaService.findPessoaById(id);
    }
}
