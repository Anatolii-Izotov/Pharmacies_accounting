package customer.controller;


import customer.client.BadRequestException;
import customer.client.MedicationsClient;
import customer.controller.payload.NewMedicationPayload;
import customer.entity.Medication;
import customer.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/medications")
public class MedicationsController {

    private final MedicationsClient medicationsClient;

    @GetMapping("list")
    public Mono<String> getMedicationsListPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        //model.addAttribute("filter", filter); // вне реактивного стрима
        return this.medicationsClient.findAllMedications(filter)
                .collectList()
                .doOnNext(medications -> {
                    model.addAttribute("medications", medications);
                    model.addAttribute("filter", filter);
                })
                .thenReturn("customer/medications/list");
    }

    @GetMapping("stock")
    public Mono<String> getMedicationsInStockListPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        return this.medicationsClient.findAllMedicationsInStock(filter)
                .collectList()
                .doOnNext(medicationsInStock -> {
                    List<Medication> medications = medicationsInStock.stream()
                            .map(Stock::medication)
                            .collect(Collectors.toList());
                    model.addAttribute("medicationsInStock", medicationsInStock);
                    model.addAttribute("medications", medications);
                    model.addAttribute("filter", filter);
                })

                .thenReturn("customer/medications/stock");
    }

    @GetMapping("create")
    public Mono<String> getNewMedicationPage() {
        return Mono.just("customer/medications/new_medication");
    }

    @PostMapping("create")
    public Mono<String> createNewMedication(NewMedicationPayload payload, Model model) {
//        model.addAttribute("inFavourites", false);
        return this.medicationsClient.createMedication(payload.name(), payload.description(), payload.manufacturer(),
                        payload.price(), payload.category())
                .map(medication -> "redirect:/customer/medications/create")
                .onErrorResume(BadRequestException.class, exception -> {
                    model.addAttribute("payload", payload);
                    model.addAttribute("errors", exception.getErrors());
                    return Mono.just("customer/medications/new_medication");
                })
                .doOnError(ex -> {
                    // Логируем общие ошибки
                    System.err.println("Error: " + ex.getMessage());
                    model.addAttribute("error", ex.getMessage());
                });
    }
}
