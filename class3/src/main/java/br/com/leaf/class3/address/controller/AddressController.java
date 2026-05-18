package br.com.leaf.class3.address.controller;

import br.com.leaf.class3.address.dto.AddressResponse;
import br.com.leaf.class3.address.dto.AddressZipCodeResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private static final String URL = "https://nominatim.openstreetmap.org";

    private final RestClient restClient;

    public AddressController(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(URL)
                .defaultHeader("User-Agent", "class3-api/1.0")
                .build();
    }

    @GetMapping("/search")
    public List<AddressResponse> searchAddress(@RequestParam
                                               @NotBlank(message = "Cidade é obrigatória")
                                               @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
                                               String cidade,

                                               @RequestParam
                                               @NotBlank(message = "Estado é obrigatório")
                                               @Size(min = 2, max = 2, message = "Estado deve possuir 2 caracteres")
                                               String estado,

                                               @RequestParam
                                               @NotBlank(message = "País é obrigatório")
                                               @Size(min = 2, max = 100, message = "País deve ter entre 2 e 100 caracteres")
                                               String pais) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("city", cidade)
                        .queryParam("state", estado)
                        .queryParam("country", pais)
                        .queryParam("format", "json")
                        .queryParam("addressdetails", "1")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<AddressResponse>>() {
                });
    }

    @GetMapping("/search/zipCode")
    public AddressZipCodeResponse searchByZipCode(
            @RequestParam
            @NotBlank(message = "CEP é obrigatório")
            @Size(min = 8, max = 8, message = "CEP deve possuir 8 caracteres")
            String cep) {
        return restClient.get()
                .uri("https://viacep.com.br/ws/{cep}/json/", cep)
                .retrieve()
                .body(AddressZipCodeResponse.class);

    }


}