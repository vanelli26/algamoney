package com.example.algamoney.api.service;

import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.mail.Mailer;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.UsuarioRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.example.algamoney.api.storage.S3;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LancamentoService {

    private static final String DESTINATARIO = "ROLE_PESQUISAR_LANCAMENTO";
    private static final Logger LOGGER = LoggerFactory.getLogger(LancamentoService.class);

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    Mailer mailer;

    @Autowired
    private S3 s3;

    public Lancamento salvar(Lancamento lancamento) {
        validarPessoa(lancamento);

        if (StringUtils.hasText(lancamento.getAnexo())) {
            s3.salvar(lancamento.getAnexo());
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }
        if (StringUtils.isEmpty(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
            s3.remover(lancamentoSalvo.getAnexo());
        } else if (StringUtils.hasText(lancamento.getAnexo()) && !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
            s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
        }
        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;
        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
        Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
        if (lancamentoSalvo == null) {
            throw new IllegalArgumentException();
        }
        return lancamentoSalvo;
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception{
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos(){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Iniciando envio de e-mail sobre lançamentos vencidos!");
        }

        List<Lancamento> lancamentosVencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
        if (lancamentosVencidos.isEmpty()){
            LOGGER.info("Nenhum lançamento vencido para enviar e-mail!");
            return;
        }
        LOGGER.info("{} lançamentos vencidos!", lancamentosVencidos.size());

        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIO);
        if (destinatarios.isEmpty()){
            LOGGER.warn("Nenhum destinatario encontrado para enviar as notificações!");
            return;
        }

        mailer.avisarSobreLancamentosVencidos(lancamentosVencidos, destinatarios);
        LOGGER.info("Envio de e-mail sobre lançamentos vencidos concluido!");
    }
}