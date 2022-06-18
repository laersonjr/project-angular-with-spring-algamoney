package com.algamoneyapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "nome")
    private String nome;

    @Embedded
    private Endereco endereco;

    @NotNull
    @Column(name = "ativo")
    private Boolean ativo;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long id) {
        this.codigo = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa)) return false;

        Pessoa pessoa = (Pessoa) o;

        return getCodigo().equals(pessoa.getCodigo());
    }

    @Override
    public int hashCode() {
        return getCodigo().hashCode();
    }
}