package br.edu.atitus.currency_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HolidayAPI", url = "https://brasilapi.com.br/api")
public interface HolidayClient {
	// Bernardo Heckler
    @GetMapping("/feriados/v1/{year}")
    List<HolidayResponse> getHolidays(@PathVariable String year);
}