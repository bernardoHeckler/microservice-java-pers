package br.edu.atitus.currency_service.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import br.edu.atitus.currency_service.clients.HolidayClient;
import br.edu.atitus.currency_service.clients.HolidayResponse;

@Component
public class BusinessDayFinder {
    
    private final HolidayClient holidayClient;
    
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public BusinessDayFinder(HolidayClient holidayClient) {
        this.holidayClient = holidayClient;
    }

    public String getUltimoDiaUtilBCBFormat() {
        LocalDate dataBusca = LocalDate.now();
        Set<LocalDate> feriados = getBrazilianHolidays(dataBusca.getYear());

        while (!isBusinessDay(dataBusca, feriados)) {
            dataBusca = dataBusca.minusDays(1);
        }
        
        return dataBusca.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    private boolean isBusinessDay(LocalDate date, Set<LocalDate> feriados) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }
        return !feriados.contains(date);
    }
    
    @Cacheable(value = "Holidays", key = "#year")
    private Set<LocalDate> getBrazilianHolidays(int year) {
        try {
            List<HolidayResponse> responses = holidayClient.getHolidays(String.valueOf(year));
            
            return responses.stream()
                .map(h -> LocalDate.parse(h.getDate(), API_DATE_FORMAT))
                .collect(Collectors.toSet());
        } catch (Exception e) {
            System.err.println("WARN: Falha ao obter lista de feriados, a verificação de feriados será ignorada.");
            return Collections.emptySet();
        }
    }
}
// Bernardo Heckler