package com.algamoneyapi.repository.lancamento;

import com.algamoneyapi.model.Lancamento;
import com.algamoneyapi.model.Lancamento_;
import com.algamoneyapi.repository.filter.LancamentoFilter;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{

    //Injetar a classe.
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        //From...
        Root<Lancamento> root = criteria.from(Lancamento.class);

        //Criar as restrições
        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);

        return query.getResultList();

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
}
