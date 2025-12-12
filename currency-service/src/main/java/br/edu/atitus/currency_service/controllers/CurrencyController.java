package br.edu.atitus.currency_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.currency_service.clients.CurrencyBCClient;
import br.edu.atitus.currency_service.clients.CurrencyBCResponse;
import br.edu.atitus.currency_service.entities.CurrencyEntity;
import br.edu.atitus.currency_service.repositories.CurrencyRepository;
import br.edu.atitus.currency_service.utils.BusinessDayFinder; // Novo Import

@RestController
@RequestMapping("currency")
public class CurrencyController {
	
	private final CurrencyRepository repository;
	private final CurrencyBCClient currencyBCClient;
	private final CacheManager cacheManager;
	private final BusinessDayFinder businessDayFinder; // Novo campo injetado
	
	@Value("${server.port}")
	private int serverPort;

	public CurrencyController(CurrencyRepository repository, CurrencyBCClient currencyBCClient, CacheManager cacheManager, BusinessDayFinder businessDayFinder) {
		super();
		this.repository = repository;
		this.currencyBCClient = currencyBCClient;
		this.cacheManager = cacheManager;
		this.businessDayFinder = businessDayFinder; // Injeção
	}
	
	@GetMapping("/{value}/{source}/{target}")
	public ResponseEntity<CurrencyEntity> getConversion(
			@PathVariable double value,
			@PathVariable String source,
			@PathVariable String target) throws Exception{
		
		source = source.toUpperCase();
		target = target.toUpperCase();
		String dataSource = "None";
		String keyCache = source + target;
		String nameCache = "CurrencyCache";
		
		// 1. TENTATIVA DE CACHE
		CurrencyEntity currency = cacheManager.getCache(nameCache).get(keyCache, CurrencyEntity.class);
		
		if (currency != null) {
			dataSource = "Cache";
		} else {
			currency = new CurrencyEntity();
			currency.setSource(source);
			currency.setTarget(target);
			
			if (source.equals(target)) {
				currency.setConversionRate(1);
			} else {
				
                String dataCotacao = businessDayFinder.getUltimoDiaUtilBCBFormat();
				
				try {
					double sourceRate = 1;
					double targetRate = 1;
					
					if (!source.equals("BRL")) {
						CurrencyBCResponse resp = currencyBCClient.getCurrencyBC(source, dataCotacao);
						if (resp.getValue().isEmpty()) throw new Exception("BCB returned no data for source.");
						sourceRate = resp.getValue().get(resp.getValue().size() - 1).getCotacaoVenda();
					}
					if (!target.equals("BRL")) {
						CurrencyBCResponse resp = currencyBCClient.getCurrencyBC(target, dataCotacao);
						if (resp.getValue().isEmpty()) throw new Exception("BCB returned no data for target.");
						targetRate = resp.getValue().get(resp.getValue().size() - 1).getCotacaoVenda();
					}
					
					currency.setConversionRate(targetRate / sourceRate); 
					dataSource = "API BCB";
				} catch (Exception e) {
					currency = repository.
							findBySourceAndTarget(source, target)
							.orElseThrow(() -> new Exception("Currency not found in BCB or Local DB"));
					dataSource = "Local Database";
				}
			}
			
			cacheManager.getCache(nameCache).put(keyCache, currency);
		}
		
		
		
		currency.setConvertedValue(value * currency.getConversionRate());
		currency.setEnviroment("Currency running in port: " + serverPort
								+ " - Source: " + dataSource);
		
		
		return ResponseEntity.ok(currency);
		// Bernardo Heckler
	}
}