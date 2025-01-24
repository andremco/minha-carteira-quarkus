package org.finance.mappers;

import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class})
public interface DashboardMapper {
    ValoresCarteiraResponse toValoresCarteiraResponse(String totalCarteira, String totalCarteiraAtualizado, String lucroOuPerda, Boolean balancoPositivo);
}
