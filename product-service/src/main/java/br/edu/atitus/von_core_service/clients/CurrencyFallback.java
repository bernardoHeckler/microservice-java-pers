package br.edu.atitus.von_core_service.clients;

import org.springframework.stereotype.Component;

@Component
public class CurrencyFallback implements CurrencyClient{

	@Override
	public CurrencyResponse getCurrency(double value, String source, String target) {
		
		return null;
	}

}
//Bernardo Heckler