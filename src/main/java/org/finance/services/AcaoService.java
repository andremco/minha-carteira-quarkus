package org.finance.services;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AcaoMapper;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.SetorRepository;

@ApplicationScoped
public class AcaoService {
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    AcaoMapper acaoMapper;
    @ConfigProperty(name = "registro.nao.encontrado")
    String registroNaoEncontrado;
    @ConfigProperty(name = "registro.ja.existe")
    String registroJaExiste;

    public AcaoResponse salvar(SalvarAcaoRequest request) throws NotImplementedException {
        if(acaoRepository.count("descricao", request.getRazaoSocial()) != 0 ||
                acaoRepository.count("ticker", request.getRazaoSocial()) != 0)
            throw new NegocioException(registroJaExiste);

        var setor = setorRepository.findById(request.getSetorId().longValue());

        if (setor == null)
            throw new NegocioException(registroJaExiste);

        throw new NotImplementedException();
    }
}
