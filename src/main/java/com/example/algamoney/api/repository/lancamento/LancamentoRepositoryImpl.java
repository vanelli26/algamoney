package com.example.algamoney.api.repository.lancamento;

import com.example.algamoney.api.dto.LancamentoEstatisticaCategoria;
import com.example.algamoney.api.dto.LancamentoEstatisticaDia;
import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(ResumoLancamento.class
                , root.get("codigo")
                , root.get("descricao")
                , root.get("dataVencimento")
                , root.get("dataPagamento")
                , root.get("valor")
                , root.get("tipo")
                , root.get("categoria").get("nome")
                , root.get("pessoa").get("nome"))
        );

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    @Override
    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentoEstatisticaCategoria> criteria = builder.createQuery(LancamentoEstatisticaCategoria.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);
        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.select(builder.construct(LancamentoEstatisticaCategoria.class
                , root.get("categoria")
                , builder.sum(root.get("valor")))
        );

        criteria.where(
                builder.greaterThanOrEqualTo(root.get("dataVencimento"), primeiroDia),
                builder.lessThanOrEqualTo(root.get("dataVencimento"), ultimoDia)
        );

        criteria.groupBy(root.get("categoria"));

        TypedQuery<LancamentoEstatisticaCategoria> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentoEstatisticaDia> criteria = builder.createQuery(LancamentoEstatisticaDia.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);
        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.select(builder.construct(LancamentoEstatisticaDia.class
                , root.get("tipo")
                , root.get("dataVencimento")
                , builder.sum(root.get("valor")))
        );

        criteria.where(
                builder.greaterThanOrEqualTo(root.get("dataVencimento"), primeiroDia),
                builder.lessThanOrEqualTo(root.get("dataVencimento"), ultimoDia)
        );

        criteria.groupBy(root.get("tipo"), root.get("dataVencimento"));

        TypedQuery<LancamentoEstatisticaDia> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentoEstatisticaPessoa> criteria = builder.createQuery(LancamentoEstatisticaPessoa.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(LancamentoEstatisticaPessoa.class
                , root.get("tipo")
                , root.get("pessoa")
                , builder.sum(root.get("valor")))
        );

        criteria.where(
                builder.greaterThanOrEqualTo(root.get("dataVencimento"), inicio),
                builder.lessThanOrEqualTo(root.get("dataVencimento"), fim)
        );

        criteria.groupBy(root.get("tipo"), root.get("pessoa"));

        TypedQuery<LancamentoEstatisticaPessoa> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }


}