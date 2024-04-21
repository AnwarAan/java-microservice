package microservice.inventoryservice.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import microservice.inventoryservice.model.Inventory;
import microservice.inventoryservice.repository.InventoryRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iPhone X");
        inventory.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setSkuCode("iPhone Xs");
        inventory2.setQuantity(0);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory2);
    }
}
