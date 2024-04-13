package entities;

import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Data
@AllArgsConstructor
@Builder
@Jacksonized
public class BankAccount {
    private Long id;
    @NonNull
    private String accountName;
    @NonNull
    private Integer cash;
}
