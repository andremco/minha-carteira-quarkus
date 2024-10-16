package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AcaoMapper;
import org.finance.models.data.Acao;
import org.finance.models.data.Setor;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AcaoService {
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    AcaoMapper acaoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    public AcaoResponse salvar(SalvarAcaoRequest request) {
        if(acaoRepository.count("razaoSocial", request.getRazaoSocial()) != 0 ||
                acaoRepository.count("ticker", request.getTicker()) != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var setor = setorRepository.findById(request.getSetorId().longValue());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var acao = acaoMapper.toAcao(request, setor);
        acaoRepository.persist(acao);

        return acaoMapper.toAcaoResponse(acao, setor);
    }

    public AcaoResponse editar(EditarAcaoRequest request) throws NegocioException {
        var setor = setorRepository.findById(request.getSetorId().longValue());
        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        Acao acao = acaoRepository.findById(request.getId().longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        long totalPorRazaoSocial = acaoRepository.count("razaoSocial", request.getRazaoSocial());
        long totalPorTicker = acaoRepository.count("ticker", request.getTicker());

        if (totalPorRazaoSocial >= 1 || totalPorTicker >= 1)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getRazaoSocial() != null)
            acao.setRazaoSocial(request.getRazaoSocial());
        if (request.getSetorId() != null)
            acao.setSetor(setor);
        if (request.getTicker() != null)
            acao.setTicker(request.getTicker());
        if (request.getEhFIIs() != null)
            acao.setEhFIIs(request.getEhFIIs());
        if (request.getNota() != null)
            acao.setNota(request.getNota());

        acao.setDataRegistroEdicao(LocalDateTime.now());
        acaoRepository.persist(acao);

        return acaoMapper.toAcaoResponse(acao, setor);
    }

    public void excluir(Integer id) throws NegocioException {
        Acao acao = acaoRepository.findById(id.longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        acao.setDataRegistroRemocao(LocalDateTime.now());
        acaoRepository.persist(acao);
    }

    public Paginado<AcaoResponse> filtrarAcoes(Integer pagina, Integer tamanho){
        long totalAcoes = total();
        Paginado<AcaoResponse> paginado = Paginado.<AcaoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAcoes)
                .itens(acaoMapper.toAcoesResponse(acaoRepository.findAcoesPaged(pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(){ return acaoRepository.count(); }
}
