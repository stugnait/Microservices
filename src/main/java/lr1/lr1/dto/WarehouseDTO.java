package lr1.lr1.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    private Long id;

    @NotBlank(message = "Назва кімнати не може бути порожньою")
    private String name;
}
