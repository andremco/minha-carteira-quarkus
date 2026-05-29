package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.CoinMapper;
import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.request.moeda.EditarMoedaRequest;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.finance.repositories.mariadb.MoedaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class MoedaService {
    @Inject
    MoedaRepository moedaRepository;
    @Inject
    CoinMapper coinMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    CoinService coinService;

    public MoedaResponse salvar(SalvarMoedaRequest request) {
        var codigo = request.getCodigo().toUpperCase();
        var existePorNome = moedaRepository.find("dataRegistroRemocao is null and nome = :nome",
                Parameters.with("nome", request.getNome())).count();
        var existePorCodigo = moedaRepository.find("dataRegistroRemocao is null and codigo = :codigo",
                Parameters.with("codigo", codigo)).count();

        if(existePorNome != 0 || existePorCodigo != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var moeda = coinMapper.toMoeda(request);
        moedaRepository.persist(moeda);

        return coinMapper.toMoedaResponse(moeda, obterCotacao(moeda.getCodigo()));
    }

    public MoedaResponse editar(EditarMoedaRequest request) throws NegocioException {
        Moeda moeda = moedaRepository.findById(request.getId().longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        String codigo = request.getCodigo() != null ? request.getCodigo().toUpperCase() : null;
        Moeda findPorNome = null;
        Moeda findPorCodigo = null;

        if (request.getNome() != null)
            findPorNome = moedaRepository.find("dataRegistroRemocao is null and nome = :nome",
                    Parameters.with("nome", request.getNome())).firstResult();
        if (codigo != null)
            findPorCodigo = moedaRepository.find("dataRegistroRemocao is null and codigo = :codigo",
                    Parameters.with("codigo", codigo)).firstResult();

        if (findPorNome != null && !Objects.equals(findPorNome.getId(), request.getId()) ||
                findPorCodigo != null && !Objects.equals(findPorCodigo.getId(), request.getId()))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getNome() != null)
            moeda.setNome(request.getNome());
        if (codigo != null)
            moeda.setCodigo(codigo);
        if (request.getNota() != null)
            moeda.setNota(request.getNota());

        moeda.setDataRegistroEdicao(LocalDateTime.now());
        moedaRepository.persist(moeda);

        return coinMapper.toMoedaResponse(moeda, obterCotacao(moeda.getCodigo()));
    }

    public MoedaResponse detalharMoeda(Integer id){
        Moeda moeda = moedaRepository.findById(id.longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        return coinMapper.toMoedaResponse(moeda, obterCotacao(moeda.getCodigo()));
    }

    public void excluir(Integer id) throws NegocioException {
        Moeda moeda = moedaRepository.findById(id.longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        moeda.setDataRegistroRemocao(LocalDateTime.now());
        moedaRepository.persist(moeda);
    }

    public Paginado<MoedaResponse> filtrarMoedas(String descricaoAtivo, Integer pagina, Integer tamanho){
        long totalMoedas = total(descricaoAtivo);
        var moedas = moedaRepository.findMoedasPaged(descricaoAtivo, pagina, tamanho);

        List<MoedaResponse> response = new ArrayList<>();
        moedas.forEach(moeda -> response.add(coinMapper.toMoedaResponse(moeda, obterCotacao(moeda.getCodigo()))));

        return Paginado.<MoedaResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalMoedas)
                .itens(response)
                .build();
    }

    public long total(String descricaoAtivo){
        return moedaRepository.total(descricaoAtivo);
    }

    private CoinResponse obterCotacao(String codigo) {
        var cotacao = coinService.obter(codigo);
        if (cotacao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());
        return cotacao;
    }
}
