package com.algamoneyapi.repository.lancamento;

import com.algamoneyapi.model.Lancamento;
import com.algamoneyapi.model.Lancamento_;
import com.algamoneyapi.repository.filter.LancamentoFilter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{

    //Injetar a classe.
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        //From...
        Root<Lancamento> root = criteria.from(Lancamento.class);

        //Criando uma lista de ordenação para lançamentos.
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.asc(root.get(Lancamento_.descricao)));
        orderList.add(builder.asc(root.get(Lancamento_.codigo)));
        criteria.orderBy(orderList);

        //Criar as restrições
        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter)) ;

    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        //Para adicionar o modelgen, é necessário dar o build novamente no projeto.
        //Além disso é necessário ir na pasta target do projeto, em annotations, botão direito, Mark directory as Sourcer Roots
        List<Predicate> predicates = new ArrayList<>();

        //where descricao like '%asd%'
        if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())){
            predicates.add(builder.like(
                    builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"
            ));
        }

        if(lancamentoFilter.getDataVencimentoDe() != null){
            //Vai adicionar todos os resultados de dataVencimento sendo maior ou igual a data informada
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe())
            );
        }

        if(lancamentoFilter.getDataVencimentoAte() != null){
            //Vai adicionar todos os resultados de dataVencimento sendo menor ou igual a data informada
           predicates.add(
                   builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte())
           );
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistroPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistroPorPagina);

    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        //select count(*) from lancamento...
        criteria.select(builder.count(root));

        return manager.createQuery(criteria).getSingleResult();
    }
}
