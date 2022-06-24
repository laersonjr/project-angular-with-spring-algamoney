package com.algamoneyapi.service;

import com.algamoneyapi.model.Pessoa;
import com.algamoneyapi.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizar(Long codigo, Pessoa pessoa){
        //Se não for encontrado pelo menos 1 recurso, vai chamar uma exceção
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        //Copiar os dados recebido através da "pessoa", para "pessoaSalva" ignorando o codigo
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public Pessoa buscarPessoaPeloCodigo(Long codigo) {
        Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
        return pessoaSalva;
    }
}
