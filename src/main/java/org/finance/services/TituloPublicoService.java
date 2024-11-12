package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.TituloPublicoMapper;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.SetorRepository;
import org.finance.repositories.TituloPublicoRepository;

@ApplicationScoped
public class TituloPublicoService {
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    TituloPublicoMapper tituloPublicoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    public TituloPublicoResponse salvar(SalvarTituloPublicoRequest request){
        if (tituloPublicoRepository.count("descricao", request.getDescricao()) != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());
        var setor = setorRepository.findById(request.getSetorId().longValue());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var tituloPublico = tituloPublicoMapper.toTituloPublico(request, setor);
        tituloPublicoRepository.persist(tituloPublico);

        return tituloPublicoMapper.toTituloPublicoResponse(tituloPublico, setor);
    }
}
