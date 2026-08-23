package com.promptmaestro.controller;

import com.promptmaestro.entity.Order;
import com.promptmaestro.entity.Recipe;
import com.promptmaestro.entity.User;
import com.promptmaestro.repository.OrderRepository;
import com.promptmaestro.repository.RecipeRepository;
import com.promptmaestro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupController(UserRepository userRepository, OrderRepository orderRepository, RecipeRepository recipeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.recipeRepository = recipeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/init-admin")
    public Map<String, String> initAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role("ADMIN")
                    .build();
            userRepository.save(admin);
            return Map.of("status", "created", "message", "Admin user created");
        }
        return Map.of("status", "exists", "message", "Admin user already exists");
    }

    @GetMapping("/check")
    public Map<String, Object> check() {
        return Map.of(
            "userCount", userRepository.count(),
            "adminExists", userRepository.existsByUsername("admin")
        );
    }

    @PostMapping("/seed-orders")
    public Map<String, Object> seedOrders() {
        long existing = orderRepository.count();
        if (existing > 0) {
            return Map.of("status", "skipped", "message", "Ya hay " + existing + " pedidos en la BD");
        }

        List<Order> samples = List.of(
            createOrder("María García", "600 123 456", "Tarta de Chocolate", "L", "Papel de Azúcar", "Decoración con flores de chocolate", "COMPLETADO", 65.00),
            createOrder("Juan López", "611 234 567", "Tarta Fresa Natural", "M", "Sin personalización", "Entregar antes de las 14h", "EN_PROCESO", 38.00),
            createOrder("Ana Martínez", "622 345 678", "Tarta Limón Merengada", "S", "Papelería", "Con dedicatoria: Feliz Cumpleaños Laura", "PENDIENTE", 42.00),
            createOrder("Pedro Sánchez", "633 456 789", "Tarta Red Velvet", "XL", "Papel de Azúcar", "Para boda, 150 personas", "PENDIENTE", 95.00),
            createOrder("Laura Fernández", "644 567 890", "Tarta Nuez y Caramelo", "M", "Sin personalización", "", "COMPLETADO", 78.00),
            createOrder("Carlos Ruiz", "655 678 901", "Tarta Vainilla Clásica", "L", "Mezcla", "Cumpleaños niña 5 años, colores pastel", "EN_PROCESO", 52.00),
            createOrder("Elena Díaz", "666 789 012", "Tarta Chocolate", "S", "Sin personalización", "", "CANCELADO", 35.00),
            createOrder("Roberto Moreno", "677 890 123", "Tarta Fresa Natural", "XL", "Papel de Azúcar", "Boda rural, servir 200 personas", "PENDIENTE", 120.00)
        );

        orderRepository.saveAll(samples);
        return Map.of("status", "created", "message", samples.size() + " pedidos de ejemplo creados", "count", samples.size());
    }

    private Order createOrder(String name, String phone, String tarta, String size, String personalization, String notes, String status, double total) {
        Order o = new Order();
        o.setCustomerName(name);
        o.setCustomerPhone(phone);
        o.setTartaName(tarta);
        o.setTartaSize(size);
        o.setPersonalization(personalization);
        o.setNotes(notes);
        o.setStatus(status);
        o.setTotalAmount(total);
        return o;
    }

    @PostMapping("/seed-recipes")
    public Map<String, Object> seedRecipes() {
        long existing = recipeRepository.count();
        if (existing > 0) {
            return Map.of("status", "skipped", "message", "Ya hay " + existing + " recetas en la BD");
        }

        List<Recipe> samples = List.of(
            createRecipe("Bizcocho de Chocolate", "Tarta de Chocolate", "Bizcocho", "200g harina, 200g azúcar, 80g cacao en polvo, 3 huevos, 150ml leche, 100g mantequilla, 1 cucharadita levadura, 1 pizca sal",
                "1. Precalentar horno a 180C. 2. Batir mantequilla con azúcar. 3. Añadir huevos uno a uno. 4. Mezclar harina, cacao y levadura. 5. Alternar mezcla seca con leche. 6. Verter en molde engrasado. 7. Hornear 30-35 min.", 12, 20, 35, "Media", "Usar cacao puro sin azúcar para mejor sabor"),
            createRecipe("Crema Chantilly", "", "Crema", "500ml nata líquida, 50g azúcar glas, 1 vainilla",
                "1. Enfriar la nata y el bol. 2. Batir a velocidad media. 3. Añadir azúcar glas. 4. Batir hasta picos suaves. 5. No sobrebatir o se cortará.", 20, 10, 0, "Fácil", "Mantener bien fría hasta su uso"),
            createRecipe("Ganache de Chocolate", "", "Cobertura", "200g chocolate negro, 200ml nata líquida, 20g mantequilla",
                "1. Picar el chocolate fino. 2. Calentar la nata hasta que hierva. 3. Verter sobre el chocolate. 4. Mezclar en círculos desde el centro. 5. Añadir mantequilla. 6. Dejar enfriar a temperatura ambiente.", 16, 10, 0, "Fácil", "Proporción 1:1 para cobertura firme"),
            createRecipe("Buttercream Americano", "", "Cobertura", "250g mantequilla pomada, 500g azúcar glas, 2 cucharadas leche, 1 vainilla",
                "1. Batir mantequilla 5 min hasta que esté blanca. 2. Añadir azúcar glas poco a poco. 3. Batir 3 min. 4. Añadir leche y vainilla. 5. Batir 2 min más.", 15, 15, 0, "Fácil", "Usar mantequilla a temperatura ambiente"),
            createRecipe("Mousse de Fresa", "", "Relleno", "300g fresas, 200ml nata líquida, 3 claras de huevo, 50g azúcar, 1 sobre gelatina",
                "1. Triturar fresas. 2. Batir nata y reservar. 3. Batir claras con azúcar. 4. Hidratar gelatina y disolver. 5. Mezclar todo suavemente. 6. Refrigerar 4 horas.", 10, 15, 0, "Media", "Usar fresas maduras para mejor sabor"),
            createRecipe("Merengüe Italiano", "", "Cobertura", "200g azúcar, 50ml agua, 2 claras de huevo",
                "1. Cocinar azúcar con agua a 121C. 2. Batir claras a punto de nieve. 3. Verter el almíbar en hilo sobre las claras. 4. Seguir batiendo 10 min hasta enfriar.", 10, 15, 0, "Difícil", "Usar termómetro para controlar la temperatura del almíbar"),
            createRecipe("Pasta de Azúcar", "", "Decoración", "500g azúcar glas, 1 claras de huevo, 1 cucharada glicerina, 1 cucharadita extracto de limón",
                "1. Tamizar azúcar glas. 2. Mezclar claras con glicerina. 3. Añadir al azúcar poco a poco. 4. Amasar 10 min. 5. Envolver en film plástico. 6. Dejar reposar 24h.", 30, 0, 0, "Difícil", "Manipular con manos engrasadas para que no se pegue"),
            createRecipe("Frangipan", "", "Relleno", "150g mantequilla pomada, 150g azúcar, 150g almendra molida, 2 huevos, 1 cucharada harina",
                "1. Batir mantequilla con azúcar. 2. Añadir almendra molida. 3. Batir huevos uno a uno. 4. Añadir harina cernida. 5. Mezclar hasta obtener una masa homogénea.", 15, 5, 0, "Fácil", "Se puede congelar hasta 3 meses")
        );

        recipeRepository.saveAll(samples);
        return Map.of("status", "created", "message", samples.size() + " recetas de ejemplo creadas", "count", samples.size());
    }

    private Recipe createRecipe(String name, String tarta, String category, String ingredients, String instructions, int portions, int prep, int cook, String difficulty, String notes) {
        Recipe r = new Recipe();
        r.setName(name);
        r.setTartaName(tarta);
        r.setCategory(category);
        r.setIngredients(ingredients);
        r.setInstructions(instructions);
        r.setPortions(portions);
        r.setPrepTimeMinutes(prep);
        r.setCookTimeMinutes(cook);
        r.setDifficulty(difficulty);
        r.setNotes(notes);
        r.setActive(true);
        return r;
    }
}
