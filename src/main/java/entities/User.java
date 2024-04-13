package entities;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class User {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private Integer age;
    @NonNull
    private Address address;
    private List<BankAccount> bankAccounts;

    public void addBankAccount(BankAccount account) {
        if (this.bankAccounts == null) {
            this.bankAccounts = new ArrayList<>();
        }
        this.bankAccounts.add(account);
    }
}
